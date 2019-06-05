package ru.javawebinar.basejava.model;

import java.time.LocalDate;
import java.util.Objects;

public class Organization {
    private final String companyName;
    private final LocalDate start;
    private final LocalDate end;
    private final String position;
    private final String duties;

    public Organization(String companyName, LocalDate start, LocalDate end, String position, String duties) {
        this.companyName = companyName;
        this.start = start;
        this.end = end;
        this.position = position;
        this.duties = duties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return companyName.equals(that.companyName) &&
                start.equals(that.start) &&
                end.equals(that.end) &&
                position.equals(that.position) &&
                duties.equals(that.duties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyName, start, end, position, duties);
    }

    @Override
    public String toString() {
        return "\n" + companyName + "\n" + start.getMonthValue() + "/" + start.getYear() + " - " + end.getMonthValue() + "/" +
                end.getYear() + "\n" + position + "\n" + duties+ "\n";
    }
}
