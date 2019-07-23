package ru.javawebinar.basejava;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class MainStream {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int answer;

        System.out.println("Choose the programm:");
        do {
            System.out.println("1 - array, 2 - list");
            answer = scanner.nextInt();
        } while (answer != 1 && answer != 2);

        if (answer == 1) {
            deleteDuplicatesFromArray(scanner);
        } else {
            deleteNumbers(scanner);
        }
    }

    private static void deleteDuplicatesFromArray(Scanner scanner) {
        int[] array = new int[scanner.nextInt()];
        int number;

        System.out.println("Enter the desired array size:");
        for (int i = 0; i < array.length; i++) {
            do {
                System.out.println("Enter " + (i + 1) + " number (required in the range of 1 to 9):");
                number = scanner.nextInt();
            } while (number < 1 || number > 9);
            array[i] = number;
        }
        System.out.println("There is number from the unique parts of your array:" + "\n" + minValue(array));
    }

    private static void deleteNumbers(Scanner scanner) {
        List<Integer> integers = new ArrayList<>();

        System.out.println("Enter numbers for the list (enter any letter to stop):");
        while (scanner.hasNextInt()) {
            integers.add(scanner.nextInt());
        }

        for (int num : oddOrEven(integers)) {
            System.out.print(num + " ");
        }
    }

    private static int minValue(int[] array) {
        return IntStream.of(array)
                .distinct()
                .reduce(0, (a, b) -> 10 * a + b);
    }

    private static List<Integer> oddOrEven(List<Integer> integers) {
        return integers.stream()
                .reduce(0, Integer::sum) == 0 ?
                doFilter(integers, p -> p % 2 == 0) : doFilter(integers, p -> p % 2 != 0);
    }

    private static List<Integer> doFilter(List<Integer> integers, Predicate<Integer> predicate) {
        return integers.stream().filter(predicate).collect(Collectors.toList());
    }
}
