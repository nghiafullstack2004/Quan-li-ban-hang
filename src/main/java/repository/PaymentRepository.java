package repository;

import entity.Payment;
import entity.PaymentMethod;


import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PaymentRepository implements IRepository<Payment> {
    private static final String FILE_NAME="data/payments.csv";

    @Override
    public void write(List<Payment> list) {
        String dataString="";
        for (Payment item:list){
            dataString+=item.getPaymentId()+","+item.getOrderId()+item.getAmount()+item.getMethod()+item.getPaidAt()+"\n";
        }
        System.out.println(dataString);

        try {
            FileWriter fileWriter=new FileWriter(FILE_NAME);
            BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);
            bufferedWriter.write(dataString);
            bufferedWriter.close();
        }catch (IOException e){
            System.out.println("PaymentRepository.write.error"+e.getMessage());
        }
    }

    @Override
    public List<Payment> read() {
        List<Payment> list=new ArrayList<>();
        try{
            FileReader fileReader=new FileReader(FILE_NAME);
            BufferedReader bufferedReader=new BufferedReader(fileReader);
            while (true){
                String line=bufferedReader.readLine();
                if (line==null)break;
                String[] data=line.split(",");
                long paymentId=Long.parseLong(data[0]);
                long orderId=Long.parseLong(data[1]);
                double amount=Double.parseDouble(data[2]);
                PaymentMethod method = PaymentMethod.valueOf(data[3].trim());
                LocalDateTime paidAt = LocalDateTime.parse(data[4].trim());
                Payment item=new Payment(paymentId,orderId,amount,method,paidAt);
                list.add(item);
            }
            bufferedReader.close();
            return list;
        }catch (IOException e){
            System.out.println("PaymentRepository.read.error: "+e.getMessage());
        }
        return list;
    }
}
