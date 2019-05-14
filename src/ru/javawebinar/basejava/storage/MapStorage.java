package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.HashMap;
import java.util.Map;

public class MapStorage extends AbstractStorage {
    HashMap<Object, Resume> storage = new HashMap<>();

    @Override
    protected void saveToStorage(Resume resume, Object key) {
        storage.put(key, resume);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    protected void updateInStorage(Resume resume, Object key) {
        storage.replace(key, resume);
    }

    @Override
    protected void deleteFromStorage(Object key) {
        storage.remove(key);
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
        return storage.get(key);
    }

    @Override
    protected Object getSearchKey(String uuid) {
        Resume key = null;
        Resume resume = new Resume(uuid);
        for (Map.Entry<Object, Resume> pair : storage.entrySet()) {
            if (pair.getValue().equals(resume))
                key = pair.getValue();
        }
        return key;
    }
}
