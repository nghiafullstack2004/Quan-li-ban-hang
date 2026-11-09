package entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private long orderId;
    private long customerorderId;
    private long userId;
    private LocalDate orderDate;
    private OrderStatus orderStatus;
    private double orderTotal;
    private List<OrderItem> items = new ArrayList<>();

    public Order() {
    }

    public Order(long orderId, long customerorderId, long userorderId, LocalDate date, OrderStatus status, double total) {
        this.orderId = orderId;
        this.customerorderId = customerorderId;
        this.userId = userorderId;
        this.orderDate = date;
        this.orderStatus = status;
        this.orderTotal = total;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getCustomerorderId() {
        return customerorderId;
    }

    public void setCustomerorderId(long customerorderId) {
        this.customerorderId = customerorderId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public double getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(double orderTotal) {
        this.orderTotal = orderTotal;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "OrderID: " + orderId + " | CustomerID: " + customerorderId + " | UserID: " + userId +
                " | Date: " + orderDate + " | Status: " + orderStatus + " | Total: " + orderTotal;
    }
}
