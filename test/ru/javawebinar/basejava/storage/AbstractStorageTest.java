package ru.javawebinar.basejava.storage;

import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.*;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public abstract class AbstractStorageTest {
    static final File STORAGE_DIR = Config.get().getStorageDir();

    protected Storage storage;

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

        setContacts(RESUME_1, RESUME_2, RESUME_3, RESUME_4);
        setSections(RESUME_1, RESUME_2, RESUME_3, RESUME_4);
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

    private static void setContacts(Resume... resumes) {
        for (Resume resume : resumes) {
            resume.setContacts(ContactType.PHONE, "+1-234-567-89-0");
            resume.setContacts(ContactType.SKYPE, "Microsoft");
            resume.setContacts(ContactType.EMAIL, "abcd@yandex.ru");
            resume.setContacts(ContactType.LINKEDIN, "http://www.linkedin.com");
        }
    }

    private static void setSections(Resume... resumes) {
        for (Resume resume : resumes) {
            resume.setSections(SectionType.OBJECTIVE, new TextSection("Objectives"));
            resume.setSections(SectionType.PERSONAL, new TextSection("Personal"));

            resume.setSections(SectionType.ACHIEVEMENT, new ListSection("Achievement1", "Achievement2", "Achievement3"));
            resume.setSections(SectionType.QUALIFICATION, new ListSection("Qualification1", "Qualification2", "Qualification3"));

            resume.setSections(SectionType.EXPERIENCE, new OrganizationSection(
                    new Organization(
                            new Link("COMPANY_1", "URL_1"),
                            new Organization.Stage(
                                    LocalDate.now(),
                                    LocalDate.now(),
                                    "POSITION_1",
                                    "DUTIES_1"),
                            new Organization.Stage(
                                    LocalDate.now(),
                                    LocalDate.now(),
                                    "POSITION_2",
                                    "DUTIES_2"))));

            resume.setSections(SectionType.EDUCATION, new OrganizationSection(
                    new Organization(
                            new Link("UNIVERSITY_1", "URL_1"),
                            new Organization.Stage(
                                    LocalDate.now(),
                                    LocalDate.now(),
                                    "STUDENT_1"))));
        }
    }
}