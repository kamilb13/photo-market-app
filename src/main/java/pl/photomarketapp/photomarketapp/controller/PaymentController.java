package pl.photomarketapp.photomarketapp.controller;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.net.Webhook;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.web.bind.annotation.*;
import pl.photomarketapp.photomarketapp.enums.PaymentStatus;
import pl.photomarketapp.photomarketapp.model.Order;
import pl.photomarketapp.photomarketapp.model.Payment;
import pl.photomarketapp.photomarketapp.repository.OrderRepository;
import pl.photomarketapp.photomarketapp.repository.PaymentRepository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// TODO zrobic porzadek

@RestController
public class PaymentController {
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    public PaymentController(OrderRepository orderRepository, PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
    }

    @PostMapping("/create-checkout-session")
    public Map<String, Object> createCheckoutSession(@RequestBody ProductRequest request) throws StripeException {
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:4200/success?idPhoto=" + request.getProductId())
                .setCancelUrl("http://localhost:4200/cancel")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("pln")
                                                .setUnitAmount(request.getProductPrice())
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName(request.getProductName())
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .build();

        Session session = Session.create(params);
        String sessionId = session.getId();
        Order order = new Order(new Date(), request.getProductId(), sessionId);
        orderRepository.save(order);
        
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount((double) (request.getProductPrice() / 100));
        payment.setStatus(PaymentStatus.PENDING);
        payment.setSessionId(sessionId);
        paymentRepository.save(payment);
        
        Map<String, Object> response = new HashMap<>();
        response.put("id", session.getId());
        return response;
    }

    @PostMapping("/admin/payments/{id}/approve-offline")
    public ResponseEntity<?> approveOfflinePayment(@PathVariable Long id) {
        Payment payment = paymentRepository.findById(id).orElseThrow();
        payment.setStatus(PaymentStatus.OFFLINE_APPROVED);
        paymentRepository.save(payment);
        return ResponseEntity.ok().build();
    }
    
    @Getter
    @Setter
    public static class ProductRequest {
        private Long productId;
        private String productName;
        private Long productPrice;
    }
}
    
