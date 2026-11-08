package repository;

import entity.Order;

import entity.OrderStatus;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository implements IRepository<Order>{
    private static final String FILE_NAME="data/orders.csv";

    @Override
    public void write(List<Order> list) {
        String dataString="";
        for (Order item:list){
            dataString+=item.getOrderId()+","+item.getCustomerorderId()+item.getUserId()+item.getOrderDate()+item.getOrderStatus()+item.getOrderTotal()+"\n";
        }
        System.out.println(dataString);

        try {
            FileWriter fileWriter=new FileWriter(FILE_NAME);
            BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);
            bufferedWriter.write(dataString);
            bufferedWriter.close();
        }catch (IOException e){
            System.out.println("OrderRepository.write.error"+e.getMessage());
        }
    }

    @Override
    public List<Order> read() {
        List<Order> list=new ArrayList<>();
        try{
            FileReader fileReader=new FileReader(FILE_NAME);
            BufferedReader bufferedReader=new BufferedReader(fileReader);
            while (true){
                String line=bufferedReader.readLine();
                if (line==null)break;
                String[] data=line.split(",");
                long orderId=Long.parseLong(data[0]);
                long customerId=Long.parseLong(data[1]);
                long userId=Long.parseLong(data[2]);
                LocalDate date = LocalDate.parse(data[3].trim());
                OrderStatus orderStatus = OrderStatus.valueOf(data[4].trim());
                double orderTotal=Double.parseDouble(data[5]);
                Order item=new Order(orderId,customerId,userId,date,orderStatus,orderTotal);
                list.add(item);
            }
            bufferedReader.close();
            return list;
        }catch (IOException e){
            System.out.println("OrderRepository.read.error: "+e.getMessage());
        }
        return list;
    }
}
