package repository;

import entity.Customer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepository implements IRepository<Customer> {
    private static final String FILE_NAME = "data/customers.csv";

    @Override
    public void write(List<Customer> list) {
        StringBuilder dataString = new StringBuilder();
        for (Customer item : list) {
            dataString.append(item.getCustomerId()).append(",")
                    .append(item.getCustomerName()).append(",")
                    .append(item.getCustomerPhone()).append(",")
                    .append(item.getCustomerAddress()).append("\n");
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
            System.out.println("CustomerRepository.write.error: " + e.getMessage());
        }
    }

    @Override
    public List<Customer> read() {
        List<Customer> list = new ArrayList<>();
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
                    long customerId = Long.parseLong(data[0].trim());
                    String customerName = data[1].trim();
                    String customerPhone = data[2].trim();
                    String customerAddress = data[3].trim();
                    Customer item = new Customer(customerId, customerName, customerPhone, customerAddress);
                    list.add(item);
                }
            }
        } catch (IOException e) {
            System.out.println("CustomerRepository.read.error: " + e.getMessage());
        }
        return list;
    }
}
