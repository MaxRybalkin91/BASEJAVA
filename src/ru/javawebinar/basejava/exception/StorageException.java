package ru.javawebinar.basejava.exception;

public class StorageException extends RuntimeException {
    private final String uuid;

    public StorageException(String message, Object uuid) {
        super(message);
        this.uuid = (String)uuid;
    }

    public StorageException(Exception e) {
        this(e.getMessage(), e);
    }

    public StorageException(String message, Exception e) {
        this(message, null, e);
    }

    public StorageException(String message, String uuid, Exception e) {
        super(message, e);
        this.uuid = uuid;
    }
}
