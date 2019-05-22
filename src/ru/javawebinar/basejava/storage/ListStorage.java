package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;

import static ru.javawebinar.basejava.storage.SortedArrayStorage.RESUME_COMPARATOR;

public class ListStorage extends AbstractStorage {
    protected List<Resume> storage = new ArrayList<>();

    @Override
    protected void saveToStorage(Resume resume, Object key) {
        storage.add(resume);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    protected void updateInStorage(Resume resume, Object index) {
        storage.set((Integer) index, resume);
    }

    @Override
    protected void deleteFromStorage(Object index) {
        storage.remove(((Integer) index).intValue());
    }

    @Override
    public List<Resume> getAllSorted() {
        storage.sort(RESUME_COMPARATOR);
        return storage;
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    protected Resume getFromStorage(Object index) {
        return storage.get((Integer) index);
    }

    @Override
    protected Integer getSearchKey(String uuid) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getUuid().equals(uuid))
                return i;
        }
        return null;
    }

    @Override
    protected boolean isExist(String uuid) {
        return getSearchKey(uuid) != null;
    }
}