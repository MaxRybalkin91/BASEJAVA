package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.File;
import java.io.IOException;
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
    protected void saveToStorage(Resume resume, File searchFile) {
        try {
            searchFile.createNewFile();
            writeToStorage(resume, searchFile);
        } catch (IOException e) {
            throw new StorageException("IO error", searchFile.getName(), e);
        }
    }

    protected abstract void writeToStorage(Resume resume, File searchFile) throws IOException;

    @Override
    public void clear() {
        for (File file : directory.listFiles()) {
            if (file.isFile()) {
                file.delete();
            }
        }
    }

    @Override
    protected void updateInStorage(Resume resume, File searchFile) {
        try {
            writeToStorage(resume, searchFile);
        } catch (IOException e) {
            throw new StorageException("IO error", searchFile.getName(), e);
        }
    }

    @Override
    protected void deleteFromStorage(File searchFile) {
        searchFile.delete();
    }

    @Override
    protected List<Resume> getAllStorage() {
        List<Resume> resumes = new ArrayList<>();
        for (File file : directory.listFiles()) {
            resumes.add(getFromStorage(file));
        }
        return resumes;
    }

    @Override
    protected Resume getFromStorage(File searchFile) {
        try {
            return readFromStorage(searchFile);
        } catch (IOException e) {
            throw new StorageException("can not read the resume", searchFile.getName(), e);
        }
    }

    protected abstract Resume readFromStorage(File searchFile) throws IOException;

    @Override
    public int size() {
        return directory.listFiles().length;
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected boolean isExist(File searchFile) {
        return searchFile.exists();
    }
}