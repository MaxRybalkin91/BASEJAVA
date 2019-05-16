package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MapStorage extends AbstractStorage {
    Map<String, Resume> storage = new HashMap<>();

    @Override
    protected void saveToStorage(Resume resume, Object key) {
        storage.put(resume.getUuid(), resume);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    protected void updateInStorage(Resume resume, Object key) {
        storage.replace(resume.getUuid(), resume);
    }

    @Override
    protected void deleteFromStorage(Object key) {
        storage.remove(key.toString());
    }

    @Override
    public Resume[] getAll() {
        Resume[] sortedStorage = storage.values().toArray(new Resume[0]);
        Arrays.sort(sortedStorage);
        return sortedStorage;
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    protected Resume getFromStorage(Object key) {
        return storage.get(key.toString());
    }

    @Override
    protected String getSearchKey(String uuid) {
        return uuid;
    }

    @Override
    protected boolean isExist(String uuid) {
        for (Map.Entry<String, Resume> pair : storage.entrySet()) {
            if (pair.getKey().equals(uuid))
                return true;
        }
        return false;
    }
}
