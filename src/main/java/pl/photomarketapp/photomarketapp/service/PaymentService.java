package pl.photomarketapp.photomarketapp.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.photomarketapp.photomarketapp.enums.PaymentStatus;
import pl.photomarketapp.photomarketapp.model.Order;
import pl.photomarketapp.photomarketapp.model.Payment;
import pl.photomarketapp.photomarketapp.repository.OrderRepository;
import pl.photomarketapp.photomarketapp.repository.PaymentRepository;

import java.util.List;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentService(PaymentRepository paymentRepository, OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public void updatePaymentStatus(Payment payment, PaymentStatus newStatus) {
        payment.setStatus(newStatus);
        paymentRepository.save(payment);

        Order order = payment.getOrder();
        List<Payment> payments = order.getPayments();

        boolean allPaid = payments.stream().allMatch(p -> p.getStatus() == PaymentStatus.PAID);
        boolean anyFailed = payments.stream().anyMatch(p -> p.getStatus() == PaymentStatus.FAILED);

        if (allPaid) {
            order.setPaymentStatus(PaymentStatus.PAID);
        } else if (anyFailed) {
            order.setPaymentStatus(PaymentStatus.FAILED);
        } else {
            order.setPaymentStatus(PaymentStatus.PENDING);
        }

        orderRepository.save(order);
    }
}
