package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import static ru.javawebinar.basejava.storage.AbstractArrayStorage.STORAGE_LIMIT;

public abstract class AbstractArrayStorageTest{
    private Storage storage;
    private Resume resume1 = new Resume("uuid1");
    private Resume resume2 = new Resume("uuid2");
    private Resume resume3 = new Resume("uuid3");
    private Resume resume4 = new Resume("uuid4");

    protected AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() throws Exception {
        storage.clear();
        storage.save(resume1);
        storage.save(resume2);
        storage.save(resume3);
    }

    @Test
    public void size() {
        assertEquals(3, storage.size());
    }

    @Test
    public void clear() {
        storage.clear();
        assertEquals(0, storage.size());
    }

    @Test
    public void update() {
        storage.update(resume3);
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        storage.update(resume4);
    }

    @Test
    public void save() {
        storage.save(resume4);
        assertEquals(4, storage.size());
        assertEquals(resume4, storage.get(resume4.getUuid()));
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() {
        storage.save(resume3);
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() {
        storage.delete(resume4.getUuid());
        assertEquals(3, storage.size());
        storage.get(resume4.getUuid());
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete(resume4.getUuid());
    }

    @Test
    public void get() {
        assertEquals(resume3, storage.get("uuid3"));
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get(resume4.getUuid());
    }

    @Test
    public void getAll() {
        Resume[] array = new Resume[] {resume1, resume2, resume3,};
        assertEquals(3, array.length);
        assertEquals(resume1, array[0]);
        assertEquals(resume2, array[1]);
        assertEquals(resume3, array[2]);
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
}