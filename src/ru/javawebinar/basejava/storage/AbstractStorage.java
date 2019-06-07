package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Comparator;
import java.util.List;

public abstract class AbstractStorage<SearchKey> implements Storage {

    protected abstract void saveToStorage(Resume resume, SearchKey searchKey);

    protected abstract void updateInStorage(Resume resume, SearchKey searchKey);

    protected abstract void deleteFromStorage(SearchKey searchKey);

    protected abstract Resume getFromStorage(SearchKey searchKey);

    protected abstract SearchKey getSearchKey(String uuid);

    protected abstract boolean isExist(SearchKey searchKey);

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
    public List<Resume> getSortedStorage() {
        List<Resume> sortedStorage = getAllStorage();
        sortedStorage.sort(RESUME_COMPARATOR);
        return sortedStorage;
    }

    private SearchKey getExistedSearchKey(String uuid) {
        SearchKey searchKey = getSearchKey(uuid);
        if (!isExist(searchKey)) {
            throw new NotExistStorageException(uuid);
        }
        return searchKey;
    }

    private SearchKey getNotExistedSearchKey(String uuid) {
        SearchKey searchKey = getSearchKey(uuid);
        if (isExist(searchKey)) {
            throw new ExistStorageException(uuid);
        }
        return searchKey;
    }
}