package ru.javawebinar.basejava.storage;

import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.*;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public abstract class AbstractStorageTest {
    protected static final File STORAGE_DIR = new File("/home/maksim/basejava/storage");

    Storage storage;

    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final String UUID_4 = "uuid4";

    private static final Resume RESUME_1;
    private static final Resume RESUME_2;
    private static final Resume RESUME_3;
    private static final Resume RESUME_4;

    static {
        RESUME_1 = new Resume(UUID_1, "Name1");
        RESUME_2 = new Resume(UUID_2, "Name2");
        RESUME_3 = new Resume(UUID_3, "Name3");
        RESUME_4 = new Resume(UUID_4, "Name4");

        RESUME_1.setContacts(ContactType.PHONE, new Link("12345"));
        RESUME_2.setContacts(ContactType.SKYPE, new Link("54321"));
        RESUME_3.setContacts(ContactType.EMAIL, new Link("abcd@yandex.ru"));
        RESUME_4.setContacts(ContactType.LINKEDIN, new Link("LINKEDIN_URL"));

        List<String> achievments = new ArrayList<>();
        List<String> qualifications = new ArrayList<>();

        achievments.add("Achievment1");
        qualifications.add("Qualification1");

        RESUME_1.setSections(SectionType.ACHIEVEMENT, new ListSection(achievments));
        RESUME_2.setSections(SectionType.QUALIFICATIONS, new ListSection(qualifications));
        RESUME_3.setSections(SectionType.ACHIEVEMENT, new ListSection(achievments));
        RESUME_4.setSections(SectionType.QUALIFICATIONS, new ListSection(qualifications));

        List<Organization> jobOrganizations = new ArrayList<>();
        List<Organization> eduOrganizations = new ArrayList<>();

        Organization organization1 = new Organization(new Link("NAME_1"), new ArrayList<>());
        organization1.addPeriod(new Organization.Period(
                LocalDate.now(),
                LocalDate.now(),
                "POSITION",
                "DUTIES"));
        jobOrganizations.add(organization1);

        Organization organization2 = new Organization(new Link("NAME_2"), new ArrayList<>());
        organization1.addPeriod(new Organization.Period(
                LocalDate.now(),
                LocalDate.now(),
                "POSITION",
                "DUTIES"));
        eduOrganizations.add(organization2);

        RESUME_1.setSections(SectionType.EXPERIENCE, new OrganizationSection(jobOrganizations));
        RESUME_2.setSections(SectionType.EDUCATION, new OrganizationSection(eduOrganizations));
        RESUME_3.setSections(SectionType.EXPERIENCE, new OrganizationSection(jobOrganizations));
        RESUME_4.setSections(SectionType.EDUCATION, new OrganizationSection(eduOrganizations));
    }

    AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() {
        storage.clear();
        storage.save(RESUME_1);
        storage.save(RESUME_2);
        storage.save(RESUME_3);
    }

    @Test
    public void size() {
        assertSize(3);
    }

    @Test
    public void clear() {
        storage.clear();
        assertSize(0);
    }

    @Test
    public void update() {
        Resume resume3 = new Resume("uuid3", "new_name3");
        storage.update(resume3);
        assertEquals(resume3, storage.get(RESUME_3.getUuid()));
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        storage.update(RESUME_4);
    }

    @Test
    public void save() {
        storage.save(RESUME_4);
        assertSize(4);
        assertGet(RESUME_4);
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() {
        storage.save(RESUME_3);
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() {
        storage.delete(RESUME_3.getUuid());
        assertGet(RESUME_3);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete(RESUME_4.getUuid());
    }

    @Test
    public void get() {
        assertGet(RESUME_3);
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get(RESUME_4.getUuid());
    }

    @Test
    public void getAllSorted() {
        List<Resume> list = new ArrayList<>();
        List<Resume> sortedStorage = storage.getSortedStorage();
        list.add(RESUME_1);
        list.add(RESUME_2);
        list.add(RESUME_3);
        assertEquals(list.size(), sortedStorage.size());
        assertEquals(list, sortedStorage);
    }

    private void assertSize(int size) {
        assertEquals(size, storage.size());
    }

    private void assertGet(Resume resume) {
        assertEquals(resume, storage.get(resume.getUuid()));
    }
}