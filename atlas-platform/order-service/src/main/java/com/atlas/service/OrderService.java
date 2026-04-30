package com.atlas.service;

import com.atlas.dto.OrderLineItemsDto;
import com.atlas.dto.OrderLineItemsResponse;
import com.atlas.dto.OrderRequest;
import com.atlas.dto.OrderResponse;
import com.atlas.events.OrderCreatedEvent;
import com.atlas.kafka.OrderEventProducer;
import com.atlas.model.OrderLineItems;
import com.atlas.model.OrderModel;
import com.atlas.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderEventProducer orderEventProducer;

    public OrderResponse placeOrder(OrderRequest request) {
        OrderModel order = new OrderModel();
        order.setOrderId(UUID.randomUUID().toString());
        order.setUserId(request.getUserId());

        List<OrderLineItems> orderLineItems = request.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToEntity)
                .collect(Collectors.toList());

        order.setOrderLineItemsList(orderLineItems);
        order.setOrderStatus("PENDING");

        OrderModel savedOrder = orderRepository.save(order);
        log.info("Order saved to DB: {}", savedOrder.getOrderId());

        // For now, we still send a simplified event for the first item to avoid breaking downstream
        if (!orderLineItems.isEmpty()) {
            OrderLineItems firstItem = orderLineItems.get(0);
            OrderCreatedEvent event = new OrderCreatedEvent(
                    savedOrder.getOrderId(),
                    savedOrder.getUserId(),
                    firstItem.getSkuCode(),
                    firstItem.getQuantity(),
                    savedOrder.getOrderStatus()
            );
            orderEventProducer.publishOrderCreatedEvent(event);
        }

        return mapToResponse(savedOrder);
    }

    @Transactional(readOnly = true)
    public Optional<OrderResponse> getOrderById(String orderId) {
        return orderRepository.findByOrderId(orderId)
                .map(this::mapToResponse);
    }

    private OrderLineItems mapToEntity(OrderLineItemsDto dto) {
        OrderLineItems entity = new OrderLineItems();
        entity.setPrice(dto.getPrice());
        entity.setQuantity(dto.getQuantity());
        entity.setSkuCode(dto.getSkuCode());
        return entity;
    }

    private OrderResponse mapToResponse(OrderModel model) {
        OrderResponse response = new OrderResponse();
        response.setOrderId(model.getOrderId());
        response.setUserId(model.getUserId());
        response.setOrderStatus(model.getOrderStatus());
        response.setOrderLineItemsList(model.getOrderLineItemsList()
                .stream()
                .map(this::mapToLineItemResponse)
                .collect(Collectors.toList()));
        return response;
    }

    private OrderLineItemsResponse mapToLineItemResponse(OrderLineItems entity) {
        OrderLineItemsResponse response = new OrderLineItemsResponse();
        response.setId(entity.getId());
        response.setSkuCode(entity.getSkuCode());
        response.setPrice(entity.getPrice());
        response.setQuantity(entity.getQuantity());
        return response;
    }
}
