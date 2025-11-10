package com.ecommerce.agriconnectke.controllers;
import com.ecommerce.agriconnectke.models.Order;
import com.ecommerce.agriconnectke.services.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins="*")
public class OrderController {
  private final OrderService orderService;
  public OrderController(OrderService orderService){ this.orderService = orderService; }

  @PostMapping
  public Order createOrder(@RequestBody Order order){
      // create order row; deliver_fee null for now
      return orderService.createOrder(order);
  }

  @GetMapping("/{id}")
  public Order get(@PathVariable Long id){
      // TODO: replace with orderService.getById(id) after adding that method to OrderService
      return null;
  }

  @GetMapping("/buyer/{buyerId}")
  public List<Order> byBuyer(@PathVariable Long buyerId){ return orderService.getByBuyer(buyerId); }
}