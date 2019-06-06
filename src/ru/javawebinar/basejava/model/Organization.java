package ru.javawebinar.basejava.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Organization {
    private final String companyName;
    private List<Period> periods;

    public Organization(String companyName, List<Period> periods) {
        this.companyName = companyName;
        this.periods = periods;
    }

    public void addPeriod(Period period) {
        periods.add(period);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return Objects.equals(companyName, that.companyName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(companyName);
    }

    @Override
    public String toString() {
        return "\n" + companyName + "\n" + periods;
    }

    public static class Period {
        private final LocalDate start;
        private final LocalDate end;
        private final String position;
        private final String duties;

        public Period(LocalDate start, LocalDate end, String position, String duties) {
            this.start = start;
            this.end = end;
            this.position = position;
            this.duties = duties;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Period period = (Period) o;
            return Objects.equals(start, period.start) &&
                    Objects.equals(end, period.end) &&
                    Objects.equals(position, period.position) &&
                    Objects.equals(duties, period.duties);
        }

        @Override
        public int hashCode() {
            return Objects.hash(start, end, position, duties);
        }

        @Override
        public String toString() {
            String period = "\n" + start.getMonthValue() + "/" + start.getYear() + " - " + end.getMonthValue() + "/" +
                    end.getYear() + "\n" + position + "\n";
            if (!duties.isEmpty()) {
                period += duties + "\n";
            }
            return period;
        }
    }
}
