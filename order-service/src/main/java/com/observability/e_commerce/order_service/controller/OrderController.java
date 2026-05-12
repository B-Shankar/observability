package com.observability.e_commerce.order_service.controller;

import com.observability.e_commerce.order_service.entity.Order;
import com.observability.e_commerce.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    @PostMapping
    public Order create(@RequestBody Order order) {
        return service.create(order);
    }

    @GetMapping("/{id}")
    public Order get(@PathVariable Long id) {
        return service.getById(id);
    }
}
