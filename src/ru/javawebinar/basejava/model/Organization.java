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
    private List<Stage> stages;

    public Organization() {
    }

    public Organization(Link link, Stage... stages) {
        this(link, Arrays.asList(stages));
    }

    public Organization(Link link, List<Stage> stages) {
        Objects.requireNonNull(link, "Organization link must be not null");
        Objects.requireNonNull(stages, "Organization stages must be not null");
        this.link = link;
        this.stages = stages;
    }

    public Link getLink() {
        return link;
    }

    public List<Stage> getStages() {
        return new ArrayList<>(stages);
    }

    public void addStage(Stage stage) {
        stages.add(stage);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return Objects.equals(link, that.link) &&
                Objects.equals(stages, that.stages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(link, stages);
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Stage implements Serializable {
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private LocalDate start;
        @XmlJavaTypeAdapter(LocalDateAdapter.class)
        private LocalDate end;

        private String position;
        private String duties;

        public Stage() {
        }

        public Stage(LocalDate start, LocalDate end, String position) {
            this(start, end, position, "");
        }

        public Stage(LocalDate start, LocalDate end, String position, String duties) {
            Objects.requireNonNull(start, "Date of start must be not null");
            Objects.requireNonNull(end, "Date of end must be not null");
            Objects.requireNonNull(position, "Position must be not null");
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
            Stage stage = (Stage) o;
            return Objects.equals(start, stage.start) &&
                    Objects.equals(end, stage.end) &&
                    Objects.equals(position, stage.position) &&
                    Objects.equals(duties, stage.duties);
        }

        @Override
        public int hashCode() {
            return Objects.hash(start, end, position, duties);
        }

        @Override
        public String toString() {
            String stage = "\n" + start.getMonthValue() + "/" + start.getYear() + " - " + end.getMonthValue() + "/"
                    + end.getYear() + "\n" + position;
            if (!duties.equals("")) {
                stage += "\n" + duties + "\n";
            }
            return stage;
        }
    }
}
