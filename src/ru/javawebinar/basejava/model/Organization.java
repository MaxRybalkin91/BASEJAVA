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
    private List<Periods> periods;

    public Organization() {
    }

    public Organization(Link link, Periods... periods) {
        this(link, Arrays.asList(periods));
    }

    public Organization(Link link, List<Periods> periods) {
        Objects.requireNonNull(link, "Organization name must be not null");
        Objects.requireNonNull(periods, "Organization name must be not null");
        this.link = link;
        this.periods = periods;
    }

    public Link getLink() {
        return link;
    }

    public List<Periods> getPeriods() {
        return new ArrayList<>(periods);
    }

    public void addPeriod(Periods period) {
        periods.add(period);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return Objects.equals(link, that.link) &&
                Objects.equals(periods, that.periods);
    }

    @Override
    public int hashCode() {
        return Objects.hash(link, periods);
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Periods implements Serializable {
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private LocalDate start;
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private LocalDate end;

        private String position;
        private String duties;

        public Periods() {
        }

        public Periods(LocalDate start, LocalDate end, String position) {
            this(start, end, position, "");
        }

        public Periods(LocalDate start, LocalDate end, String position, String duties) {
            Objects.requireNonNull(start, "Date of start must be not null");
            Objects.requireNonNull(end, "Date of end must be not null");
            Objects.requireNonNull(position, "Position must be not null");
            Objects.requireNonNull(duties, "Duties must be not null");
            this.start = start;
            this.end = end;
            this.position = position;
            this.duties = duties;
        }

        public String getStartDate() {
            return start.toString();
        }

        public String getEndDate() {
            return end.toString();
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
            Periods period = (Periods) o;
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
            String period = "\n" + start.getMonthValue() + "/" + start.getYear() + " - " + end.getMonthValue() + "/"
                    + end.getYear() + "\n" + position;
            if (!duties.equals("")) {
                period += "\n" + duties + "\n";
            }
            return period;
        }
    }
}
