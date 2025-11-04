package com.ecommerce.agriconnectke.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.agriconnectke.models.Order;
import com.ecommerce.agriconnectke.services.OrderService;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody Order order) {
        try {
            Order createdOrder = orderService.createOrder(order);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<Order>> getBuyerOrders(@PathVariable Long buyerId) {
        List<Order> orders = orderService.getBuyerOrders(buyerId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/farmer/{farmerId}")
    public ResponseEntity<List<Order>> getFarmerOrders(@PathVariable Long farmerId) {
        List<Order> orders = orderService.getFarmerOrders(farmerId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/farmer/{farmerId}/sales")
    public ResponseEntity<List<Order>> getFarmerSales(@PathVariable Long farmerId) {
        List<Order> sales = orderService.getFarmerSales(farmerId);
        return ResponseEntity.ok(sales);
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long orderId, @RequestParam String status) {
        try {
            Order updatedOrder = orderService.updateOrderStatus(orderId, status);
            return ResponseEntity.ok(updatedOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable Long orderId) {
        try {
            Order order = orderService.getOrderById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found"));
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/buyer/{buyerId}/status/{status}")
    public ResponseEntity<List<Order>> getBuyerOrdersByStatus(
            @PathVariable Long buyerId, @PathVariable String status) {
        List<Order> orders = orderService.getBuyerOrdersByStatus(buyerId, status);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/farmer/{farmerId}/status/{status}")
    public ResponseEntity<List<Order>> getFarmerOrdersByStatus(
            @PathVariable Long farmerId, @PathVariable String status) {
        List<Order> orders = orderService.getFarmerOrdersByStatus(farmerId, status);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long orderId) {
        try {
            Order cancelledOrder = orderService.cancelOrder(orderId);
            return ResponseEntity.ok(cancelledOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/farmer/{farmerId}/stats/sales")
    public ResponseEntity<?> getFarmerSalesStats(@PathVariable Long farmerId) {
        try {
            Double totalSales = orderService.getFarmerTotalSales(farmerId);
            Long orderCount = orderService.getFarmerOrderCount(farmerId);
            
            var stats = new SalesStats(totalSales != null ? totalSales : 0.0, orderCount);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/recent")
    public ResponseEntity<List<Order>> getRecentOrders(@RequestParam(defaultValue = "7") int days) {
        List<Order> orders = orderService.getRecentOrders(days);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/buyer/{buyerId}/order/{orderId}")
    public ResponseEntity<?> getOrderForBuyer(
            @PathVariable Long buyerId, @PathVariable Long orderId) {
        try {
            Order order = orderService.getOrderForBuyer(orderId, buyerId)
                    .orElseThrow(() -> new RuntimeException("Order not found"));
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/farmer/{farmerId}/order/{orderId}")
    public ResponseEntity<?> getOrderForFarmer(
            @PathVariable Long farmerId, @PathVariable Long orderId) {
        try {
            Order order = orderService.getOrderForFarmer(orderId, farmerId)
                    .orElseThrow(() -> new RuntimeException("Order not found"));
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/cleanup-abandoned")
    public ResponseEntity<String> cleanupAbandonedOrders() {
        try {
            orderService.cleanupAbandonedOrders();
            return ResponseEntity.ok("Abandoned orders cleaned up successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error cleaning up abandoned orders: " + e.getMessage());
        }
    }

    // DTO for sales stats
    private static class SalesStats {
        private final Double totalSales;
        private final Long orderCount;

        public SalesStats(Double totalSales, Long orderCount) {
            this.totalSales = totalSales;
            this.orderCount = orderCount;
        }

        public Double getTotalSales() { return totalSales; }
        public Long getOrderCount() { return orderCount; }
    }
}