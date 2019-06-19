package ru.javawebinar.basejava.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ListSection extends AbstractSection {
    private static final long serialVersionUuid = 1L;

    private List<String> values;

    public ListSection() {
    }
    public ListSection(String... values) {
        this(Arrays.asList(values));
    }

    public ListSection(List<String> values) {
        this.values = values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListSection listType = (ListSection) o;
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
