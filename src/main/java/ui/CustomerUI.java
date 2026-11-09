package ui;

import entity.Customer;
import lib.InputHelper;
import lib.errors.DataNotFoundException;
import service.CustomerService;

import java.time.Instant;
import java.util.List;

public class CustomerUI {
    private static final CustomerService customerService = CustomerService.getInstance();

    public static void showMainMenu() {
        int choice;
        do {
            System.out.println("\n========== CUSTOMER MANAGEMENT ==========");
            System.out.println("1. Add Customer");
            System.out.println("2. Edit Customer");
            System.out.println("3. Delete Customer");
            System.out.println("4. View All Customers");
            System.out.println("5. Search Customer by Name or Phone");
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
        System.out.println("\n----- Add Customer -----");
        long id = Instant.now().getEpochSecond();
        System.out.print("Enter customer name: ");
        String name = InputHelper.inputNonEmptyString("Customer name");
        System.out.print("Enter customer phone: ");
        String phone = InputHelper.inputNonEmptyString("Customer phone");
        System.out.print("Enter customer address: ");
        String address = InputHelper.inputString();

        Customer customer = new Customer(id, name, phone, address);
        customerService.add(customer);
        System.out.println("Customer added successfully! (ID: " + id + ")");
    }

    public static void editUI() {
        System.out.println("\n----- Edit Customer -----");
        System.out.print("Enter customer ID to edit: ");
        long id = InputHelper.inputLong();
        try {
            Customer customer = customerService.findByID(id);
            System.out.println("Current name: " + customer.getCustomerName());
            System.out.print("Enter new name (leave empty to keep current): ");
            String name = InputHelper.inputString();
            if (!name.isEmpty()) {
                customer.setCustomerName(name);
            }

            System.out.println("Current phone: " + customer.getCustomerPhone());
            System.out.print("Enter new phone (leave empty to keep current): ");
            String phone = InputHelper.inputString();
            if (!phone.isEmpty()) {
                customer.setCustomerPhone(phone);
            }

            System.out.println("Current address: " + customer.getCustomerAddress());
            System.out.print("Enter new address (leave empty to keep current): ");
            String address = InputHelper.inputString();
            if (!address.isEmpty()) {
                customer.setCustomerAddress(address);
            }

            customerService.update(id, customer);
            System.out.println("Customer updated successfully!");
        } catch (DataNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void deleteUI() {
        System.out.println("\n----- Delete Customer -----");
        System.out.print("Enter customer ID to delete: ");
        long id = InputHelper.inputLong();
        try {
            Customer customer = customerService.findByID(id);
            System.out.print("Are you sure you want to delete '" + customer.getCustomerName() + "' (y/n)? ");
            if (InputHelper.inputString().equalsIgnoreCase("y")) {
                customerService.delete(id);
                System.out.println("Customer deleted successfully!");
            } else {
                System.out.println("Deletion cancelled.");
            }
        } catch (DataNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void getAllUI() {
        System.out.println("\n----- List of Customers -----");
        List<Customer> list = customerService.getAll();
        printCustomerList(list);
    }

    public static void searchUI() {
        System.out.println("\n----- Search Customer -----");
        System.out.print("Enter keyword (name or phone): ");
        String keyword = InputHelper.inputString();
        List<Customer> list = customerService.findAllByContains(keyword);
        printCustomerList(list);
    }

    private static void printCustomerList(List<Customer> list) {
        if (list.isEmpty()) {
            System.out.println("No customers found.");
            return;
        }
        System.out.printf("%-15s %-25s %-15s %-30s\n", "Customer ID", "Name", "Phone", "Address");
        System.out.println("-----------------------------------------------------------------------------------------");
        for (Customer c : list) {
            System.out.printf("%-15d %-25s %-15s %-30s\n",
                    c.getCustomerId(), c.getCustomerName(), c.getCustomerPhone(), c.getCustomerAddress());
        }
    }
}
