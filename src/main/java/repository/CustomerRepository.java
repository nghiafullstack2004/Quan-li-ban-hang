package repository;

import entity.Customer;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepository implements IRepository<Customer>{
    private static final String FILE_NAME="data/customers.csv";

    @Override
    public void write(List<Customer> list) {
        String dataString="";
        for (Customer item:list){
            dataString+=item.getCustomerId()+","+item.getCustomerName()+item.getCustomerPhone()+item.getCustomerAddress()+"\n";
        }
        System.out.println(dataString);

        try {
            FileWriter fileWriter=new FileWriter(FILE_NAME);
            BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);
            bufferedWriter.write(dataString);
            bufferedWriter.close();
        }catch (IOException e){
            System.out.println("CustomerRepository.write.error"+e.getMessage());
        }
    }

    @Override
    public List<Customer> read() {
        List<Customer> list=new ArrayList<>();
        try{
            FileReader fileReader=new FileReader(FILE_NAME);
            BufferedReader bufferedReader=new BufferedReader(fileReader);
            while (true){
                String line=bufferedReader.readLine();
                if (line==null)break;
                String[] data=line.split(",");
                long customerId=Long.parseLong(data[0]);
                String customerName=data[1];
                String customerPhone=data[2];
                String customerAddress=data[3];
                Customer item=new Customer(customerId,customerName,customerPhone,customerAddress);
                list.add(item);
            }
            bufferedReader.close();
            return list;
        }catch (IOException e){
            System.out.println("CustomerRepository.read.error: "+e.getMessage());
        }
        return list;
    }
}
