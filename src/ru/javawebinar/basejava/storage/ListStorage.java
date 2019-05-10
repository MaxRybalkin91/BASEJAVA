package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListStorage extends AbstractStorage {
    protected List<Resume> storage = new ArrayList<>();

    @Override
    public void save(Resume resume) {
        if (getIndex(resume.getUuid()) >= 0)
            throw new ExistStorageException(resume.getUuid());
        else {
            storage.add(resume);
            Collections.sort(storage);
        }
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public void update(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (index >= 0)
            storage.set(index, resume);
        else
            throw new NotExistStorageException(resume.getUuid());
    }

    @Override
    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index == -1)
            throw new NotExistStorageException(uuid);
        return storage.get(index);
    }

    @Override
    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index >= 0)
            storage.remove(index);
        else
            throw new NotExistStorageException(uuid);
    }

    @Override
    public Resume[] getAll() {
        return storage.toArray(new Resume[storage.size()]);
    }

    @Override
    public int size() {
        return storage.size();
    }

    private int getIndex(String uuid) {
        Resume key = new Resume(uuid);
        return Collections.binarySearch(storage, key);
    }
}