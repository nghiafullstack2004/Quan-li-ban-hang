package repository;

import entity.User;
import entity.UserRole;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements IRepository<User> {
    private static final String FILENAME="data/users.csv";

    public void write(List<User> list){
        String dataString="";
        for (User item:list){
            dataString=item.getUserId()+item.getUsername()+item.getUserPassword()+item.getUserFullName()+item.getUserRole()+"\n";
        }
        try{
            FileWriter fileWriter=new FileWriter(FILENAME);
            BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);
            bufferedWriter.write(dataString);
            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println("UserRepository.write.errors"+e.getMessage());
        }
    }

    public List<User> read(){
        List<User> list=new ArrayList<>();
        try {
            FileReader fileReader=new FileReader(FILENAME);
            BufferedReader bufferedReader=new BufferedReader(fileReader);
            while (true){
                String line=bufferedReader.readLine();
                if (line==null)break;
                String[] data=line.split(",");
                long userId=Long.parseLong(data[0]);
                String userName=data[1];
                String userPassword=data[2];
                String userFullName=data[3];
                UserRole userRole=UserRole.valueOf(data[4].trim());
                User item=new User(userId,userName,userPassword,userFullName,userRole);
                list.add(item);
            }
            bufferedReader.close();
        }catch (IOException e){
            System.out.println("UserRepository.read.errors"+e.getMessage());
        }
        return list;
    }
}
