package ru.javawebinar.basejava.model;

import java.util.Objects;

public class TextType extends Section {

    private final String value;

    public TextType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
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
        return value;
    }
}
