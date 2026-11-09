package ui;

import entity.User;
import entity.UserRole;
import lib.InputHelper;
import lib.errors.DataNotFoundException;
import service.UserService;

import java.time.Instant;
import java.util.List;

public class UserUI {
    private static final UserService userService = UserService.getInstance();

    public static void showMainMenu() {
        int choice;
        do {
            System.out.println("\n========== USER / EMPLOYEE MANAGEMENT ==========");
            System.out.println("1. Add User");
            System.out.println("2. Edit User");
            System.out.println("3. Delete User");
            System.out.println("4. View All Users");
            System.out.println("5. Search User by Username / Full Name");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");
            choice = InputHelper.inputInt();
            switch (choice) {
                case 1:
                    addUI();
                    break;
                case 2:
                    editUI();
                    break;
                case 3:
                    deleteUI();
                    break;
                case 4:
                    getAllUI();
                    break;
                case 5:
                    searchUI();
                    break;
                case 0:
                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid choice, please try again!");
            }
        } while (choice != 0);
    }

    public static void addUI() {
        System.out.println("\n----- Add User -----");
        long id = Instant.now().getEpochSecond();
        String username;
        while (true) {
            System.out.print("Enter username: ");
            username = InputHelper.inputNonEmptyString("Username");
            if (userService.findByUsername(username) != null) {
                System.out.println("Username already exists! Please choose another.");
            } else {
                break;
            }
        }

        System.out.print("Enter password: ");
        String password = InputHelper.inputNonEmptyString("Password");
        System.out.print("Enter full name: ");
        String fullName = InputHelper.inputNonEmptyString("Full name");

        System.out.println("--- Select Role ---");
        System.out.println("1. ADMIN");
        System.out.println("2. STAFF");
        System.out.print("Enter choice (1-2): ");
        int roleChoice = InputHelper.inputInt(1, 2);
        UserRole role = (roleChoice == 1) ? UserRole.ADMIN : UserRole.STAFF;

        User user = new User(id, username, password, fullName, role);
        userService.add(user);
        System.out.println("User added successfully! (ID: " + id + ")");
    }

    public static void editUI() {
        System.out.println("\n----- Edit User -----");
        System.out.print("Enter User ID to edit: ");
        long id = InputHelper.inputLong();
        try {
            User user = userService.findByID(id);
            System.out.println("Current username: " + user.getUsername());
            System.out.println("Current full name: " + user.getUserFullName());
            System.out.print("Enter new full name (leave empty to keep current): ");
            String fullName = InputHelper.inputString();
            if (!fullName.isEmpty()) {
                user.setUserFullName(fullName);
            }

            System.out.print("Do you want to change password (y/n)? ");
            if (InputHelper.inputString().equalsIgnoreCase("y")) {
                System.out.print("Enter new password: ");
                user.setUserPassword(InputHelper.inputNonEmptyString("Password"));
            }

            System.out.println("Current role: " + user.getUserRole());
            System.out.print("Do you want to change role (y/n)? ");
            if (InputHelper.inputString().equalsIgnoreCase("y")) {
                System.out.println("1. ADMIN");
                System.out.println("2. STAFF");
                System.out.print("Enter choice (1-2): ");
                int rChoice = InputHelper.inputInt(1, 2);
                user.setUserRole(rChoice == 1 ? UserRole.ADMIN : UserRole.STAFF);
            }

            userService.update(id, user);
            System.out.println("User updated successfully!");
        } catch (DataNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void deleteUI() {
        System.out.println("\n----- Delete User -----");
        System.out.print("Enter User ID to delete: ");
        long id = InputHelper.inputLong();
        try {
            User user = userService.findByID(id);
            System.out.print("Are you sure you want to delete user '" + user.getUsername() + "' (y/n)? ");
            if (InputHelper.inputString().equalsIgnoreCase("y")) {
                userService.delete(id);
                System.out.println("User deleted successfully!");
            } else {
                System.out.println("Deletion cancelled.");
            }
        } catch (DataNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void getAllUI() {
        System.out.println("\n----- List of Users -----");
        List<User> list = userService.getAll();
        printUserList(list);
    }

    public static void searchUI() {
        System.out.println("\n----- Search User -----");
        System.out.print("Enter keyword: ");
        String keyword = InputHelper.inputString();
        List<User> list = userService.findAllByContains(keyword);
        printUserList(list);
    }

    private static void printUserList(List<User> list) {
        if (list.isEmpty()) {
            System.out.println("No users found.");
            return;
        }
        System.out.printf("%-15s %-20s %-25s %-15s\n", "User ID", "Username", "Full Name", "Role");
        System.out.println("--------------------------------------------------------------------------------");
        for (User u : list) {
            System.out.printf("%-15d %-20s %-25s %-15s\n",
                    u.getUserId(), u.getUsername(), u.getUserFullName(), u.getUserRole());
        }
    }
}
