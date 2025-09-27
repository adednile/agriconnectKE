package com.ecommerce.agriconnectke.controllers;

import com.ecommerce.agriconnectke.models.Order;
import com.ecommerce.agriconnectke.services.OrderService;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderservice) {
        this.orderService = orderservice;
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        return orderService.createOrder(order);
    }
    
}
