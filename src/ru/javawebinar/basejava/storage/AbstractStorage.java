package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractStorage implements Storage {

    protected abstract void saveToStorage(Resume r, int index);

    protected abstract void updateInStorage(Resume r, int index);

    protected abstract void deleteFromStorage(int index);

    protected abstract int getIndex(String uuid);

    public void save(Resume r) {
        int index = getResume(r.getUuid());
        if(index >= 0)
            throw new ExistStorageException(r.getUuid());
        saveToStorage(r, index);
    }

    public void update(Resume r) {
        int index = getResume(r.getUuid());
        if(index == -1)
            throw new NotExistStorageException(r.getUuid());
        updateInStorage(r, index);
    }

    public void delete(String uuid) {
        int index = getResume(uuid);
        if(index == -1)
            throw new NotExistStorageException(uuid);
        deleteFromStorage(index);
    }

    private int getResume(String uuid) {
        return getIndex(uuid);
    }
}