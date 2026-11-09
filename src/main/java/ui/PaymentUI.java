package ui;

import entity.Order;
import entity.OrderStatus;
import entity.Payment;
import entity.PaymentMethod;
import lib.InputHelper;
import lib.errors.DataNotFoundException;
import service.OrderService;
import service.PaymentService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

public class PaymentUI {
    private static final PaymentService paymentService = PaymentService.getInstance();
    private static final OrderService orderService = OrderService.getInstance();

    public static void showMainMenu() {
        int choice;
        do {
            System.out.println("\n========== PAYMENT MANAGEMENT ==========");
            System.out.println("1. Create Payment for Order");
            System.out.println("2. View All Payments");
            System.out.println("3. View Payments by Order ID");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");
            choice = InputHelper.inputInt();
            switch (choice) {
                case 1:
                    createPaymentUI();
                    break;
                case 2:
                    getAllUI();
                    break;
                case 3:
                    viewByOrderIdUI();
                    break;
                case 0:
                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid choice, please try again!");
            }
        } while (choice != 0);
    }

    public static void createPaymentUI() {
        System.out.println("\n----- Create Payment -----");
        List<Order> pendingOrders = orderService.findByStatus(OrderStatus.PENDING);
        if (pendingOrders.isEmpty()) {
            System.out.println("No pending orders available for payment.");
            return;
        }

        System.out.println("--- Pending Orders ---");
        for (Order o : pendingOrders) {
            System.out.printf("[Order ID: %d] Total: %.2f | Date: %s\n",
                    o.getOrderId(), o.getOrderTotal(), o.getOrderDate());
        }

        System.out.print("Enter Order ID to pay: ");
        long orderId = InputHelper.inputLong();
        Order selectedOrder = null;
        try {
            selectedOrder = orderService.findByID(orderId);
            if (selectedOrder.getOrderStatus() == OrderStatus.PAID) {
                System.out.println("This order has already been paid!");
                return;
            }
        } catch (DataNotFoundException e) {
            System.out.println("Order not found!");
            return;
        }

        System.out.println("Order Total Amount: " + selectedOrder.getOrderTotal());
        System.out.println("--- Select Payment Method ---");
        System.out.println("1. CASH");
        System.out.println("2. TRANSFER");
        System.out.println("3. CARD");
        System.out.print("Enter choice (1-3): ");
        int methodChoice = InputHelper.inputInt(1, 3);
        PaymentMethod method;
        switch (methodChoice) {
            case 1: method = PaymentMethod.CASH; break;
            case 2: method = PaymentMethod.TRANSFER; break;
            default: method = PaymentMethod.CARD; break;
        }

        long paymentId = Instant.now().getEpochSecond();
        Payment payment = new Payment(paymentId, orderId, selectedOrder.getOrderTotal(), method, LocalDateTime.now());
        paymentService.add(payment);

        // Update order status to PAID
        selectedOrder.setOrderStatus(OrderStatus.PAID);
        orderService.update(orderId, selectedOrder);

        System.out.println("Payment processed successfully! (Payment ID: " + paymentId + ")");
    }

    public static void getAllUI() {
        System.out.println("\n----- List of Payments -----");
        List<Payment> list = paymentService.getAll();
        printPaymentList(list);
    }

    public static void viewByOrderIdUI() {
        System.out.println("\n----- View Payments by Order ID -----");
        System.out.print("Enter Order ID: ");
        long orderId = InputHelper.inputLong();
        List<Payment> list = paymentService.findByOrderId(orderId);
        printPaymentList(list);
    }

    private static void printPaymentList(List<Payment> list) {
        if (list.isEmpty()) {
            System.out.println("No payments found.");
            return;
        }
        System.out.printf("%-15s %-15s %-12s %-15s %-25s\n",
                "Payment ID", "Order ID", "Amount", "Method", "Paid At");
        System.out.println("-------------------------------------------------------------------------------------");
        for (Payment p : list) {
            System.out.printf("%-15d %-15d %-12.2f %-15s %-25s\n",
                    p.getPaymentId(), p.getOrderId(), p.getAmount(), p.getMethod(), p.getPaidAt());
        }
    }
}
