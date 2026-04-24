package com.atlas.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.atlas.events.OrderCreatedEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class OrderEventProducer {
    private static final Logger log = LoggerFactory.getLogger(OrderEventProducer.class);
    private static final String TOPIC = "order-created";

    @Autowired
    private KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    public void publishOrderCreatedEvent(OrderCreatedEvent event){
        kafkaTemplate.send(TOPIC, event.getOrderId(), event)
        .whenComplete((result, ex) ->{
            if(ex == null){
                log.info("Order Created Event published successfully: {}", event.getOrderId());
            }else{
                log.error("Order Created Event failed to publish: {}", ex.getMessage());
            }
        });
    }
}
