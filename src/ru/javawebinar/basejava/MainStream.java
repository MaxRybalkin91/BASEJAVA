package ru.javawebinar.basejava;

import java.util.Scanner;
import java.util.stream.IntStream;


public class MainStream {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int number;

        System.out.println("Enter the desired array size:");
        int[] array = new int[scanner.nextInt()];
        for (int i = 0; i < array.length; i++) {
            do {
                System.out.println("Enter " + (i + 1) + " number (required in the range of 1 to 9):");
                number = scanner.nextInt();
            } while (number < 1 || number > 9);
            array[i] = number;
        }
        System.out.print("There is number from the unique parts of your array:" + "\n" + minValue(array));
    }

    private static int minValue(int[] array) {
        return IntStream.of(array).distinct().reduce(0, (a, b) -> 10 * a + b);
    }
}
