package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractStorage implements Storage {

    protected abstract void saveToStorage(Resume r, Object key);

    protected abstract void updateInStorage(Resume r, Object key);

    protected abstract void deleteFromStorage(Object key);

    protected abstract Resume getFromStorage(Object key);

    protected abstract Object getSearchKey(String uuid);

    public void save(Resume r) {
        Object key = getSearchKey(r.getUuid());
        if(key != null)
            throw new ExistStorageException(r.getUuid());
        saveToStorage(r, key);
    }

    public void update(Resume r) {
        Object key = getSearchKey(r.getUuid());
        if(key == null)
            throw new NotExistStorageException(r.getUuid());
        updateInStorage(r, key);
    }

    public void delete(String uuid) {
        Object key = getSearchKey(uuid);
        if(key == null)
            throw new NotExistStorageException(uuid);
        deleteFromStorage(key);
    }

    public Resume get(String uuid) {
        Object key = getSearchKey(uuid);
        if(key == null)
            throw new NotExistStorageException(uuid);
        return getFromStorage(key);
    }
}