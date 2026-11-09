package repository;

import entity.Product;
import entity.ProductStatus;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository implements IRepository<Product> {
    private static final String FILE_NAME = "data/products.csv";

    @Override
    public void write(List<Product> list) {
        StringBuilder dataString = new StringBuilder();
        for (Product item : list) {
            dataString.append(item.getProductId()).append(",")
                    .append(item.getProductName()).append(",")
                    .append(item.getProductPrice()).append(",")
                    .append(item.getProductStock()).append(",")
                    .append(item.getCategoryId()).append(",")
                    .append(item.getProductStatus()).append("\n");
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
            System.out.println("ProductRepository.write.error: " + e.getMessage());
        }
    }

    @Override
    public List<Product> read() {
        List<Product> list = new ArrayList<>();
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
                    long productId = Long.parseLong(data[0].trim());
                    String productName = data[1].trim();
                    double productPrice = Double.parseDouble(data[2].trim());
                    int productStock = Integer.parseInt(data[3].trim());
                    long categoryId = Long.parseLong(data[4].trim());
                    ProductStatus productStatus = ProductStatus.valueOf(data[5].trim());
                    Product item = new Product(productId, productName, productPrice, productStock, categoryId, productStatus);
                    list.add(item);
                }
            }
        } catch (IOException e) {
            System.out.println("ProductRepository.read.error: " + e.getMessage());
        }
        return list;
    }
}
