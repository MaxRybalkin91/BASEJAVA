package ru.javawebinar.basejava;

public class MainDeadLock {
    private static final Object OBJECT_1 = new Object();
    private static final Object OBJECT_2 = new Object();

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            synchronized (OBJECT_1) {
                System.out.println("Waiting for OBJECT2 will be unlocked");
                synchronized (OBJECT_2) {
                    System.out.println("OBJECT2 is unlocked");
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            synchronized (OBJECT_2) {
                System.out.println("Waiting for OBJECT1 will be unlocked");
                synchronized (OBJECT_1) {
                    System.out.println("OBJECT1 is unlocked");
                }
            }
        });

        thread1.start();
        thread2.start();
    }
}