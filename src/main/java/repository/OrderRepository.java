package repository;

import entity.Order;
import entity.OrderStatus;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository implements IRepository<Order> {
    private static final String FILE_NAME = "data/orders.csv";

    @Override
    public void write(List<Order> list) {
        StringBuilder dataString = new StringBuilder();
        for (Order item : list) {
            dataString.append(item.getOrderId()).append(",")
                    .append(item.getCustomerorderId()).append(",")
                    .append(item.getUserId()).append(",")
                    .append(item.getOrderDate()).append(",")
                    .append(item.getOrderStatus()).append(",")
                    .append(item.getOrderTotal()).append("\n");
        }

        try {
            File file = new File(FILE_NAME);
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }
            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
                bufferedWriter.write(dataString.toString());
            }
        } catch (IOException e) {
            System.out.println("OrderRepository.write.error: " + e.getMessage());
        }
    }

    @Override
    public List<Order> read() {
        List<Order> list = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return list;
        }

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] data = line.split(",");
                if (data.length >= 6) {
                    long orderId = Long.parseLong(data[0].trim());
                    long customerId = Long.parseLong(data[1].trim());
                    long userId = Long.parseLong(data[2].trim());
                    LocalDate date = LocalDate.parse(data[3].trim());
                    OrderStatus orderStatus = OrderStatus.valueOf(data[4].trim());
                    double orderTotal = Double.parseDouble(data[5].trim());
                    Order item = new Order(orderId, customerId, userId, date, orderStatus, orderTotal);
                    list.add(item);
                }
            }
        } catch (IOException e) {
            System.out.println("OrderRepository.read.error: " + e.getMessage());
        }
        return list;
    }
}
