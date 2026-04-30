package com.atlas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private String orderId;
    private String userId;
    private String orderStatus;
    private List<OrderLineItemsResponse> orderLineItemsList;
}
