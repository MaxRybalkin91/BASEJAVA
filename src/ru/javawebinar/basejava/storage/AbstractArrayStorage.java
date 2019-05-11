package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage extends AbstractStorage {
    protected static final int STORAGE_LIMIT = 10000;

    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    public int size() {
        return size;
    }

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    public void updateInStorage(Resume resume) {
        int index = getIndex(resume.getUuid());
        storage[index] = resume;
    }

    public Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    @Override
    public void saveToStorage(Resume resume) {
        int index = getIndex(resume.getUuid());
        saveElement(resume, index);
        size++;
    }

    @Override
    public void deleteFromStorage(String uuid) {
        int index = getIndex(uuid);
        deleteElement(index);
        storage[size - 1] = null;
        size--;
    }

    @Override
    public Resume getFromStorage(String uuid) {
        int index = getIndex(uuid);
        if (index >= 0)
            return storage[index];
        return null;
    }

    protected abstract void saveElement(Resume resume, int index);

    protected abstract void deleteElement(int index);

    protected abstract int getIndex(String uuid);
}