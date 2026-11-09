package service;

import entity.OrderItem;
import lib.errors.DataNotFoundException;
import repository.OrderItemRepository;

import java.util.ArrayList;
import java.util.List;

public class OrderItemService implements IService<OrderItem> {
    private final OrderItemRepository orderItemRepository = new OrderItemRepository();
    private final List<OrderItem> list;
    private static final OrderItemService orderItemService = new OrderItemService();

    private OrderItemService() {
        this.list = orderItemRepository.read();
    }

    public static OrderItemService getInstance() {
        return orderItemService;
    }

    public static OrderItemService getObject() {
        return orderItemService;
    }

    public List<OrderItem> findByOrderId(long orderId) {
        List<OrderItem> items = new ArrayList<>();
        for (OrderItem item : list) {
            if (item.getOrderId() == orderId) {
                items.add(item);
            }
        }
        return items;
    }

    public void deleteByOrderId(long orderId) {
        list.removeIf(item -> item.getOrderId() == orderId);
        orderItemRepository.write(list);
    }

    @Override
    public void add(OrderItem newOrderItem) {
        this.list.add(newOrderItem);
        this.orderItemRepository.write(this.list);
    }

    @Override
    public void delete(long id) {
        // Xóa theo orderId
        deleteByOrderId(id);
    }

    @Override
    public void update(long id, OrderItem orderItem) {
        int index = findIndexById(id);
        this.list.set(index, orderItem);
        this.orderItemRepository.write(this.list);
    }

    @Override
    public List<OrderItem> getAll() {
        return this.list;
    }

    @Override
    public int findIndexById(long id) {
        for (int i = 0; i < this.list.size(); i++) {
            OrderItem item = this.list.get(i);
            if (item.getOrderId() == id) {
                return i;
            }
        }
        throw new DataNotFoundException("OrderItem not found with Order ID: " + id);
    }

    @Override
    public OrderItem findByID(long id) {
        int index = this.findIndexById(id);
        return this.list.get(index);
    }
}

