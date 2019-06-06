package ru.javawebinar.basejava.storage;

import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.ResumeTestData;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public abstract class AbstractStorageTest {
    Storage storage;
    private static final Resume RESUME_1 = new Resume("uuid1", "name1");
    private static final Resume RESUME_2 = new Resume("uuid2", "name2");
    private static final Resume RESUME_3 = new Resume("uuid3", "name3");
    private static final Resume RESUME_4 = new Resume("uuid4", "name4");

    AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    static {
        ResumeTestData.setResume("uuid", "Григорий Кислин");
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
        List<Resume> sortedStorage = storage.getAllSorted();
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