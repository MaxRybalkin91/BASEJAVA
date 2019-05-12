package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.List;

public class ListStorage extends AbstractStorage {
    protected List<Resume> storage = new ArrayList<>();

    @Override
    protected void saveToStorage(Resume resume, int index) {
        storage.add(resume);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    protected void updateInStorage(Resume resume, int index) {
        storage.set(index, resume);
    }

    @Override
    protected void deleteFromStorage(int index) {
        storage.remove(index);
    }

    @Override
    public Resume[] getAll() {
        return storage.toArray(new Resume[storage.size()]);
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    protected Resume getFromStorage(int index) {
        return(storage.get(index));
    }

    @Override
    protected int getIndex(String uuid) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getUuid().equals(uuid))
                return i;
        }
        return -1;
    }
}