package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Comparator;
import java.util.List;

public abstract class AbstractStorage implements Storage {

    protected abstract void saveToStorage(Resume resume, Object searchKey);

    protected abstract void updateInStorage(Resume resume, Object searchKey);

    protected abstract void deleteFromStorage(Object searchKey);

    protected abstract Resume getFromStorage(Object searchKey);

    protected abstract Object getSearchKey(String uuid);

    protected abstract boolean isExist(Object searchKey);

    protected abstract List<Resume> getAllStorage();

    private final Comparator<Resume> RESUME_COMPARATOR = Comparator.comparing(Resume::getFullName)
            .thenComparing(Resume::getUuid);

    @Override
    public void save(Resume resume) {
        saveToStorage(resume, getNotExistedSearchKey(resume.getUuid()));
    }

    @Override
    public void update(Resume resume) {
        updateInStorage(resume, getExistedSearchKey(resume.getUuid()));
    }

    @Override
    public void delete(String uuid) {
        deleteFromStorage(getExistedSearchKey(uuid));
    }

    @Override
    public Resume get(String uuid) {
        return getFromStorage(getExistedSearchKey(uuid));
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> sortedStorage = getAllStorage();
        sortedStorage.sort(RESUME_COMPARATOR);
        return sortedStorage;
    }

    private Object getExistedSearchKey(String uuid) {
        Object searchKey = getSearchKey(uuid);
        if (!isExist(searchKey)) {
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }

    private Object getNotExistedSearchKey(String uuid) {
        Object searchKey = getSearchKey(uuid);
        if (isExist(searchKey)) {
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }
}