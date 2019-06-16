package ru.javawebinar.basejava;

import java.io.File;

public class MainFile {
    public static void main(String[] args) {
        File dir = new File("./src/ru/javawebinar/basejava");
        printDirectoryContent(dir, "");
    }

    private static void printDirectoryContent(File dir, String space) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    System.out.println("DIR " + file.getName() + " :");
                    printDirectoryContent(file, "\t");
                } else {
                    System.out.println(space + file.getName());
                }
            }
        }
    }
}