import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private final Resume[] storage = new Resume[10000];
    private int size = 0;
    private int index;

    void clear() {
        Arrays.fill(storage, 0, getSize(), null);
        size = 0;
    }

    void save(Resume resume) {
        if(size != storage.length) {
            index = checkUuid(resume.getUuid());
            if(index != -1) {
                System.out.println("Resume " + resume.getUuid() + " is already written");
            } else {
                storage[size] = resume;
                size++;
            }
        } else {
            System.out.println("Storage is full!");
        }
    }

    void update(Resume resume) {
        index = checkUuid(resume.getUuid());
        if(index != -1) {
            storage[index] = resume;
        } else {
            System.out.println("This UUID is empty! You need to set it!");
        }
    }

    Resume get(String uuid) {
        index = checkUuid(uuid);
        if (index != -1) {
            for (int i = 0; i < size; i++) {
                return storage[index];
            }
        }
        System.out.println("Resume not founded");
        return null;
    }

    void delete(String uuid) {
        index = checkUuid(uuid);
        if (index != -1) {
            storage[index] = storage[size - 1];
            storage[size - 1] = null;
            size--;
        } else {
            System.out.println("Can't find this UUID");
        }
    }

    private int checkUuid(String uuid) {
        for(int i = 0; i < size; i++) {
            if(storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        return Arrays.copyOf(storage, getSize());
    }

    int getSize() {
        return size;
    }
}
