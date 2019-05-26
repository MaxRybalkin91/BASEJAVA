package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractArrayStorage extends AbstractStorage {
    static final int STORAGE_LIMIT = 10000;

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
    public boolean isExist(Object searchKey) {
        return (Integer) searchKey >= 0;
    }

    @Override
    public void updateInStorage(Resume resume, Object index) {
        storage[(Integer) index] = resume;
    }

    @Override
    public List<Resume> getAllStorage() {
        return Arrays.asList(Arrays.copyOf(storage, size));
    }

    @Override
    public void saveToStorage(Resume resume, Object index) {
        if(size == STORAGE_LIMIT)
            throw new StorageException("Storage overflow!", resume.getUuid());
        saveElement(resume, (Integer) index);
        size++;
    }

    @Override
    public void deleteFromStorage(Object index) {
        deleteElement((Integer) index);
        storage[size - 1] = null;
        size--;
    }

    @Override
    protected Resume getFromStorage(Object index) {
        return storage[(Integer) index];
    }

    protected abstract void saveElement(Resume resume, int index);

    protected abstract void deleteElement(int index);

    protected abstract Object getSearchKey(String uuid);
}