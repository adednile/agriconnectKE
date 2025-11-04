package com.ecommerce.agriconnectke.services;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ecommerce.agriconnectke.exceptions.OrderNotFoundException;
import com.ecommerce.agriconnectke.models.Order;
import com.ecommerce.agriconnectke.repositories.OrderRepository;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void getById_returnsOrderWhenFound() {
        Order order = new Order();
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order result = orderService.getById(1L);

        assertSame(order, result);
        verify(orderRepository).findById(1L);
    }

    @Test
    void getById_throwsWhenNotFound() {
        when(orderRepository.findById(2L)).thenReturn(Optional.empty());

    assertThrows(OrderNotFoundException.class, () -> orderService.getById(2L));
        verify(orderRepository).findById(2L);
    }

    @Test
    void getByBuyer_returnsList() {
        Order o1 = new Order();
        Order o2 = new Order();
        List<Order> list = List.of(o1, o2);
        when(orderRepository.findByBuyerId(5L)).thenReturn(list);

        List<Order> result = orderService.getByBuyer(5L);

        assertEquals(2, result.size());
        assertSame(o1, result.get(0));
        assertSame(o2, result.get(1));
        verify(orderRepository).findByBuyerId(5L);
    }
}
