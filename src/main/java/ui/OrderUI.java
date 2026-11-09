package ui;

import entity.*;
import lib.InputHelper;
import lib.errors.DataNotFoundException;
import service.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderUI {
    private static final OrderService orderService = OrderService.getInstance();
    private static final OrderItemService orderItemService = OrderItemService.getInstance();
    private static final CustomerService customerService = CustomerService.getInstance();
    private static final ProductService productService = ProductService.getInstance();
    private static final UserService userService = UserService.getInstance();

    public static void showMainMenu() {
        int choice;
        do {
            System.out.println("\n========== ORDER MANAGEMENT ==========");
            System.out.println("1. Create New Order");
            System.out.println("2. View All Orders");
            System.out.println("3. View Order Details");
            System.out.println("4. Update Order Status");
            System.out.println("5. Delete Order");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");
            choice = InputHelper.inputInt();
            switch (choice) {
                case 1:
                    createOrderUI();
                    break;
                case 2:
                    getAllUI();
                    break;
                case 3:
                    viewDetailsUI();
                    break;
                case 4:
                    updateStatusUI();
                    break;
                case 5:
                    deleteUI();
                    break;
                case 0:
                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid choice, please try again!");
            }
        } while (choice != 0);
    }

    public static void createOrderUI() {
        System.out.println("\n----- Create New Order -----");
        List<Customer> customers = customerService.getAll();
        if (customers.isEmpty()) {
            System.out.println("No customers found! Please add a customer first.");
            return;
        }

        List<Product> products = productService.getAll();
        if (products.isEmpty()) {
            System.out.println("No products found! Please add products first.");
            return;
        }

        // 1. Select Customer
        System.out.println("--- Select Customer ---");
        for (int i = 0; i < customers.size(); i++) {
            Customer c = customers.get(i);
            System.out.println((i + 1) + ". [ID: " + c.getCustomerId() + "] " + c.getCustomerName() + " (" + c.getCustomerPhone() + ")");
        }
        long customerId;
        while (true) {
            System.out.print("Enter Customer ID: ");
            customerId = InputHelper.inputLong();
            try {
                customerService.findByID(customerId);
                break;
            } catch (DataNotFoundException e) {
                System.out.println("Invalid Customer ID! Please try again.");
            }
        }

        // 2. Select Staff/User (Optional, default to 1 or select if available)
        long userId = 1;
        List<User> users = userService.getAll();
        if (!users.isEmpty()) {
            System.out.println("--- Select Staff/User ---");
            for (int i = 0; i < users.size(); i++) {
                User u = users.get(i);
                System.out.println((i + 1) + ". [ID: " + u.getUserId() + "] " + u.getUserFullName() + " (" + u.getUsername() + ")");
            }
            System.out.print("Enter User ID (or press 0 to skip): ");
            long enteredUserId = InputHelper.inputLong();
            if (enteredUserId != 0) {
                try {
                    userService.findByID(enteredUserId);
                    userId = enteredUserId;
                } catch (DataNotFoundException ignored) {
                    System.out.println("User not found, defaulting to ID: 1");
                }
            }
        }

        // 3. Add Order Items
        long orderId = Instant.now().getEpochSecond();
        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0;

        while (true) {
            System.out.println("\n--- Add Product to Order ---");
            for (int i = 0; i < products.size(); i++) {
                Product p = products.get(i);
                System.out.printf("%d. [ID: %d] %-20s Price: %-10.2f Stock: %d\n",
                        (i + 1), p.getProductId(), p.getProductName(), p.getProductPrice(), p.getProductStock());
            }

            System.out.print("Enter Product ID to add: ");
            long prodId = InputHelper.inputLong();
            Product selectedProduct = null;
            try {
                selectedProduct = productService.findByID(prodId);
            } catch (DataNotFoundException e) {
                System.out.println("Product not found! Please try again.");
                continue;
            }

            System.out.print("Enter quantity: ");
            int qty = InputHelper.inputInt();
            if (qty <= 0) {
                System.out.println("Quantity must be greater than 0!");
                continue;
            }
            if (qty > selectedProduct.getProductStock()) {
                System.out.println("Not enough stock! Available stock: " + selectedProduct.getProductStock());
                continue;
            }

            // Deduct stock
            selectedProduct.setProductStock(selectedProduct.getProductStock() - qty);
            if (selectedProduct.getProductStock() == 0) {
                selectedProduct.setProductStatus(ProductStatus.OUT_OF_STOCK);
            }
            productService.update(selectedProduct.getProductId(), selectedProduct);

            OrderItem item = new OrderItem(orderId, prodId, qty, selectedProduct.getProductPrice());
            orderItems.add(item);
            totalAmount += item.getSubtotal();

            System.out.print("Add another product to this order (y/n)? ");
            if (!InputHelper.inputString().equalsIgnoreCase("y")) {
                break;
            }
        }

        if (orderItems.isEmpty()) {
            System.out.println("No items added. Order creation cancelled.");
            return;
        }

        // Save order items
        for (OrderItem item : orderItems) {
            orderItemService.add(item);
        }

        // Save order
        Order order = new Order(orderId, customerId, userId, LocalDate.now(), OrderStatus.PENDING, totalAmount);
        orderService.add(order);

        System.out.println("\nOrder created successfully! (Order ID: " + orderId + ", Total: " + totalAmount + ")");
    }

    public static void getAllUI() {
        System.out.println("\n----- List of Orders -----");
        List<Order> list = orderService.getAll();
        if (list.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }
        System.out.printf("%-15s %-25s %-12s %-12s %-12s\n",
                "Order ID", "Customer", "Date", "Total", "Status");
        System.out.println("-------------------------------------------------------------------------------");
        for (Order o : list) {
            String custName = "Unknown";
            try {
                custName = customerService.findByID(o.getCustomerorderId()).getCustomerName();
            } catch (Exception ignored) {
            }
            System.out.printf("%-15d %-25s %-12s %-12.2f %-12s\n",
                    o.getOrderId(), custName, o.getOrderDate(), o.getOrderTotal(), o.getOrderStatus());
        }
    }

    public static void viewDetailsUI() {
        System.out.println("\n----- View Order Details -----");
        System.out.print("Enter Order ID: ");
        long id = InputHelper.inputLong();
        try {
            Order order = orderService.findByID(id);
            String custName = "Unknown";
            String custPhone = "Unknown";
            try {
                Customer c = customerService.findByID(order.getCustomerorderId());
                custName = c.getCustomerName();
                custPhone = c.getCustomerPhone();
            } catch (Exception ignored) {
            }

            System.out.println("==================================================");
            System.out.println("Order ID   : " + order.getOrderId());
            System.out.println("Date       : " + order.getOrderDate());
            System.out.println("Customer   : " + custName + " (Phone: " + custPhone + ")");
            System.out.println("Status     : " + order.getOrderStatus());
            System.out.println("--------------------------------------------------");
            System.out.printf("%-20s %-10s %-10s %-10s\n", "Product", "Price", "Qty", "Subtotal");
            System.out.println("--------------------------------------------------");

            List<OrderItem> items = orderItemService.findByOrderId(order.getOrderId());
            for (OrderItem it : items) {
                String prodName = "Unknown";
                try {
                    prodName = productService.findByID(it.getProductId()).getProductName();
                } catch (Exception ignored) {
                }
                System.out.printf("%-20s %-10.2f %-10d %-10.2f\n",
                        prodName, it.getUnitPrice(), it.getQuantity(), it.getSubtotal());
            }
            System.out.println("--------------------------------------------------");
            System.out.printf("TOTAL AMOUNT: %.2f\n", order.getOrderTotal());
            System.out.println("==================================================");
        } catch (DataNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void updateStatusUI() {
        System.out.println("\n----- Update Order Status -----");
        System.out.print("Enter Order ID: ");
        long id = InputHelper.inputLong();
        try {
            Order order = orderService.findByID(id);
            System.out.println("Current status: " + order.getOrderStatus());
            System.out.println("Select new status:");
            System.out.println("1. PENDING");
            System.out.println("2. PAID");
            System.out.println("3. CANCELLED");
            System.out.print("Enter choice (1-3): ");
            int ch = InputHelper.inputInt(1, 3);
            switch (ch) {
                case 1: order.setOrderStatus(OrderStatus.PENDING); break;
                case 2: order.setOrderStatus(OrderStatus.PAID); break;
                case 3: order.setOrderStatus(OrderStatus.CANCELLED); break;
            }
            orderService.update(id, order);
            System.out.println("Order status updated successfully!");
        } catch (DataNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void deleteUI() {
        System.out.println("\n----- Delete Order -----");
        System.out.print("Enter Order ID to delete: ");
        long id = InputHelper.inputLong();
        try {
            orderService.findByID(id);
            System.out.print("Are you sure you want to delete order " + id + " and its items (y/n)? ");
            if (InputHelper.inputString().equalsIgnoreCase("y")) {
                orderItemService.deleteByOrderId(id);
                orderService.delete(id);
                System.out.println("Order deleted successfully!");
            } else {
                System.out.println("Deletion cancelled.");
            }
        } catch (DataNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
