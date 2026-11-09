package repository;

import entity.Payment;
import entity.PaymentMethod;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PaymentRepository implements IRepository<Payment> {
    private static final String FILE_NAME = "data/payment.csv";

    @Override
    public void write(List<Payment> list) {
        StringBuilder dataString = new StringBuilder();
        for (Payment item : list) {
            dataString.append(item.getPaymentId()).append(",")
                    .append(item.getOrderId()).append(",")
                    .append(item.getAmount()).append(",")
                    .append(item.getMethod()).append(",")
                    .append(item.getPaidAt()).append("\n");
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
            System.out.println("PaymentRepository.write.error: " + e.getMessage());
        }
    }

    @Override
    public List<Payment> read() {
        List<Payment> list = new ArrayList<>();
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
                if (data.length >= 5) {
                    long paymentId = Long.parseLong(data[0].trim());
                    long orderId = Long.parseLong(data[1].trim());
                    double amount = Double.parseDouble(data[2].trim());
                    PaymentMethod method = PaymentMethod.valueOf(data[3].trim());
                    LocalDateTime paidAt = LocalDateTime.parse(data[4].trim());
                    Payment item = new Payment(paymentId, orderId, amount, method, paidAt);
                    list.add(item);
                }
            }
        } catch (IOException e) {
            System.out.println("PaymentRepository.read.error: " + e.getMessage());
        }
        return list;
    }
}
