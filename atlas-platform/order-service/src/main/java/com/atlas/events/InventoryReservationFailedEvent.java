package com.atlas.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryReservationFailedEvent {
    private String orderId;
    private String productId;
    private int quantity;
    private String reason;
}
