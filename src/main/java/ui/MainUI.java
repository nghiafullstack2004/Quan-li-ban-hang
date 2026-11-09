package ui;

import lib.InputHelper;

public class MainUI {
    public static void showMainMenu() {
        int choice;
        do {
            System.out.println("\n==============================================");
            System.out.println("  QUAN LI BAN HANG   ");
            System.out.println("==============================================");
            System.out.println("1. Category Management");
            System.out.println("2. Product Management");
            System.out.println("3. Customer Management");
            System.out.println("4. Order Management");
            System.out.println("5. Payment Management");
            System.out.println("6. User / Employee Management");
            System.out.println("0. Exit System");
            System.out.println("==============================================");
            System.out.print("Enter your choice: ");
            choice = InputHelper.inputInt();
            switch (choice) {
                case 1:
                    CategoryUI.showMainMenu();
                    break;
                case 2:
                    ProductUI.showMainMenu();
                    break;
                case 3:
                    CustomerUI.showMainMenu();
                    break;
                case 4:
                    OrderUI.showMainMenu();
                    break;
                case 5:
                    PaymentUI.showMainMenu();
                    break;
                case 6:
                    UserUI.showMainMenu();
                    break;
                case 0:
                    System.out.println("Thank you for using the Store Sales Management System. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice, please try again!");
            }
        } while (choice != 0);
    }
}
