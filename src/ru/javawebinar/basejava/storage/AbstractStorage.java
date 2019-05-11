package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractStorage implements Storage {

    protected abstract void saveToStorage(Resume r);

    protected abstract void updateInStorage(Resume r);

    protected abstract Resume getFromStorage(String uuid);

    protected abstract void deleteFromStorage(String uuid);

    public void save(Resume r) {
        if(get(r.getUuid()) != null)
            throw new ExistStorageException(r.getUuid());
        saveToStorage(r);
    }

    public void update(Resume r) {
        if(get(r.getUuid()) == null)
            throw new NotExistStorageException(r.getUuid());
        updateInStorage(r);
    }

    public void delete(String uuid) {
        if(get(uuid) == null)
            throw new NotExistStorageException(uuid);
        deleteFromStorage(uuid);
    }

    public Resume get(String uuid) {
        if(getFromStorage(uuid) == null)
            throw new NotExistStorageException(uuid);
        return getFromStorage(uuid);
    }
}