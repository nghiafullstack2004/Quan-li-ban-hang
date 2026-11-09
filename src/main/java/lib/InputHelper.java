package lib;

import java.util.Scanner;

public class InputHelper {
    private static final Scanner input = new Scanner(System.in);

    public static int inputInt() {
        do {
            try {
                return Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number, please try again!");
            }
        } while (true);
    }

    public static int inputInt(int min, int max) {
        do {
            int val = inputInt();
            if (val >= min && val <= max) {
                return val;
            }
            System.out.println("Please enter a number between " + min + " and " + max + "!");
        } while (true);
    }

    public static double inputDouble() {
        do {
            try {
                return Double.parseDouble(input.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid decimal number, please try again!");
            }
        } while (true);
    }

    public static long inputLong() {
        do {
            try {
                return Long.parseLong(input.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid long number, please try again!");
            }
        } while (true);
    }

    public static String inputString() {
        return input.nextLine().trim();
    }

    public static String inputNonEmptyString(String fieldName) {
        do {
            String val = input.nextLine().trim();
            if (!val.isEmpty()) {
                return val;
            }
            System.out.println(fieldName + " cannot be empty. Please try again!");
        } while (true);
    }
}
