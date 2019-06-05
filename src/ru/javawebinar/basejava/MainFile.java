package ru.javawebinar.basejava;

import java.io.File;

public class MainFile {
    public static void main(String[] args) {
        File dir = new File("./src/ru/javawebinar/basejava");
        printDirectoryContent(dir.getAbsolutePath());
    }

    private static void printDirectoryContent(String dir) {
        File[] files = new File(dir).listFiles();
        for (File file : files) {
            if (file.isFile()) {
                System.out.println(file.getName());
            }
            if (file.isDirectory()) {
                dir = file.getAbsolutePath();
                printDirectoryContent(dir);
            }
        }
    }
}