package ru.javawebinar.basejava.storage.serializer;

import ru.javawebinar.basejava.model.Resume;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface StreamSerializer {
    void writeToStorage(Resume resume, OutputStream outputFile) throws IOException;

    Resume readFromStorage(InputStream inputFile) throws IOException;
}
