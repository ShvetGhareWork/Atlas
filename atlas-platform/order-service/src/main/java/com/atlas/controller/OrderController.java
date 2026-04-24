package com.atlas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.atlas.dto.OrderRequest;
import com.atlas.model.OrderModel;
import com.atlas.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderModel> placeOrder(@RequestBody OrderRequest request) {
        OrderModel order = orderService.placeOrder(request);
        return ResponseEntity.accepted().body(order);
    }
}