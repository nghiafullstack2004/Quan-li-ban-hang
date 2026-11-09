package service;

import entity.Product;
import lib.errors.DataNotFoundException;
import repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;

public class ProductService implements IService<Product> {
    private final ProductRepository productRepository = new ProductRepository();
    private final List<Product> list;
    private static final ProductService productService = new ProductService();

    private ProductService() {
        this.list = productRepository.read();
    }

    public static ProductService getInstance() {
        return productService;
    }

    public static ProductService getObject() {
        return productService;
    }

    public List<Product> findAllByContains(String keyword) {
        List<Product> listFound = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        for (Product item : list) {
            if (item.getProductName().toLowerCase().contains(lowerKeyword)) {
                listFound.add(item);
            }
        }
        return listFound;
    }

    public List<Product> findByCategoryId(long categoryId) {
        List<Product> listFound = new ArrayList<>();
        for (Product item : list) {
            if (item.getCategoryId() == categoryId) {
                listFound.add(item);
            }
        }
        return listFound;
    }

    @Override
    public void add(Product newProduct) {
        this.list.add(newProduct);
        this.productRepository.write(this.list);
    }

    @Override
    public void delete(long id) {
        int index = findIndexById(id);
        this.list.remove(index);
        this.productRepository.write(this.list);
    }

    @Override
    public void update(long id, Product product) {
        int index = this.findIndexById(id);
        this.list.set(index, product);
        this.productRepository.write(this.list);
    }

    @Override
    public List<Product> getAll() {
        return this.list;
    }

    @Override
    public int findIndexById(long id) {
        for (int i = 0; i < this.list.size(); i++) {
            Product product = this.list.get(i);
            if (product.getProductId() == id) {
                return i;
            }
        }
        throw new DataNotFoundException("Product not found with ID: " + id);
    }

    @Override
    public Product findByID(long id) {
        int index = this.findIndexById(id);
        return this.list.get(index);
    }
}
