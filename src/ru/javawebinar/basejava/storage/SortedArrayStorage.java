package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected void deleteSortedElement(int index) {
        int length = size - index - 1;
        System.arraycopy(storage, index + 1, storage, index, length);
    }

    @Override
    protected void saveSortedElement(Resume resume, int index) {
        int destPos = -index - 1;
        System.arraycopy(storage, destPos, storage, destPos + 1, size - destPos);
        storage[destPos] = resume;
    }

    protected void updateSortedElement(Resume resume, int index) {
        storage[index] = resume;
    }

    @Override
    protected int getIndex(String uuid) {
        Resume key = new Resume();
        key.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, size, key);
    }
}