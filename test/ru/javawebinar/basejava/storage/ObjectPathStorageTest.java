package ru.javawebinar.basejava.storage;

public class ObjectPathStorageTest extends AbstractStorageTest {

    public ObjectPathStorageTest() {
        super(new ObjectPathStorage(STORAGE_DIR.toPath()));
    }
}
