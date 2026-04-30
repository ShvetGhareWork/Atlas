package com.atlas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.atlas.dto.OrderRequest;
import com.atlas.events.OrderCreatedEvent;
import com.atlas.kafka.OrderEventProducer;
import com.atlas.model.OrderModel;
import com.atlas.repository.OrderRepository;
import com.atlas.service.OrderService;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderEventProducer orderEventProducer;

    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldPlaceOrderSuccessfully() {
        // Arrange
        OrderRequest request = new OrderRequest("USER-1", "PROD-001", 2);
        OrderModel savedOrder = new OrderModel("mock-order-id", "USER-1", "PROD-001", 2, "PENDING");
        
        when(orderRepository.save(any(OrderModel.class))).thenReturn(savedOrder);

        // Act
        OrderModel result = orderService.placeOrder(request);

        // Assert
        assertNotNull(result.getOrderId());
        assertEquals("PROD-001", result.getProductId());
        assertEquals("PENDING", result.getOrderStatus());
        
        verify(orderRepository, times(1)).save(any(OrderModel.class));
        verify(orderEventProducer, times(1)).publishOrderCreatedEvent(any(OrderCreatedEvent.class));
    }
}
