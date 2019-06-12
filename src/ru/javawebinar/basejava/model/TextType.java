package ru.javawebinar.basejava.model;

import java.io.Serializable;
import java.util.Objects;

public class TextType extends AbstractSection {
    private static final long serialVersionUuid = 1L;

    private final String value;

    public TextType(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextType textType = (TextType) o;
        return value.equals(textType.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value + "\n";
    }
}
