import entity.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ProjectIntegrationTest {

    @Test
    public void testCategoryFlow() {
        CategoryService service = CategoryService.getInstance();
        long testId = 999901L;
        Category category = new Category(testId, "Test Beverage", "Soft drinks and juices");

        service.add(category);
        Category found = service.findByID(testId);
        Assertions.assertNotNull(found);
        Assertions.assertEquals("Test Beverage", found.getCategoryName());

        found.setCategoryName("Updated Beverage");
        service.update(testId, found);
        Assertions.assertEquals("Updated Beverage", service.findByID(testId).getCategoryName());

        service.delete(testId);
        Assertions.assertThrows(Exception.class, () -> service.findByID(testId));
    }

    @Test
    public void testProductFlow() {
        ProductService service = ProductService.getInstance();
        long testId = 999902L;
        Product product = new Product(testId, "Coca Cola", 10000.0, 50, 1L, ProductStatus.ACTIVE);

        service.add(product);
        Product found = service.findByID(testId);
        Assertions.assertNotNull(found);
        Assertions.assertEquals("Coca Cola", found.getProductName());

        found.setProductPrice(12000.0);
        service.update(testId, found);
        Assertions.assertEquals(12000.0, service.findByID(testId).getProductPrice());

        service.delete(testId);
        Assertions.assertThrows(Exception.class, () -> service.findByID(testId));
    }

    @Test
    public void testCustomerFlow() {
        CustomerService service = CustomerService.getInstance();
        long testId = 999903L;
        Customer customer = new Customer(testId, "Nguyen Van A", "0901234567", "Da Nang");

        service.add(customer);
        Customer found = service.findByID(testId);
        Assertions.assertNotNull(found);
        Assertions.assertEquals("Nguyen Van A", found.getCustomerName());

        service.delete(testId);
        Assertions.assertThrows(Exception.class, () -> service.findByID(testId));
    }

    @Test
    public void testOrderAndItemFlow() {
        OrderService orderService = OrderService.getInstance();
        OrderItemService itemService = OrderItemService.getInstance();

        long orderId = 999904L;
        Order order = new Order(orderId, 1L, 1L, LocalDate.now(), OrderStatus.PENDING, 24000.0);
        orderService.add(order);

        OrderItem item = new OrderItem(orderId, 1L, 2, 12000.0);
        itemService.add(item);

        Order foundOrder = orderService.findByID(orderId);
        Assertions.assertNotNull(foundOrder);
        Assertions.assertEquals(OrderStatus.PENDING, foundOrder.getOrderStatus());

        List<OrderItem> items = itemService.findByOrderId(orderId);
        Assertions.assertFalse(items.isEmpty());
        Assertions.assertEquals(24000.0, items.get(0).getSubtotal());

        itemService.deleteByOrderId(orderId);
        orderService.delete(orderId);
        Assertions.assertThrows(Exception.class, () -> orderService.findByID(orderId));
    }

    @Test
    public void testPaymentAndUserFlow() {
        PaymentService paymentService = PaymentService.getInstance();
        UserService userService = UserService.getInstance();

        long userId = 999905L;
        User user = new User(userId, "testadmin", "pass123", "Admin Test", UserRole.ADMIN);
        userService.add(user);
        Assertions.assertNotNull(userService.findByID(userId));
        userService.delete(userId);

        long paymentId = 999906L;
        Payment payment = new Payment(paymentId, 100L, 50000.0, PaymentMethod.CASH, LocalDateTime.now());
        paymentService.add(payment);
        Assertions.assertNotNull(paymentService.findByID(paymentId));
        paymentService.delete(paymentId);
    }
}

