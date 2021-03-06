package ru.javawebinar.basejava.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public enum SectionType {
    OBJECTIVE("Позиция:"),
    PERSONAL("Личные качества:"),
    ACHIEVEMENT("Достижения:"),
    QUALIFICATION("Квалификация:"),
    EXPERIENCE("Опыт работы:"),
    EDUCATION("Образование:");

    private String title;

    SectionType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}