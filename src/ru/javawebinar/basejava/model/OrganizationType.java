package ru.javawebinar.basejava.model;

import java.util.List;
import java.util.Objects;

public class OrganizationType extends AbstractSection {
    private static final long serialVersionUuid = 1L;
    private final List<Organization> organizations;

    public OrganizationType(List<Organization> organizations) {
        this.organizations = organizations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganizationType that = (OrganizationType) o;
        return Objects.equals(organizations, that.organizations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(organizations);
    }

    @Override
    public String toString() {
        return organizations.toString();
    }
}