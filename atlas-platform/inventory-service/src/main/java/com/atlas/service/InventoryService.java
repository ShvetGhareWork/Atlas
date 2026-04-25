package com.atlas.service;

import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import com.atlas.events.InventoryReservedEvent;
import com.atlas.events.InventoryReservationFailedEvent;
import com.atlas.events.OrderCreatedEvent;
import com.atlas.kafka.InventoryEventProducer;
import com.atlas.model.InventoryModel;
import com.atlas.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryEventProducer eventProducer;

    @CachePut(value = "inventory", key = "#inventory.productId")
    public InventoryModel addInventory(InventoryModel inventory) {
        return inventoryRepository.save(inventory);
    }

    @Cacheable(value = "inventory", key = "#productId")
    public InventoryModel checkStock(String productId) {
        log.info("Checking stock for productId: [{}]", productId);
        return inventoryRepository.findByProductIdIgnoreCase(productId.trim())
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));
    }

    @CacheEvict(value = "inventory", key = "#event.productId")
    public void processOrder(OrderCreatedEvent event) {
    try {
        Optional<InventoryModel> optional = inventoryRepository.findByProductIdIgnoreCase(event.getProductId());

        if (optional.isEmpty() || optional.get().getQuantity() < event.getQuantity()) {
            eventProducer.publishFailed(new InventoryReservationFailedEvent(
                event.getOrderId(),
                event.getProductId(),
                "Insufficient stock"
            ));
            return;
        }

        InventoryModel inventory = optional.get();
        inventory.setQuantity(inventory.getQuantity() - event.getQuantity());
        inventoryRepository.save(inventory);

        eventProducer.publishReserved(new InventoryReservedEvent(
            event.getOrderId(),
            event.getUserId(),
            event.getProductId(),
            event.getQuantity(),
            "RESERVED"
        ));
        log.info("Inventory reserved for orderId={}", event.getOrderId());
    } catch (OptimisticLockingFailureException e) {
        log.warn("Concurrent update detected for orderId={}, publishing failure", event.getOrderId());
        eventProducer.publishFailed(new InventoryReservationFailedEvent(
            event.getOrderId(),
            event.getProductId(),
            "Concurrent update conflict, please retry"
        ));
    }
 
}
}