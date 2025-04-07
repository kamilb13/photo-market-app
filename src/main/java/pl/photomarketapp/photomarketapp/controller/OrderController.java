package pl.photomarketapp.photomarketapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.photomarketapp.photomarketapp.model.Order;
import pl.photomarketapp.photomarketapp.repository.OrderRepository;

import java.util.List;

@RestController
public class OrderController {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @PostMapping("/add-order")
    public Order addOrder(@RequestBody Order order) {
        return orderRepository.save(order);
    }

    @GetMapping("/get-all-orders")
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
