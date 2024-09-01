package com.example.aston.order.service;

import com.example.aston.exception.NotFoundException;
import com.example.aston.order.dto.input.CreateOrderInput;
import com.example.aston.order.dto.input.UpdateOrderInput;
import com.example.aston.order.dto.output.OrderOutput;

import java.util.List;

public interface OrderServiceInterface {
    OrderOutput save(CreateOrderInput dto) throws NotFoundException;

    OrderOutput update(UpdateOrderInput order, Integer id) throws NotFoundException;

    OrderOutput findById(Integer id) throws NotFoundException;

    List<OrderOutput> findAll();

    void deleteById(Integer id) throws NotFoundException;
}