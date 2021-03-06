package ru.javawebinar.basejava.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class ListSection extends Section {
    private static final long serialVersionUuid = 1L;

    private List<String> values;

    public ListSection() {
    }

    public ListSection(String... values) {
        this(Arrays.asList(values));
    }

    public ListSection(List<String> values) {
        Objects.requireNonNull(values, "Values must be not null");
        this.values = values;
    }

    public List<String> getValues() {
        return new ArrayList<>(values);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListSection that = (ListSection) o;
        return values.equals(that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values);
    }

    @Override
    public String toString() {
        return String.join("\n", values);
    }
}
