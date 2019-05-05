package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import static ru.javawebinar.basejava.storage.AbstractArrayStorage.STORAGE_LIMIT;

import org.junit.*;

public abstract class AbstractArrayStorageTest extends Assert {
    private Storage storage;

    public AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";

    @Before
    public void setUp() throws Exception {
        storage.clear();
        storage.save(new Resume(UUID_1));
        storage.save(new Resume(UUID_2));
        storage.save(new Resume(UUID_3));
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
        Resume resume = new Resume("test_uuid");
        storage.update(resume);
    }

    @Test
    public void getAll() {
        Resume[] testStorage = storage.getAll();
        assertEquals(3, testStorage.length);
    }

    @Test
    public void save() {
        String UUID_4 = "uuid4";
        storage.save(new Resume(UUID_4));
        assertEquals(4, storage.size());
    }

    @Test
    public void delete() {
        storage.delete(UUID_3);
        assertEquals(2, storage.size());
    }

    @Test
    public void get() {
        storage.get("test_uuid");
    }

    @Test(expected = StorageException.class)
    public void arrayOverflow() {
        storage.clear();
        try {
            for (int i = 0; i != STORAGE_LIMIT; i++) {
                storage.save(new Resume());
            }
        } catch (StorageException e) {
            Assert.fail();
        }
        storage.save(new Resume());
    }
}