package com.atlas;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.atlas.dto.OrderLineItemsDto;
import com.atlas.dto.OrderRequest;
import com.atlas.dto.OrderResponse;
import com.atlas.events.OrderCreatedEvent;
import com.atlas.kafka.OrderEventProducer;
import com.atlas.model.OrderLineItems;
import com.atlas.model.OrderModel;
import com.atlas.repository.OrderRepository;
import com.atlas.service.OrderService;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Collections;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderEventProducer orderEventProducer;

    @Mock
    private WebClient.Builder webClientBuilder;

    @InjectMocks
    private OrderService orderService;

    @Test
    @SuppressWarnings("unchecked")
    void shouldPlaceOrderSuccessfully() {
        // Arrange
        OrderLineItemsDto itemDto = new OrderLineItemsDto("PROD-1", "SKU-1", new BigDecimal("100.00"), 2);
        OrderRequest request = new OrderRequest("USER-1", Collections.singletonList(itemDto));
        
        OrderLineItems item = new OrderLineItems(1L, "SKU-1", new BigDecimal("100.00"), 2);
        OrderModel savedOrder = new OrderModel(1L, "mock-order-id", "USER-1", Collections.singletonList(item), "PENDING");

        // Mock WebClient chain
        WebClient webClient = mock(WebClient.class);
        WebClient.RequestHeadersUriSpec requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Object.class)).thenReturn(Mono.just(new Object()));
        
        when(orderRepository.save(any(OrderModel.class))).thenReturn(savedOrder);

        // Act
        OrderResponse result = orderService.placeOrder(request);

        // Assert
        assertNotNull(result.getOrderId());
        assertEquals("USER-1", result.getUserId());
        assertEquals("PENDING", result.getOrderStatus());
        assertEquals(1, result.getOrderLineItemsList().size());
        
        verify(orderRepository, times(1)).save(any(OrderModel.class));
        verify(orderEventProducer, times(1)).publishOrderCreatedEvent(any(OrderCreatedEvent.class));
    }
}
