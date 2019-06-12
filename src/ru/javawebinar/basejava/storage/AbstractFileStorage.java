package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFileStorage extends AbstractStorage<File> {
    private File directory;

    protected AbstractFileStorage(File directory) {
        Objects.requireNonNull(directory, "directory must not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + " is not readable/writable");
        }
        this.directory = directory;
    }

    @Override
    protected void saveToStorage(Resume resume, File file) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new StorageException("Couldn't create file", file.getName(), e);
        }
        if (file.isFile()) {
            updateInStorage(resume, file);
        }
    }

    @Override
    protected void updateInStorage(Resume resume, File file) {
        try {
            writeToStorage(resume, new BufferedOutputStream(new FileOutputStream(file)));
        } catch (IOException e) {
            throw new StorageException("IO error", file.getName(), e);
        }
    }

    protected abstract void writeToStorage(Resume resume, OutputStream file) throws IOException;

    @Override
    public void clear() {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    deleteFromStorage(file);
                }
            }
        }
    }

    @Override
    protected void deleteFromStorage(File file) {
        if (!file.delete()) {
            throw new StorageException("File delete error", file.getName());
        }
    }

    @Override
    protected List<Resume> getAllStorage() {
        File[] files = directory.listFiles();
        if (files == null) {
            throw new StorageException("Directory read error", null);
        }
        List<Resume> list = new ArrayList<>();
        for (File file : files) {
            list.add(getFromStorage(file));
        }
        return list;
    }

    @Override
    protected Resume getFromStorage(File file) {
        try {
            return readFromStorage(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException | ClassNotFoundException e) {
            throw new StorageException("Can not read the resume", file.getName(), e);
        }
    }

    protected abstract Resume readFromStorage(InputStream file) throws IOException, ClassNotFoundException;

    @Override
    public int size() {
        File[] list = directory.listFiles();
        if (list == null) {
            throw new StorageException("Directory read error", null);
        }
        return list.length;
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected boolean isExist(File file) {
        return file.exists();
    }
}