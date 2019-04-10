import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private final Resume[] storage = new Resume[10000];
    private int size = 0;

    void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    void save(Resume resume) {
        if(size != storage.length) {
            if(isResumePresent(resume.uuid)) {
                System.out.println("Resume " + resume.uuid + " is already written");
            } else {
                storage[size] = resume;
                size++;
            }
        } else {
            System.out.println("Storage is full!");
        }
    }

    void update(Resume resume) {
        if(isResumePresent(resume.uuid)) {
            storage[size] = resume;
        } else {
            System.out.println("This UUID is empty! You need to set it!");
        }
    }

    Resume get(String uuid) {
        if(isResumePresent(uuid)) {
            for(int i = 0; i < size; i++) {
                return storage[i];
            }
        } else {
            System.out.println("Resume not founded");
        }
        return null;
    }

    void delete(String uuid) {
        for (int i = 0; i < size; i++) {
            if (isResumePresent(uuid)) {
                storage[i] = storage[size - 1];
                storage[size - 1] = null;
                size--;
            } else {
                System.out.println("You loose");
                break;
            }
        }
    }

    boolean isResumePresent(String uuid) {
        for(int i = 0; i < size; i++) {
            if(storage[i].uuid.equals(uuid)) {
                return true;
            }
        }
        return false;
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
