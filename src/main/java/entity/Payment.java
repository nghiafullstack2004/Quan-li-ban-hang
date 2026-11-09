package entity;

import java.time.LocalDateTime;

public class Payment {
    private long paymentId;
    private long orderId;
    private double amount;
    private PaymentMethod method;
    private LocalDateTime paidAt;

    public Payment() {
    }

    public Payment(long paymentId, long orderId, double amount, PaymentMethod method, LocalDateTime paidAt) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.amount = amount;
        this.method = method;
        this.paidAt = paidAt;
    }

    public long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(long paymentId) {
        this.paymentId = paymentId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public void setMethod(PaymentMethod method) {
        this.method = method;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }

    @Override
    public String toString() {
        return "PaymentID: " + paymentId + " | OrderID: " + orderId + " | Amount: " + amount +
                " | Method: " + method + " | PaidAt: " + paidAt;
    }
}
