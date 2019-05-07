package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static ru.javawebinar.basejava.storage.AbstractArrayStorage.STORAGE_LIMIT;

public abstract class AbstractArrayStorageTest {
    private Storage storage;
    private static final Resume resume1 = new Resume("uuid1");
    private static final Resume resume2 = new Resume("uuid2");
    private static final Resume resume3 = new Resume("uuid3");
    private static final Resume resume4 = new Resume("uuid4");

    protected AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() {
        storage.clear();
        storage.save(resume1);
        storage.save(resume2);
        storage.save(resume3);
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
        Resume resume3 = new Resume("uuid3");
        storage.update(resume3);
        assertGet(resume3);
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        storage.update(resume4);
    }

    @Test
    public void save() {
        storage.save(resume4);
        assertSize(4);
        assertGet(resume4);
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() {
        storage.save(resume3);
    }

    @Test(expected = StorageException.class)
    public void saveOverflow() {
        storage.clear();
        try {
            for (int i = 0; i != STORAGE_LIMIT; i++) {
                storage.save(new Resume());
            }
        } catch (StorageException e) {
            Assert.fail("Storage overflow!");
        }
        storage.save(new Resume());
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() {
        storage.delete(resume3.getUuid());
        assertSize(2);
        assertGet(resume3);
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete(resume4.getUuid());
    }

    @Test
    public void get() {
        assertGet(resume3);
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get(resume4.getUuid());
    }

    @Test
    public void getAll() {
        Resume[] array = new Resume[]{resume1, resume2, resume3,};
        assertEquals(3, array.length);
        assertTrue(resume1 == array[0]);
        assertTrue(resume2 == array[1]);
        assertTrue(resume3 == array[2]);
    }

    private void assertSize(int size) {
        assertEquals(size, storage.size());
    }

    private void assertGet(Resume resume) {
        assertEquals(resume, storage.get(resume.getUuid()));
    }
}