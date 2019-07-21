package ru.javawebinar.basejava;

import java.util.Scanner;
import java.util.stream.IntStream;


public class MainStream {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the desired array size:");
        int[] array = new int[scanner.nextInt()];
        for (int i = 0; i < array.length; i++) {
            System.out.println("Enter " + (i + 1) + " number:");
            array[i] = scanner.nextInt();
        }
        System.out.println(minValue(array));
    }

    private static int minValue(int[] array) {
        int[] noDuplicates = IntStream.of(array).distinct().toArray();
        int result = 0;

        for (int i = noDuplicates.length - 1, n = 0; i >= 0; --i, n++) {
            int pos = (int) Math.pow(10, i);
            result += noDuplicates[n] * pos;
        }
        return result;
    }
}
