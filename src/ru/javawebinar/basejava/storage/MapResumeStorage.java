package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.javawebinar.basejava.storage.SortedArrayStorage.RESUME_COMPARATOR;

public class MapResumeStorage extends AbstractStorage {
    Map<Resume, Resume> storage = new HashMap<>();

    @Override
    protected void saveToStorage(Resume resume, Object key) {
        storage.put(resume, resume);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    protected void updateInStorage(Resume resume, Object key) {
        storage.replace(resume, resume);
    }

    @Override
    protected void deleteFromStorage(Object key) {
        storage.remove(key);
    }

    @Override
    public List<Resume> getAllSorted() {
        List<Resume> sortedStorage = new ArrayList<>(storage.values());
        sortedStorage.sort(RESUME_COMPARATOR);
        return sortedStorage;
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    protected Resume getFromStorage(Object key) {
        return storage.get(key);
    }

    @Override
    protected Resume getSearchKey(String uuid) {
        return new Resume(uuid);
    }

    @Override
    protected boolean isExist(String uuid) {
        return storage.containsKey(new Resume(uuid));
    }
}