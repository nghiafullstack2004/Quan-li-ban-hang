package repository;

import entity.User;
import entity.UserRole;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements IRepository<User> {
    private static final String FILE_NAME = "data/users.csv";

    @Override
    public void write(List<User> list) {
        StringBuilder dataString = new StringBuilder();
        for (User item : list) {
            dataString.append(item.getUserId()).append(",")
                    .append(item.getUsername()).append(",")
                    .append(item.getUserPassword()).append(",")
                    .append(item.getUserFullName()).append(",")
                    .append(item.getUserRole()).append("\n");
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
            System.out.println("UserRepository.write.error: " + e.getMessage());
        }
    }

    @Override
    public List<User> read() {
        List<User> list = new ArrayList<>();
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
                    long userId = Long.parseLong(data[0].trim());
                    String userName = data[1].trim();
                    String userPassword = data[2].trim();
                    String userFullName = data[3].trim();
                    UserRole userRole = UserRole.valueOf(data[4].trim());
                    User item = new User(userId, userName, userPassword, userFullName, userRole);
                    list.add(item);
                }
            }
        } catch (IOException e) {
            System.out.println("UserRepository.read.error: " + e.getMessage());
        }
        return list;
    }
}
