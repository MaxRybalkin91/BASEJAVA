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
    static final File STORAGE_DIR = new File("../basejava/storage");
    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final String UUID_4 = "uuid4";
    private static final String NAME_1 = "name1";
    private static final String NAME_2 = "name2";
    private static final String NAME_3 = "name3";
    private static final String NAME_4 = "name4";
    private static final Resume RESUME_1;
    private static final Resume RESUME_2;
    private static final Resume RESUME_3;
    private static final Resume RESUME_4;

    static {
        RESUME_1 = new Resume(UUID_1, NAME_1);
        RESUME_2 = new Resume(UUID_2, NAME_2);
        RESUME_3 = new Resume(UUID_3, NAME_3);
        RESUME_4 = new Resume(UUID_4, NAME_4);

        RESUME_1.setContacts(ContactType.PHONE, "+1-234-567-89-0");
        RESUME_2.setContacts(ContactType.SKYPE, "Microsoft");
        RESUME_3.setContacts(ContactType.EMAIL, "abcd@yandex.ru");
        RESUME_4.setContacts(ContactType.LINKEDIN, "http://www.linkedin.com");

        RESUME_1.setSections(SectionType.OBJECTIVE, new TextSection("Objectives1"));
        RESUME_2.setSections(SectionType.PERSONAL, new TextSection("Personal2"));
        RESUME_3.setSections(SectionType.OBJECTIVE, new TextSection("Objectives3"));
        RESUME_4.setSections(SectionType.PERSONAL, new TextSection("Personal4"));

        RESUME_1.setSections(SectionType.ACHIEVEMENT, new ListSection("Achievment1"));
        RESUME_2.setSections(SectionType.QUALIFICATION, new ListSection("Qualification2"));
        RESUME_3.setSections(SectionType.ACHIEVEMENT, new ListSection("Achievment3"));
        RESUME_4.setSections(SectionType.QUALIFICATION, new ListSection("Qualification4"));

        RESUME_1.setSections(SectionType.EXPERIENCE, new OrganizationSection(
                new Organization(
                        new Link("COMPANY_1", "URL_1"),
                        new Organization.Stage(
                                LocalDate.now(),
                                LocalDate.now(),
                                "POSITION_1",
                                null))));
        RESUME_2.setSections(SectionType.EDUCATION, new OrganizationSection(
                new Organization(
                        new Link("COMPANY_2", "URL_2"),
                        new Organization.Stage(
                                LocalDate.now(),
                                LocalDate.now(),
                                "POSITION_2"))));
        RESUME_3.setSections(SectionType.EXPERIENCE, new OrganizationSection(
                new Organization(
                        new Link("COMPANY_3", "URL_3"),
                        new Organization.Stage(
                                LocalDate.now(),
                                LocalDate.now(),
                                "POSITION_3",
                                null))));
        RESUME_4.setSections(SectionType.EDUCATION, new OrganizationSection(
                new Organization(
                        new Link("COMPANY_4", "URL_4"),
                        new Organization.Stage(
                                LocalDate.now(),
                                LocalDate.now(),
                                "POSITION_4"))));
    }

    Storage storage;

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
        Resume resume3 = new Resume(UUID_3, "NEW_NAME_3");
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
        assertEquals(RESUME_3, storage.get(UUID_3));
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