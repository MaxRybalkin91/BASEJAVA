package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractArrayStorage extends AbstractStorage<Integer> {
    static final int STORAGE_LIMIT = 10000;

    protected Resume[] storage = new Resume[STORAGE_LIMIT];
    protected int size = 0;

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    @Override
    public boolean isExist(Integer searchKey) {
        return searchKey >= 0;
    }

    @Override
    public void updateInStorage(Resume resume, Integer index) {
        storage[index] = resume;
    }

    @Override
    public List<Resume> getAllStorage() {
        return Arrays.asList(Arrays.copyOf(storage, size));
    }

    @Override
    public void saveToStorage(Resume resume, Integer index) {
        if(size == STORAGE_LIMIT)
            throw new StorageException("Storage overflow!", resume.getUuid());
        saveElement(resume, index);
        size++;
    }

    @Override
    public void deleteFromStorage(Integer index) {
        deleteElement(index);
        storage[size - 1] = null;
        size--;
    }

    @Override
    protected Resume getFromStorage(Integer index) {
        return storage[index];
    }

    protected abstract void saveElement(Resume resume, int index);

    protected abstract void deleteElement(int index);
}