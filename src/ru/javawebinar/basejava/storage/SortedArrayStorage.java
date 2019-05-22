package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;
import java.util.Comparator;

public class SortedArrayStorage extends AbstractArrayStorage {

    static final Comparator<Resume> RESUME_COMPARATOR = Comparator.comparing(Resume::getUuid);

    @Override
    protected void saveElement(Resume resume, int index) {
        int srcPos = -index - 1;
        System.arraycopy(storage, srcPos, storage, srcPos + 1, size - srcPos);
        storage[srcPos] = resume;
    }

    @Override
    protected void deleteElement(int index) {
        int length = size - index - 1;
        System.arraycopy(storage, index + 1, storage, index, length);
    }

    @Override
    protected Integer getSearchKey(String uuid) {
        Resume key = new Resume(uuid);
        return Arrays.binarySearch(storage, 0, size, key, RESUME_COMPARATOR);
    }
}