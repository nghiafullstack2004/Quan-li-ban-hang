package entity;

public class OrderItem {
    private long orderId;
    private long productId;
    private int quantity;
    private double unitPrice;

    public OrderItem() {
    }

    public OrderItem(long orderId, long productId, int quantity, double unitPrice) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getSubtotal() {
        return unitPrice * quantity;
    }

    @Override
    public String toString() {
        return "OrderID: " + orderId + " | ProductID: " + productId + " | Quantity: " + quantity +
                " | UnitPrice: " + unitPrice + " | Subtotal: " + getSubtotal();
    }
}
