package pl.photomarketapp.photomarketapp.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pl.photomarketapp.photomarketapp.enums.PaymentStatus;
import pl.photomarketapp.photomarketapp.model.Order;
import pl.photomarketapp.photomarketapp.model.Payment;
import pl.photomarketapp.photomarketapp.repository.OrderRepository;
import pl.photomarketapp.photomarketapp.repository.PaymentRepository;
import pl.photomarketapp.photomarketapp.service.PaymentService;

import java.util.Optional;

@Slf4j
@RestController
public class StripeWebhookController {
    private final PaymentRepository paymentRepository;
    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    private final OrderRepository orderRepository;
    private final PaymentService paymentService;

    public StripeWebhookController(OrderRepository orderRepository, PaymentService paymentService, PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.paymentService = paymentService;
        this.paymentRepository = paymentRepository;
    }

    @PostMapping("/stripe/webhook")
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        }

        if ("checkout.session.completed".equals(event.getType())) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode json = mapper.readTree(payload);
                String sessionId = json.at("/data/object/id").asText();
                log.info("Handling session: {}", sessionId);
                Optional<Order> optionalOrder = orderRepository.findBySessionId(sessionId);

                if (optionalOrder.isPresent()) {
                    Order order = optionalOrder.get();
                    Payment payment = paymentRepository.findBySessionId(sessionId).get();
                    payment.setOrder(order);
                    paymentService.updatePaymentStatus(payment, PaymentStatus.PAID);
                } else {
                    log.warn("Order not found for session ID: {}", sessionId);
                }
            } catch (Exception ex) {
                log.error("Failed to parse raw payload", ex);
            }
        }
        return ResponseEntity.ok("Webhook received");
    }
}
