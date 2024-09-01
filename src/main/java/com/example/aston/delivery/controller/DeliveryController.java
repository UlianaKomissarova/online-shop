package com.example.aston.delivery.controller;

import com.example.aston.delivery.dto.input.DeliveryInput;
import com.example.aston.delivery.dto.output.DeliveryOutput;
import com.example.aston.delivery.service.DeliveryServiceInterface;
import com.example.aston.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/deliveries")
@RequiredArgsConstructor
public class DeliveryController {
    private final DeliveryServiceInterface deliveryService;

    @GetMapping
    public Collection<DeliveryOutput> findAll(@RequestParam(name = "type", defaultValue = "all") String type) {
        return deliveryService.findAll(type);
    }

    @GetMapping("/{id}")
    public DeliveryOutput findById(@PathVariable("id") Integer id) throws NotFoundException {
        return deliveryService.findById(id);
    }

    @PostMapping
    public DeliveryOutput save(@RequestBody DeliveryInput dto) throws NotFoundException {
        return deliveryService.save(dto);
    }

    @PatchMapping("/{id}")
    public DeliveryOutput update(@RequestBody DeliveryInput dto,
        @PathVariable("id") Integer id) throws NotFoundException {

        return deliveryService.update(dto, id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable("id") Integer id) throws NotFoundException {
        deliveryService.deleteById(id);
    }
}