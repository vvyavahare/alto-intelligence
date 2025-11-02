package org.alto.test;

import org.alto.coordinator.RestManager;
import org.alto.models.ClientsGroup;
import org.alto.models.Table;

import java.util.Arrays;
import java.util.List;

public class RestManagerTest {

    public static void main(String[] args) {
        testPreferEmptyTableOverShared();
        testSeatAtSmallestEmptyTable();
        testTableSharingWhenNoEmptyTables();
        testWaitingQueue();
        System.out.println(" All tests passed successfully!");
    }

    private static void testPreferEmptyTableOverShared() {
        System.out.println("Running testPreferEmptyTableOverShared...");
        List<Table> tables = Arrays.asList(new Table(2), new Table(4));
        RestManager manager = new RestManager(tables);

        // Fill part of the larger table
        ClientsGroup g1 = new ClientsGroup(2, "Group A");
        manager.onArrive(g1);
        assert manager.lookup(g1).size == 2 : "Group A should be at 2-seat table";

        // Fill the 4-seat table partially
        ClientsGroup g2 = new ClientsGroup(2, "Group B");
        manager.onArrive(g2);
        assert manager.lookup(g2).size == 4 : "Group B should be at 4-seat table";

        // Now an empty 2-seat table is available
        manager.onLeave(g1); // frees 2-seat table
        ClientsGroup g3 = new ClientsGroup(2, "Group C");
        manager.onArrive(g3);

        Table t3 = manager.lookup(g3);
        assert t3.size == 2 : "Group C must prefer the empty 2-seat table over shared 4-seat one";
    }

    private static void testSeatAtSmallestEmptyTable() {
        System.out.println("Running testSeatAtSmallestEmptyTable...");
        List<Table> tables = Arrays.asList(new Table(2), new Table(4), new Table(6));
        RestManager manager = new RestManager(tables);

        ClientsGroup g1 = new ClientsGroup(3, "Group A");
        manager.onArrive(g1);

        Table t1 = manager.lookup(g1);
        assert t1.size == 4 : "Group of 3 should be seated at 4-seat table (smallest that fits)";
    }

    private static void testTableSharingWhenNoEmptyTables() {
        System.out.println("Running testTableSharingWhenNoEmptyTables...");
        List<Table> tables = Arrays.asList(new Table(4));
        RestManager manager = new RestManager(tables);

        ClientsGroup g1 = new ClientsGroup(2, "Group A");
        ClientsGroup g2 = new ClientsGroup(2, "Group B");

        manager.onArrive(g1);
        manager.onArrive(g2); // should share the same table

        Table t1 = manager.lookup(g1);
        Table t2 = manager.lookup(g2);

        assert t1 == t2 : "Both groups should share the same 4-seat table";
        assert !t1.isEmpty() && !t1.isFull() == false : "Table should be full after both groups seated";
    }

    private static void testWaitingQueue() {
        System.out.println("Running testWaitingQueue...");
        List<Table> tables = Arrays.asList(new Table(2));
        RestManager manager = new RestManager(tables);

        ClientsGroup g1 = new ClientsGroup(2, "Group A");
        ClientsGroup g2 = new ClientsGroup(2, "Group B");

        manager.onArrive(g1);
        manager.onArrive(g2); // no more space, should wait

        assert manager.lookup(g2) == null : "Group B should be waiting in queue";

        manager.onLeave(g1); // frees table
        assert manager.lookup(g2) != null : "Group B should be seated after Group A leaves";
    }
}