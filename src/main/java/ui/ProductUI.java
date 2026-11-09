package ui;

import entity.Category;
import entity.Product;
import entity.ProductStatus;
import lib.InputHelper;
import lib.errors.DataNotFoundException;
import service.CategoryService;
import service.ProductService;

import java.time.Instant;
import java.util.List;

public class ProductUI {
    private static final ProductService productService = ProductService.getInstance();
    private static final CategoryService categoryService = CategoryService.getInstance();

    public static void showMainMenu() {
        int choice;
        do {
            System.out.println("\n========== PRODUCT MANAGEMENT ==========");
            System.out.println("1. Add Product");
            System.out.println("2. Edit Product");
            System.out.println("3. Delete Product");
            System.out.println("4. View All Products");
            System.out.println("5. Search Product by Name");
            System.out.println("6. Filter Products by Category");
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
                    searchByNameUI();
                    break;
                case 6:
                    filterByCategoryUI();
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
        System.out.println("\n----- Add Product -----");
        if (categoryService.getAll().isEmpty()) {
            System.out.println("No categories found! Please create a category first before adding products.");
            return;
        }

        long id = Instant.now().getEpochSecond();
        System.out.print("Enter product name: ");
        String name = InputHelper.inputNonEmptyString("Product name");
        System.out.print("Enter product price: ");
        double price = InputHelper.inputDouble();
        System.out.print("Enter product stock: ");
        int stock = InputHelper.inputInt();

        long categoryId = selectCategoryId();
        ProductStatus status = selectProductStatus();

        Product newProduct = new Product(id, name, price, stock, categoryId, status);
        productService.add(newProduct);
        System.out.println("Product added successfully! (ID: " + id + ")");
    }

    private static long selectCategoryId() {
        System.out.println("--- Select Category ---");
        List<Category> categories = categoryService.getAll();
        for (int i = 0; i < categories.size(); i++) {
            Category cat = categories.get(i);
            System.out.println((i + 1) + ". [ID: " + cat.getCategoryId() + "] " + cat.getCategoryName());
        }
        while (true) {
            System.out.print("Enter Category ID: ");
            long catId = InputHelper.inputLong();
            for (Category cat : categories) {
                if (cat.getCategoryId() == catId) {
                    return catId;
                }
            }
            System.out.println("Invalid category ID! Please select from the list above.");
        }
    }

    private static ProductStatus selectProductStatus() {
        System.out.println("--- Select Status ---");
        System.out.println("1. ACTIVE");
        System.out.println("2. OUT_OF_STOCK");
        System.out.println("3. DISCONTINUED");
        System.out.print("Enter choice (1-3): ");
        int choice = InputHelper.inputInt(1, 3);
        switch (choice) {
            case 1: return ProductStatus.ACTIVE;
            case 2: return ProductStatus.OUT_OF_STOCK;
            default: return ProductStatus.DISCONTINUED;
        }
    }

    public static void editUI() {
        System.out.println("\n----- Edit Product -----");
        System.out.print("Enter product ID to edit: ");
        long id = InputHelper.inputLong();
        try {
            Product product = productService.findByID(id);
            System.out.println("Current name: " + product.getProductName());
            System.out.print("Enter new name (leave empty to keep current): ");
            String name = InputHelper.inputString();
            if (!name.isEmpty()) {
                product.setProductName(name);
            }

            System.out.println("Current price: " + product.getProductPrice());
            System.out.print("Do you want to change price (y/n)? ");
            if (InputHelper.inputString().equalsIgnoreCase("y")) {
                System.out.print("Enter new price: ");
                product.setProductPrice(InputHelper.inputDouble());
            }

            System.out.println("Current stock: " + product.getProductStock());
            System.out.print("Do you want to change stock (y/n)? ");
            if (InputHelper.inputString().equalsIgnoreCase("y")) {
                System.out.print("Enter new stock: ");
                product.setProductStock(InputHelper.inputInt());
            }

            System.out.print("Do you want to change category (y/n)? ");
            if (InputHelper.inputString().equalsIgnoreCase("y")) {
                product.setCategoryId(selectCategoryId());
            }

            System.out.print("Do you want to change status (y/n)? ");
            if (InputHelper.inputString().equalsIgnoreCase("y")) {
                product.setProductStatus(selectProductStatus());
            }

            productService.update(id, product);
            System.out.println("Product updated successfully!");
        } catch (DataNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void deleteUI() {
        System.out.println("\n----- Delete Product -----");
        System.out.print("Enter product ID to delete: ");
        long id = InputHelper.inputLong();
        try {
            Product product = productService.findByID(id);
            System.out.print("Are you sure you want to delete '" + product.getProductName() + "' (y/n)? ");
            if (InputHelper.inputString().equalsIgnoreCase("y")) {
                productService.delete(id);
                System.out.println("Product deleted successfully!");
            } else {
                System.out.println("Deletion cancelled.");
            }
        } catch (DataNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void getAllUI() {
        System.out.println("\n----- List of Products -----");
        List<Product> list = productService.getAll();
        printProductList(list);
    }

    public static void searchByNameUI() {
        System.out.println("\n----- Search Product by Name -----");
        System.out.print("Enter keyword: ");
        String keyword = InputHelper.inputString();
        List<Product> list = productService.findAllByContains(keyword);
        printProductList(list);
    }

    public static void filterByCategoryUI() {
        System.out.println("\n----- Filter Products by Category -----");
        long catId = selectCategoryId();
        List<Product> list = productService.findByCategoryId(catId);
        printProductList(list);
    }

    private static void printProductList(List<Product> list) {
        if (list.isEmpty()) {
            System.out.println("No products found.");
            return;
        }
        System.out.printf("%-15s %-25s %-12s %-8s %-20s %-15s\n",
                "Product ID", "Name", "Price", "Stock", "Category", "Status");
        System.out.println("--------------------------------------------------------------------------------------------------");
        for (Product item : list) {
            String catName = "Unknown";
            try {
                catName = categoryService.findByID(item.getCategoryId()).getCategoryName();
            } catch (Exception ignored) {
            }
            System.out.printf("%-15d %-25s %-12.2f %-8d %-20s %-15s\n",
                    item.getProductId(), item.getProductName(), item.getProductPrice(),
                    item.getProductStock(), catName, item.getProductStatus());
        }
    }
}
