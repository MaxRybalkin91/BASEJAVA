package ru.javawebinar.basejava;

public class MainDeadLock {
    private static final Object OBJECT_1 = new Object();
    private static final Object OBJECT_2 = new Object();

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> doDeadLock(OBJECT_1, OBJECT_2));
        Thread thread2 = new Thread(() -> doDeadLock(OBJECT_2, OBJECT_1));

        thread1.start();
        thread2.start();
    }

    private static void doDeadLock(Object obj1, Object obj2) {
        synchronized (obj1) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("Thread 1 has been caught. Waiting for thread 2... ");
            }

            synchronized (obj2) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println("Thread 1 & Thread 2 successfully locked");
                }
            }
        }
    }
}