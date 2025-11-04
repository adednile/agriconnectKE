package com.ecommerce.agriconnectke.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.agriconnectke.exceptions.InsufficientQuantityException;
import com.ecommerce.agriconnectke.exceptions.InvalidOrderException;
import com.ecommerce.agriconnectke.exceptions.OrderNotFoundException;
import com.ecommerce.agriconnectke.models.Delivery;
import com.ecommerce.agriconnectke.models.Listing;
import com.ecommerce.agriconnectke.models.Order;
import com.ecommerce.agriconnectke.repositories.ListingRepository;
import com.ecommerce.agriconnectke.repositories.OrderRepository;

@Service
public class OrderService {
    
    public Order getById(long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }
    
    public List<Order> getByBuyer(long buyerId) {
        return orderRepository.findByBuyerId(buyerId);
    }
    
    private final OrderRepository orderRepository;
    private final ListingRepository listingRepository;
    private final PaymentService paymentService;
    private final DeliveryService deliveryService;
    private final ListingService listingService;

    public OrderService(OrderRepository orderRepository, 
                       ListingRepository listingRepository,
                       PaymentService paymentService,
                       DeliveryService deliveryService,
                       ListingService listingService) {
        this.orderRepository = orderRepository;
        this.listingRepository = listingRepository;
        this.paymentService = paymentService;
        this.deliveryService = deliveryService;
        this.listingService = listingService;
    }

    @Transactional
    public Order createOrder(Order order) {
        // Validate order data
        validateOrder(order);
        
        // Verify listing exists and has sufficient quantity
        Listing listing = listingRepository.findById(order.getListingId())
                .orElseThrow(() -> new InvalidOrderException("Listing not found"));
        
        if (!"OPEN".equals(listing.getStatus())) {
            throw new InvalidOrderException("Listing is not available for purchase");
        }
        
        if (listing.getQuantity() < order.getQuantity()) {
            throw new InsufficientQuantityException(
                listing.getCropName(), listing.getQuantity(), order.getQuantity());
        }
        
        // Set unit price from listing
        order.setUnitPrice(listing.getPrice());
        
        // Save order
        Order savedOrder = orderRepository.save(order);
        
        // Reduce listing quantity
        listingService.reduceListingQuantity(listing.getListingId(), order.getQuantity());
        
        return savedOrder;
    }

    public List<Order> getBuyerOrders(Long buyerId) {
        return orderRepository.findByBuyerId(buyerId);
    }

    public List<Order> getFarmerOrders(Long farmerId) {
        return orderRepository.findByFarmerId(farmerId);
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        
        // Validate status transition
        validateStatusTransition(order.getOrderStatus(), status);
        
        order.setOrderStatus(status);
        
        // If order is paid, create delivery
        if ("PAID".equals(status)) {
            createDeliveryForOrder(order);
        }
        
        return orderRepository.save(order);
    }

    public Optional<Order> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    public List<Order> getFarmerSales(Long farmerId) {
        return orderRepository.findCompletedOrdersByFarmer(farmerId);
    }

    public List<Order> getBuyerOrdersByStatus(Long buyerId, String status) {
        return orderRepository.findByBuyerIdAndOrderStatus(buyerId, status);
    }

    public List<Order> getFarmerOrdersByStatus(Long farmerId, String status) {
        return orderRepository.findByFarmerIdAndOrderStatus(farmerId, status);
    }

    public Order cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        
        if (!"PENDING".equals(order.getOrderStatus()) && !"PAID".equals(order.getOrderStatus())) {
            throw new InvalidOrderException("Only pending or paid orders can be cancelled");
        }
        
        // Restore listing quantity if order was paid
        if ("PAID".equals(order.getOrderStatus())) {
            listingService.updateListingQuantity(
                order.getListingId(), 
                listingRepository.findById(order.getListingId())
                    .map(Listing::getQuantity)
                    .orElse(0) + order.getQuantity()
            );
            
            // Process refund if payment was made
            paymentService.refundPayment(orderId);
        }
        
        order.setOrderStatus("CANCELLED");
        
        return orderRepository.save(order);
    }

    public Double getFarmerTotalSales(Long farmerId) {
        return orderRepository.getTotalSalesByFarmer(farmerId);
    }

    public Long getFarmerOrderCount(Long farmerId) {
        return orderRepository.countCompletedOrdersByFarmer(farmerId);
    }

    public List<Order> getRecentOrders(int days) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        LocalDateTime endDate = LocalDateTime.now();
        return orderRepository.findByCreatedAtBetween(startDate, endDate);
    }

    public Optional<Order> getOrderForBuyer(Long orderId, Long buyerId) {
        return orderRepository.findByOrderIdAndBuyerId(orderId, buyerId);
    }

    public Optional<Order> getOrderForFarmer(Long orderId, Long farmerId) {
        return orderRepository.findByOrderIdAndFarmerId(orderId, farmerId);
    }

    public void cleanupAbandonedOrders() {
        LocalDateTime cutoffTime = LocalDateTime.now().minusHours(24); // 24 hours old
        List<Order> abandonedOrders = orderRepository.findAbandonedOrders(cutoffTime);
        
        for (Order order : abandonedOrders) {
            order.setOrderStatus("CANCELLED");
            orderRepository.save(order);
            
            // Restore listing quantity
            listingService.updateListingQuantity(
                order.getListingId(), 
                listingRepository.findById(order.getListingId())
                    .map(Listing::getQuantity)
                    .orElse(0) + order.getQuantity()
            );
        }
    }

    private void validateOrder(Order order) {
        if (order.getListingId() == null) {
            throw new InvalidOrderException("Listing ID is required");
        }
        if (order.getBuyerId() == null) {
            throw new InvalidOrderException("Buyer ID is required");
        }
        if (order.getFarmerId() == null) {
            throw new InvalidOrderException("Farmer ID is required");
        }
        if (order.getQuantity() == null || order.getQuantity() <= 0) {
            throw new InvalidOrderException("Quantity must be positive");
        }
        // Use BigDecimal comparison for unitPrice validation
        if (order.getUnitPrice() == null || order.getUnitPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidOrderException("Unit price must be positive");
        }
    }

    private void validateStatusTransition(String currentStatus, String newStatus) {
        switch (currentStatus) {
            case "PENDING":
                if (!"PAID".equals(newStatus) && !"CANCELLED".equals(newStatus)) {
                    throw new InvalidOrderException("Pending orders can only be paid or cancelled");
                }
                break;
            case "PAID":
                if (!"ASSIGNED".equals(newStatus) && !"CANCELLED".equals(newStatus)) {
                    throw new InvalidOrderException("Paid orders can only be assigned or cancelled");
                }
                break;
            case "ASSIGNED":
                if (!"IN_TRANSIT".equals(newStatus) && !"CANCELLED".equals(newStatus)) {
                    throw new InvalidOrderException("Assigned orders can only be in transit or cancelled");
                }
                break;
            case "IN_TRANSIT":
                if (!"DELIVERED".equals(newStatus) && !"CANCELLED".equals(newStatus)) {
                    throw new InvalidOrderException("Orders in transit can only be delivered or cancelled");
                }
                break;
            case "DELIVERED":
            case "CANCELLED":
            case "REFUNDED":
                throw new InvalidOrderException("Cannot change status of completed or cancelled order");
            default:
                throw new InvalidOrderException("Invalid current status: " + currentStatus);
        }
    }

    private void createDeliveryForOrder(Order order) {
        // This would typically integrate with your delivery service
        // For now, we'll create a basic delivery record
        try {
            Delivery delivery = new Delivery();
            delivery.setOrderId(order.getOrderId());
            // Set pickup and dropoff coordinates based on farmer and buyer locations
            // This would require additional user location data
            delivery.setStatus("PENDING");
            deliveryService.createDelivery(delivery);
        } catch (Exception e) {
            // Log error but don't fail the order status update
            System.err.println("Failed to create delivery for order: " + order.getOrderId());
        }
    }
}