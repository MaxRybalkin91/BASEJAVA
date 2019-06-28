package ru.javawebinar.basejava.model;

import ru.javawebinar.basejava.util.LocalDateAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class Organization implements Serializable {
    private static final long serialVersionUuid = 1L;

    private Link link;
    private List<Period> periods;

    public Organization() {
    }

    public Organization(Link link, Period... periods) {
        this(link, Arrays.asList(periods));
    }

    public Organization(Link link, List<Period> periods) {
        this.link = link;
        this.periods = periods;
    }

    public Link getLink() {
        return link;
    }

    public List<Period> getPeriods() {
        return new ArrayList<>(periods);
    }

    public void addPeriod(Period period) {
        periods.add(period);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return Objects.equals(link, that.link);
    }

    @Override
    public int hashCode() {
        return Objects.hash(link, periods);
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Period implements Serializable {
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private LocalDate start;
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private LocalDate end;

        private String position;
        private String duties;

        public Period() {
        }

        public Period(LocalDate start, LocalDate end, String position) {
            this(start, end, position, "");
        }

        public Period(LocalDate start, LocalDate end, String position, String duties) {
            this.start = start;
            this.end = end;
            this.position = position;
            this.duties = duties;
        }

        public LocalDate getStart() {
            return start;
        }

        public LocalDate getEnd() {
            return end;
        }

        public String getPosition() {
            return position;
        }

        public String getDuties() {
            return duties;
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
            String period = "\n" + start.getMonthValue() + "/" + start.getYear() + " - " + end.getMonthValue() + "/" + end.getYear() + "\n" + position + "\n";
            if (!duties.isEmpty()) {
                period += duties + "\n";
            }
            return period;
        }
    }
}
