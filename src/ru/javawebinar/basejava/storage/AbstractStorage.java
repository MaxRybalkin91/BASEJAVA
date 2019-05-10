package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractStorage implements Storage {

    public abstract void save(Resume r);

    public abstract void clear();

    public abstract void update(Resume r);

    public abstract Resume get(String uuid);

    public abstract void delete(String uuid);

    public abstract Resume[] getAll();

    public abstract int size();
}