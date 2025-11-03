package org.alto.coordinator;

import org.alto.models.ClientsGroup;
import org.alto.models.Table;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.Optional;
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
            findAvailableTableFor(group.size)
                    .ifPresentOrElse(
                            table -> {
                                table.seatGroup(group);
                                seatedGroups.put(group, table);
                            },
                            () -> waitingQueue.add(group)
                    );
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

    private Optional<Table> findAvailableTableFor(int groupSize) {
        return tables.stream()
                .filter(t -> t.isEmpty() && t.size >= groupSize)
                .findFirst()
                .or(() -> tables.stream()
                        .filter(t -> !t.isEmpty() && t.hasSpaceFor(groupSize))
                        .findFirst());
    }

    private void assignWaitingGroups() {
        if (waitingQueue.isEmpty()) return;

        List<ClientsGroup> assigned = waitingQueue.stream()
                .filter(group -> findAvailableTableFor(group.size)
                        .map(table -> {
                            table.seatGroup(group);
                            seatedGroups.put(group, table);
                            return true;
                        })
                        .orElse(false))
                .collect(Collectors.toList());

        waitingQueue.removeAll(assigned);
    }

    public void printTables() {
        synchronized (lock) {
            System.out.println("\n=== Current Tables Status ===");
            tables.forEach(System.out::println);

            if (!waitingQueue.isEmpty()) {
                System.out.println("\nWaiting Queue:");
                waitingQueue.forEach(group -> System.out.println(" - " + group));
            } else {
                System.out.println("\n --No groups waiting --");
            }
            System.out.println("=============================\n");
        }
    }

}