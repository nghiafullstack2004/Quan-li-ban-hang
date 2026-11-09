package ui;

import entity.Category;
import lib.InputHelper;
import lib.errors.DataNotFoundException;
import service.CategoryService;

import java.time.Instant;
import java.util.List;

public class CategoryUI {
    private static final CategoryService categoryService = CategoryService.getInstance();

    public static void showMainMenu() {
        int choice;
        do {
            System.out.println("\n========== CATEGORY MANAGEMENT ==========");
            System.out.println("1. Add Category");
            System.out.println("2. Edit Category");
            System.out.println("3. Delete Category");
            System.out.println("4. View All Categories");
            System.out.println("5. Search Category by Name");
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
        System.out.println("\n----- Add Category -----");
        long id = Instant.now().getEpochSecond();
        System.out.print("Enter category name: ");
        String name = InputHelper.inputNonEmptyString("Category name");
        System.out.print("Enter category description: ");
        String description = InputHelper.inputString();

        Category newCategory = new Category(id, name, description);
        categoryService.add(newCategory);
        System.out.println("Category added successfully! (ID: " + id + ")");
    }

    public static void editUI() {
        System.out.println("\n----- Edit Category -----");
        System.out.print("Enter category ID to edit: ");
        long id = InputHelper.inputLong();
        try {
            Category category = categoryService.findByID(id);
            System.out.println("Current name: " + category.getCategoryName());
            System.out.print("Enter new name (leave empty to keep current): ");
            String newName = InputHelper.inputString();
            if (!newName.isEmpty()) {
                category.setCategoryName(newName);
            }

            System.out.println("Current description: " + category.getCategoryDescription());
            System.out.print("Enter new description (leave empty to keep current): ");
            String newDesc = InputHelper.inputString();
            if (!newDesc.isEmpty()) {
                category.setCategoryDescription(newDesc);
            }

            categoryService.update(id, category);
            System.out.println("Category updated successfully!");
        } catch (DataNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void deleteUI() {
        System.out.println("\n----- Delete Category -----");
        System.out.print("Enter category ID to delete: ");
        long id = InputHelper.inputLong();
        try {
            Category category = categoryService.findByID(id);
            System.out.print("Are you sure you want to delete '" + category.getCategoryName() + "' (y/n)? ");
            String confirm = InputHelper.inputString();
            if (confirm.equalsIgnoreCase("y")) {
                categoryService.delete(id);
                System.out.println("Category deleted successfully!");
            } else {
                System.out.println("Deletion cancelled.");
            }
        } catch (DataNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void getAllUI() {
        System.out.println("\n----- List of Categories -----");
        List<Category> list = categoryService.getAll();
        if (list.isEmpty()) {
            System.out.println("No categories found.");
            return;
        }
        System.out.printf("%-15s %-25s %-35s\n", "Category ID", "Category Name", "Description");
        System.out.println("-------------------------------------------------------------------------------");
        for (Category item : list) {
            System.out.printf("%-15d %-25s %-35s\n",
                    item.getCategoryId(), item.getCategoryName(), item.getCategoryDescription());
        }
    }

    public static void searchUI() {
        System.out.println("\n----- Search Category -----");
        System.out.print("Enter keyword to search: ");
        String keyword = InputHelper.inputString();
        List<Category> list = categoryService.findAllByContains(keyword);
        if (list.isEmpty()) {
            System.out.println("No categories matched the keyword.");
            return;
        }
        System.out.printf("%-15s %-25s %-35s\n", "Category ID", "Category Name", "Description");
        System.out.println("-------------------------------------------------------------------------------");
        for (Category item : list) {
            System.out.printf("%-15d %-25s %-35s\n",
                    item.getCategoryId(), item.getCategoryName(), item.getCategoryDescription());
        }
    }
}
