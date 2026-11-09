package repository;

import entity.Category;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepository implements IRepository<Category> {
    private static final String FILE_NAME = "data/categories.csv";

    @Override
    public void write(List<Category> list) {
        StringBuilder dataString = new StringBuilder();
        for (Category item : list) {
            dataString.append(item.getCategoryId()).append(",")
                    .append(item.getCategoryName()).append(",")
                    .append(item.getCategoryDescription()).append("\n");
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
            System.out.println("CategoryRepository.write.error: " + e.getMessage());
        }
    }

    @Override
    public List<Category> read() {
        List<Category> list = new ArrayList<>();
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
                if (data.length >= 2) {
                    long categoryId = Long.parseLong(data[0].trim());
                    String categoryName = data[1].trim();
                    String categoryDescription = data.length >= 3 ? data[2].trim() : "";
                    Category item = new Category(categoryId, categoryName, categoryDescription);
                    list.add(item);
                }
            }
        } catch (IOException e) {
            System.out.println("CategoryRepository.read.error: " + e.getMessage());
        }
        return list;
    }
}
