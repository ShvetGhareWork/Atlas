package com.atlas.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atlas.dto.OrderRequest;
import com.atlas.events.OrderCreatedEvent;
import com.atlas.kafka.OrderEventProducer;
import com.atlas.model.OrderModel;
import com.atlas.repository.OrderRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderEventProducer orderEventProducer;

    public OrderModel placeOrder(OrderRequest request) {
        OrderModel order = new OrderModel();
        order.setOrderId(UUID.randomUUID().toString());
        order.setUserId(request.getUserId());
        order.setProductId(request.getProductId());
        order.setQuantity(request.getQuantity());
        order.setOrderStatus("PENDING");
        
        // Save to DB
        order = orderRepository.save(order);
        log.info("Order saved to DB: {}", order.getOrderId());

        // Publish to Kafka
        OrderCreatedEvent event = new OrderCreatedEvent(
            order.getOrderId(),
            order.getUserId(),
            order.getProductId(),
            order.getQuantity(),
            order.getOrderStatus()
        );
        orderEventProducer.publishOrderCreatedEvent(event);

        return order;
    }

    public Optional<OrderModel> getOrderById(String orderId) {
        return orderRepository.findById(orderId);
    }

    public void upateOrderStatus(String orderId, String status){
        Optional<OrderModel> orderOptional = orderRepository.findById(orderId);
        if(orderOptional.isPresent()){
            OrderModel order = orderOptional.get();
            order.setOrderStatus(status);
            orderRepository.save(order);
            log.info("Order status updated to {}: {}", status, orderId);
        }
    }
}
