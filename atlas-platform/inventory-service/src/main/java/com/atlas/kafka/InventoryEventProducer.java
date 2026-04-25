package com.atlas.kafka;

import java.util.logging.Logger;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.atlas.events.InventoryReservationFailedEvent;
import com.atlas.events.InventoryReservedEvent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryEventProducer {
    
    public static final Logger log = Logger.getLogger(InventoryEventProducer.class.getName());
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishReserved(InventoryReservedEvent event) {
        kafkaTemplate.send("inventory-reserved", event.getOrderId(), event);
        log.info("Inventory reserved event published: " + event.getOrderId());
    }

    public void publishFailed(InventoryReservationFailedEvent event) {
        kafkaTemplate.send("inventory-reservation-failed", event.getOrderId(), event);
        log.info("Inventory reservation failed event published: " + event.getOrderId());
    }
}
