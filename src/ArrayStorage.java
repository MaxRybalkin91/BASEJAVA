import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private final Resume[] storage = new Resume[10000];
    private int size = 0;
    private boolean isNeedSave = true;

    void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    void save(Resume r) {
        if(size == storage.length) {
            isNeedSave = false;
            System.out.println("Storage is full!");
        }

        for(int i = 0; i < size; i++) {
            if (storage[i].uuid.equals(r.uuid)) {
                isNeedSave = false;
                System.out.println("Resume " + r.uuid + " is already written");
            }
        }

        if(isNeedSave) {
            storage[size] = r;
            size++;
        }
    }

    Resume get(String uuid) {
        System.out.println("Resume " + uuid + ":");
        for(int i = 0; i < size; i++) {
            if(storage[i].uuid.equals(uuid)) {
                return storage[i];
            }
        }
        return null;
    }

    void delete(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].uuid.equals(uuid)) {
                System.out.println("Resume " + storage[i].uuid + " is successfully deleted");
                storage[i] = storage[size - 1];
                storage[size - 1] = null;
                size--;
            } else {
                System.out.println("Resume " + uuid + " is not in this storage");
                break;
            }
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    int size() {
        return size;
    }
}
