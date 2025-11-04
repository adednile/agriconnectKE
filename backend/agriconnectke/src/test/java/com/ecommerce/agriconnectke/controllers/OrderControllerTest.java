package com.ecommerce.agriconnectke.controllers;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ecommerce.agriconnectke.exceptions.OrderNotFoundException;
import com.ecommerce.agriconnectke.models.Order;
import com.ecommerce.agriconnectke.services.OrderService;

@WebMvcTest(controllers = OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Test
    void getById_returnsOk() throws Exception {
        Order order = new Order();
        when(orderService.getById(1L)).thenReturn(order);

        mockMvc.perform(get("/api/orders/1")).andExpect(status().isOk());
        verify(orderService).getById(1L);
    }

    @Test
    void getById_notFound() throws Exception {
        when(orderService.getById(2L)).thenThrow(new OrderNotFoundException(2L));

        mockMvc.perform(get("/api/orders/2")).andExpect(status().isNotFound());
        verify(orderService).getById(2L);
    }
}
