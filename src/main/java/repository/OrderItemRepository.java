package repository;

import entity.OrderItem;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemRepository implements IRepository<OrderItem> {
    private static final String FILE_NAME = "data/order_items.csv";

    @Override
    public void write(List<OrderItem> list) {
        StringBuilder dataString = new StringBuilder();
        for (OrderItem item : list) {
            dataString.append(item.getOrderId()).append(",")
                    .append(item.getProductId()).append(",")
                    .append(item.getQuantity()).append(",")
                    .append(item.getUnitPrice()).append("\n");
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
            System.out.println("OrderItemRepository.write.error: " + e.getMessage());
        }
    }

    @Override
    public List<OrderItem> read() {
        List<OrderItem> list = new ArrayList<>();
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
                if (data.length >= 4) {
                    long orderId = Long.parseLong(data[0].trim());
                    long productId = Long.parseLong(data[1].trim());
                    int quantity = Integer.parseInt(data[2].trim());
                    double unitPrice = Double.parseDouble(data[3].trim());
                    OrderItem item = new OrderItem(orderId, productId, quantity, unitPrice);
                    list.add(item);
                }
            }
        } catch (IOException e) {
            System.out.println("OrderItemRepository.read.error: " + e.getMessage());
        }
        return list;
    }
}

