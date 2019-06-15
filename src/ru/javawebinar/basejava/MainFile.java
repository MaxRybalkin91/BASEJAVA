package ru.javawebinar.basejava;

import java.io.File;

public class MainFile {
    public static void main(String[] args) {
        File dir = new File("./src/ru/javawebinar/basejava");
        printDirectoryContent(dir.getPath());
    }

    private static void printDirectoryContent(String directory) {
        File[] files = new File(directory).listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    System.out.println("DIR " + file.getName() + " :");
                    printDirectoryContent(file.getPath());
                } else {
                    System.out.println("   " + file.getName());
                }
            }
        }
    }
}