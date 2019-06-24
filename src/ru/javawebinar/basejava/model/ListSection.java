package ru.javawebinar.basejava.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class ListSection extends AbstractSection {
    private static final long serialVersionUuid = 1L;

    private List<String> values;

    public ListSection() {
    }

    public ListSection(List<String> values) {
        this.values = values;
    }

    public List<String> getValues() {
        return new ArrayList<>(values);
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
