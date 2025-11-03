package org.alto.models;

import java.util.Objects;

public class ClientsGroup {
    public final int size;
    public final String name;

    public ClientsGroup(int size, String name) {
        if (size < 1 || size > 6)
            throw new IllegalArgumentException("Group size must be between 1 and 6");
        this.size = size;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClientsGroup)) return false;
        ClientsGroup that = (ClientsGroup) o;
        return size == that.size && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(size, name);
    }

    @Override
    public String toString() {
        return name + " (" + size + ")";
    }
}