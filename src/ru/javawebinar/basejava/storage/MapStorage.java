package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.HashMap;
import java.util.Map;

public class MapStorage extends AbstractStorage {
    HashMap<String, Resume> storage = new HashMap<>();

    @Override
    protected void saveToStorage(Resume resume, Object key) {
        storage.put(key.toString(), resume);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    protected void updateInStorage(Resume resume, Object key) {
        storage.replace(key.toString(), resume);
    }

    @Override
    protected void deleteFromStorage(Object key) {
        storage.remove(key.toString());
    }

    @Override
    public Resume[] getAll() {
        return (Resume[]) storage.entrySet().toArray();
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
        String key = "";
        Resume resume = new Resume(uuid);
        for (Map.Entry<String, Resume> pair : storage.entrySet()) {
            if (pair.getValue().equals(resume))
                key = pair.getValue().toString();
        }
        return key;
    }

    @Override
    protected boolean isExist(String uuid) {
        return !getSearchKey(uuid).equals("");
    }
}
