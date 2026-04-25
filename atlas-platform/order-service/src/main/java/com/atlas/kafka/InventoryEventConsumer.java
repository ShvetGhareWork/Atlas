package com.atlas.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.atlas.events.InventoryReservationFailedEvent;
import com.atlas.events.InventoryReservedEvent;
import com.atlas.repository.OrderRepository;
import com.atlas.model.OrderModel;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class InventoryEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(InventoryEventConsumer.class);
    private final OrderRepository orderRepository;

    @KafkaListener(topics = "inventory-reserved", groupId = "order-service-group")
    public void handleInventoryReserved(InventoryReservedEvent event) {
        log.info("Inventory reserved for orderId={}", event.getOrderId());
        orderRepository.findById(event.getOrderId()).ifPresent(order -> {
            order.setOrderStatus("CONFIRMED");
            orderRepository.save(order);
            log.info("Order {} status updated to CONFIRMED", event.getOrderId());
        });
    }

    

    @KafkaListener(topics = "inventory-reservation-failed", groupId = "order-service-group")
    public void handleInventoryFailure(InventoryReservationFailedEvent event) {
        log.error("Received Inventory Failure for Order {}: {}", event.getOrderId(), event.getReason());
        
        orderRepository.findById(event.getOrderId()).ifPresent(order -> {
            order.setOrderStatus("FAILED");
            orderRepository.save(order);
            log.info("Order {} status updated to FAILED", event.getOrderId());
        });
    }
}
