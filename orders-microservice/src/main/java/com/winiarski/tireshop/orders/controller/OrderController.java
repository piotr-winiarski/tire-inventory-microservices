package com.winiarski.tireshop.orders.controller;

import com.winiarski.tireshop.orders.model.OrderRequest;
import com.winiarski.tireshop.orders.service.OrdersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrdersService ordersService;

    @GetMapping
    public ResponseEntity<List<OrderRequest>> getAllOrders() {
        return ResponseEntity.ok(ordersService.getAllOrders());
    }

    @GetMapping("{orderId}")
    public ResponseEntity<OrderRequest> getOrderById(@PathVariable Long orderId) {
        return ResponseEntity.ok(ordersService.getOrderById(orderId));
    }

    @PostMapping
    public ResponseEntity<OrderRequest> placeOrder(@Valid @RequestBody OrderRequest order) {
        OrderRequest savedOrder = ordersService.placeOrder(order);
        return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
    }

}
