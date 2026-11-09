package service;

import entity.Order;
import entity.OrderStatus;
import lib.errors.DataNotFoundException;
import repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;

public class OrderService implements IService<Order> {
    private final OrderRepository orderRepository = new OrderRepository();
    private final List<Order> list;
    private static final OrderService orderService = new OrderService();

    private OrderService() {
        this.list = orderRepository.read();
    }

    public static OrderService getInstance() {
        return orderService;
    }

    public static OrderService getObject() {
        return orderService;
    }

    public List<Order> findByCustomerId(long customerId) {
        List<Order> listFound = new ArrayList<>();
        for (Order item : list) {
            if (item.getCustomerorderId() == customerId) {
                listFound.add(item);
            }
        }
        return listFound;
    }

    public List<Order> findByStatus(OrderStatus status) {
        List<Order> listFound = new ArrayList<>();
        for (Order item : list) {
            if (item.getOrderStatus() == status) {
                listFound.add(item);
            }
        }
        return listFound;
    }

    @Override
    public void add(Order newOrder) {
        this.list.add(newOrder);
        this.orderRepository.write(this.list);
    }

    @Override
    public void delete(long id) {
        int index = findIndexById(id);
        this.list.remove(index);
        this.orderRepository.write(this.list);
    }

    @Override
    public void update(long id, Order order) {
        int index = this.findIndexById(id);
        this.list.set(index, order);
        this.orderRepository.write(this.list);
    }

    @Override
    public List<Order> getAll() {
        return this.list;
    }

    @Override
    public int findIndexById(long id) {
        for (int i = 0; i < this.list.size(); i++) {
            Order order = this.list.get(i);
            if (order.getOrderId() == id) {
                return i;
            }
        }
        throw new DataNotFoundException("Order not found with ID: " + id);
    }

    @Override
    public Order findByID(long id) {
        int index = this.findIndexById(id);
        return this.list.get(index);
    }
}
