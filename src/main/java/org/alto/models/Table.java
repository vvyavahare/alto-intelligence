package org.alto.models;

import java.util.ArrayList;
import java.util.List;

public class Table {
    public final int size;
    private int occupied = 0;
    private final List<ClientsGroup> seatedGroups = new ArrayList<>();

    public Table(int size) {
        this.size = size;
    }

    public synchronized boolean hasSpaceFor(int groupSize) {
        return (size - occupied) >= groupSize;
    }

    public synchronized boolean isEmpty() {
        return occupied == 0;
    }

    public synchronized boolean isFull() {
        return occupied == size;
    }

    public synchronized void seatGroup(ClientsGroup group) {
        if (!hasSpaceFor(group.size))
            throw new IllegalStateException("Not enough space for group of size " + group.size);
        occupied += group.size;
        seatedGroups.add(group);
    }

    public synchronized void removeGroup(ClientsGroup group) {
        if (seatedGroups.remove(group)) {
            occupied -= group.size;
            if (occupied < 0) occupied = 0;
        }
    }

    @Override
    public synchronized String toString() {
        if (seatedGroups.isEmpty())
            return "Table(size=" + size + ", empty)";
        else
            return "Table(size=" + size + ", occupied=" + occupied + ", groups=" + seatedGroups + ")";
    }
}