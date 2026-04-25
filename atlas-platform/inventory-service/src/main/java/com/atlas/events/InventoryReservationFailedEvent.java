package com.atlas.events;

public class InventoryReservationFailedEvent {
    private String orderId;
    private String productId;
    private String reason;

    public InventoryReservationFailedEvent() {}

    public InventoryReservationFailedEvent(String orderId, String productId, String reason) {
        this.orderId = orderId;
        this.productId = productId;
        this.reason = reason;
    }

    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    public String getProductId() {
        return productId;
    }
    public void setProductId(String productId) {
        this.productId = productId;
    }
    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }   
}
