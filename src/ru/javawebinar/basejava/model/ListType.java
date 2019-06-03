package ru.javawebinar.basejava.model;

import java.util.List;
import java.util.Objects;

public class ListType extends AbstractSection {
    private final List<String> values;

    public ListType(List<String> values) {
        this.values = values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListType listType = (ListType) o;
        return Objects.equals(values, listType.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values);
    }

    @Override
    public String toString() {
        for (String a : values) {
            System.out.println(a);
        }
        return "";
    }
}
