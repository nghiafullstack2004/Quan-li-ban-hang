package service;

import entity.Payment;
import lib.errors.DataNotFoundException;
import repository.PaymentRepository;

import java.util.ArrayList;
import java.util.List;

public class PaymentService implements IService<Payment> {
    private final PaymentRepository paymentRepository = new PaymentRepository();
    private final List<Payment> list;
    private static final PaymentService paymentService = new PaymentService();

    private PaymentService() {
        this.list = paymentRepository.read();
    }

    public static PaymentService getInstance() {
        return paymentService;
    }

    public static PaymentService getObject() {
        return paymentService;
    }

    public List<Payment> findByOrderId(long orderId) {
        List<Payment> listFound = new ArrayList<>();
        for (Payment item : list) {
            if (item.getOrderId() == orderId) {
                listFound.add(item);
            }
        }
        return listFound;
    }

    @Override
    public void add(Payment newPayment) {
        this.list.add(newPayment);
        this.paymentRepository.write(this.list);
    }

    @Override
    public void delete(long id) {
        int index = findIndexById(id);
        this.list.remove(index);
        this.paymentRepository.write(this.list);
    }

    @Override
    public void update(long id, Payment payment) {
        int index = this.findIndexById(id);
        this.list.set(index, payment);
        this.paymentRepository.write(this.list);
    }

    @Override
    public List<Payment> getAll() {
        return this.list;
    }

    @Override
    public int findIndexById(long id) {
        for (int i = 0; i < this.list.size(); i++) {
            Payment payment = this.list.get(i);
            if (payment.getPaymentId() == id) {
                return i;
            }
        }
        throw new DataNotFoundException("Payment not found with ID: " + id);
    }

    @Override
    public Payment findByID(long id) {
        int index = this.findIndexById(id);
        return this.list.get(index);
    }
}
