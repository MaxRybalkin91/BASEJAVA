package ru.javawebinar.basejava.exception;

public class NotExistStorageException extends StorageException {
    public NotExistStorageException(String uuid) {
        super("Resume not exist", uuid);
    }
}
