package repository;

import entity.Product;
import entity.ProductStatus;
import lib.errors.DataNotFoundException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository implements IRepository<Product>{
    private static final String FILENAME=("data/products.csv");


    @Override
    public void write(List<Product> list) {
        String dataString="";
        for (Product item:list){
            dataString=item.getProductId()+item.getProductName()+item.getProductPrice()+ item.getProductStock()+item.getCategoryId()+item.getProductStatus()+"\n";
        }

        try{
            FileWriter fileWriter=new FileWriter(FILENAME);
            BufferedWriter bufferedWriter=new BufferedWriter(fileWriter);
            bufferedWriter.write(dataString);
            bufferedWriter.close();
        }catch (IOException e){
            System.out.println("ProductRepository.write.error"+e.getMessage());
        }
    }

    @Override
    public List<Product> read() {
        List<Product> list=new ArrayList<>();
        try{
            FileReader fileReader=new FileReader(FILENAME);
            BufferedReader bufferedReader=new BufferedReader(fileReader);
            while (true){
                String line=bufferedReader.readLine();
                if (line==null)break;
                String[] data=line.split(",");
                long productId=Long.parseLong(data[0]);
                String productName=data[1];
                double productPrice=Double.parseDouble(data[2]);
                int productStock=Integer.parseInt(data[3]);
                long categoryId=Long.parseLong(data[4]);
                ProductStatus productStatus=ProductStatus.valueOf(data[5].trim());
                Product item=new Product(productId,productName,productPrice,productStock,categoryId,productStatus);
                list.add(item);
            }
            bufferedReader.close();

        }catch (IOException e){
            System.out.println("ProductRepository.write.error"+e.getMessage());
        }
        return list;
    }
}
