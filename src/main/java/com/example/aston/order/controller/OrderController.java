package com.example.aston.order.controller;

import com.example.aston.exception.NotFoundException;
import com.example.aston.order.dto.input.CreateOrderInput;
import com.example.aston.order.dto.input.UpdateOrderInput;
import com.example.aston.order.dto.output.OrderOutput;
import com.example.aston.order.service.OrderServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderServiceInterface orderService;

    @GetMapping
    public Collection<OrderOutput> findAll() {
        return orderService.findAll();
    }

    @GetMapping("/{id}")
    public OrderOutput findById(@PathVariable("id") Integer id) throws NotFoundException {
        return orderService.findById(id);
    }

    @PostMapping
    public OrderOutput save(@RequestBody CreateOrderInput dto) throws NotFoundException {
        return orderService.save(dto);
    }

    @PatchMapping("/{id}")
    public OrderOutput update(@RequestBody UpdateOrderInput dto, @PathVariable("id") Integer id) throws NotFoundException {
        return orderService.update(dto, id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Integer id) throws NotFoundException {
        orderService.deleteById(id);
    }
}