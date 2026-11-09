package service;

import entity.Customer;
import lib.errors.DataNotFoundException;
import repository.CustomerRepository;

import java.util.ArrayList;
import java.util.List;

public class CustomerService implements IService<Customer> {
    private final CustomerRepository customerRepository = new CustomerRepository();
    private final List<Customer> list;
    private static final CustomerService customerService = new CustomerService();

    private CustomerService() {
        this.list = customerRepository.read();
    }

    public static CustomerService getInstance() {
        return customerService;
    }

    public static CustomerService getObject() {
        return customerService;
    }

    public List<Customer> findAllByContains(String keyword) {
        List<Customer> listFound = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        for (Customer item : list) {
            if (item.getCustomerName().toLowerCase().contains(lowerKeyword) ||
                    item.getCustomerPhone().contains(keyword)) {
                listFound.add(item);
            }
        }
        return listFound;
    }

    @Override
    public void add(Customer newCustomer) {
        this.list.add(newCustomer);
        this.customerRepository.write(this.list);
    }

    @Override
    public void delete(long id) {
        int index = findIndexById(id);
        this.list.remove(index);
        this.customerRepository.write(this.list);
    }

    @Override
    public void update(long id, Customer customer) {
        int index = this.findIndexById(id);
        this.list.set(index, customer);
        this.customerRepository.write(this.list);
    }

    @Override
    public List<Customer> getAll() {
        return this.list;
    }

    @Override
    public int findIndexById(long id) {
        for (int i = 0; i < this.list.size(); i++) {
            Customer customer = this.list.get(i);
            if (customer.getCustomerId() == id) {
                return i;
            }
        }
        throw new DataNotFoundException("Customer not found with ID: " + id);
    }

    @Override
    public Customer findByID(long id) {
        int index = this.findIndexById(id);
        return this.list.get(index);
    }
}
