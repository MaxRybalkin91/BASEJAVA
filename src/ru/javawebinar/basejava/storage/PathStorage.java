package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.serializer.StreamSerializer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PathStorage extends AbstractStorage<Path> {
    private Path directory;
    private StreamSerializer serializer;

    PathStorage(String dir, StreamSerializer serializer) {
        directory = Paths.get(dir);
        this.serializer = serializer;
        Objects.requireNonNull(directory, "directory must not be null");
        if (!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(dir + " is not directory or is not writable");
        }
    }

    @Override
    protected void saveToStorage(Resume resume, Path path) {
        try {
            Files.createFile(path);
        } catch (IOException e) {
            throw new StorageException("Path creation Error", null);
        }
        updateInStorage(resume, path);
    }

    @Override
    protected void updateInStorage(Resume resume, Path path) {
        try {
            serializer.writeToStorage(resume, new BufferedOutputStream(Files.newOutputStream(path)));
        } catch (IOException e) {
            throw new StorageException("IO error", null);
        }
    }

    @Override
    public void clear() {
        try {
            Files.list(directory).forEach(this::deleteFromStorage);
        } catch (IOException e) {
            throw new StorageException("Path delete Error", null);
        }
    }

    @Override
    protected void deleteFromStorage(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new StorageException("File delete Error", null);
        }
    }

    @Override
    protected List<Resume> getAllStorage() {
        try {
            return Files.list(directory).map(this::getFromStorage).collect(Collectors.toList());
        } catch (IOException e) {
            throw new StorageException("Storage reading Error", null);
        }
    }

    @Override
    protected Resume getFromStorage(Path path) {
        try {
            return serializer.readFromStorage(new BufferedInputStream(Files.newInputStream(path)));
        } catch (IOException e) {
            throw new StorageException("Can not read the resume", path.toString(), e);
        }
    }

    @Override
    public int size() {
        try {
            return (int) Files.list(directory).count();
        } catch (IOException e) {
            throw new StorageException("No files in directory", null);
        }
    }

    @Override
    protected Path getSearchKey(String uuid) {
        return directory.resolve(uuid);
    }

    @Override
    protected boolean isExist(Path path) {
        return Files.exists(path);
    }
}