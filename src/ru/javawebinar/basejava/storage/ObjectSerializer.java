package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface ObjectSerializer {
    void writeToStorage(Resume resume, OutputStream outputFile) throws IOException;

    Resume readFromStorage(InputStream inputFile) throws IOException;
}
