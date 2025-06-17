package pl.photomarketapp.photomarketapp.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RestController;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.web.bind.annotation.*;
import pl.photomarketapp.photomarketapp.model.Order;
import pl.photomarketapp.photomarketapp.repository.OrderRepository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class PaymentController {
    private final OrderRepository orderRepository;

    public PaymentController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
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
        Order order = new Order(new Date(), request.getProductId(), session.getId());
        orderRepository.save(order);
        Map<String, Object> response = new HashMap<>();
        response.put("id", session.getId());
        return response;
    }

    @Getter
    @Setter
    public static class ProductRequest {
        private Long productId;
        private String productName;
        private Long productPrice;
    }
}
