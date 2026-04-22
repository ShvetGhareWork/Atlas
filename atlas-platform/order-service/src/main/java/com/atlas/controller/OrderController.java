package com.atlas.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders") // This MUST match the Gateway Path
public class OrderController {

    @GetMapping
    public String getOrders() {
        return "Order service is reached! Here are your orders (from the DB soon).";
    }
}
