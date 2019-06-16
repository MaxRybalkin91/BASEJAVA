package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.*;

public class ObjectStorage implements ObjectSerializer {

    @Override
    public void writeToStorage(Resume resume, OutputStream outputFile) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(outputFile)) {
            oos.writeObject(resume);
        }
    }

    @Override
    public Resume readFromStorage(InputStream inputFile) throws IOException {
        try (ObjectInputStream oos = new ObjectInputStream(inputFile)) {
            return (Resume) oos.readObject();
        } catch (ClassNotFoundException e) {
            throw new StorageException("Error read resume", null, e);
        }
    }
}
