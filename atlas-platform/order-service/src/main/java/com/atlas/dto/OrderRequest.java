package com.atlas.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private String userId;
    @NotEmpty(message = "Order must contain at least one item")
    @Valid
    private List<OrderLineItemsDto> orderLineItemsDtoList;
}