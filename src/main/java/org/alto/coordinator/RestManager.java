package org.alto.coordinator;

import org.alto.models.ClientsGroup;
import org.alto.models.Table;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.LinkedList;


public class RestManager {

    private final List<Table> tables;
    private final Map<ClientsGroup, Table> seatedGroups = new HashMap<>();
    private final Queue<ClientsGroup> waitingQueue = new LinkedList<>();

    private final Object lock = new Object();

    public RestManager(List<Table> tables) {
        this.tables = new ArrayList<>(tables);
        this.tables.sort(Comparator.comparingInt(t -> t.size));
    }

    public void onArrive(ClientsGroup group) {
        synchronized (lock) {
            Table table = findAvailableTableFor(group.size);
            if (table != null) {
                table.seatGroup(group);
                seatedGroups.put(group, table);
            } else {
                waitingQueue.add(group);
            }
        }
    }

    public void onLeave(ClientsGroup group) {
        synchronized (lock) {
            Table table = seatedGroups.remove(group);
            if (table != null) {
                table.removeGroup(group);
                assignWaitingGroups();
            } else {
                waitingQueue.remove(group);
            }
        }
    }

    public Table lookup(ClientsGroup group) {
        synchronized (lock) {
            return seatedGroups.get(group);
        }
    }

    private Table findAvailableTableFor(int groupSize) {
        for (Table table : tables) {
            if (table.isEmpty() && table.size >= groupSize)
                return table;
        }
        for (Table table : tables) {
            if (!table.isEmpty() && table.hasSpaceFor(groupSize))
                return table;
        }
        return null;
    }

    private void assignWaitingGroups() {
        if (waitingQueue.isEmpty()) return;

        Iterator<ClientsGroup> iterator = waitingQueue.iterator();
        while (iterator.hasNext()) {
            ClientsGroup group = iterator.next();
            Table table = findAvailableTableFor(group.size);
            if (table != null) {
                table.seatGroup(group);
                seatedGroups.put(group, table);
                iterator.remove();
            }
        }
    }

    public void printTables() {
        synchronized (lock) {
            System.out.println("\n=== Current Tables Status ===");
            for (Table table : tables) {
                System.out.println(table);
            }
            if (!waitingQueue.isEmpty()) {
                System.out.println("\nWaiting Queue:");
                for (ClientsGroup group : waitingQueue) {
                    System.out.println(" - " + group);
                }
            } else {
                System.out.println("\n --No groups waiting --");
            }
            System.out.println("=============================\n");
        }
    }

}