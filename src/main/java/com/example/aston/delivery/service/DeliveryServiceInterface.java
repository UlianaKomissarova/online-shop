package com.example.aston.delivery.service;

import com.example.aston.delivery.dto.input.DeliveryInput;
import com.example.aston.delivery.dto.output.DeliveryOutput;
import com.example.aston.exception.NotFoundException;

import java.util.List;

public interface DeliveryServiceInterface {
    DeliveryOutput save(DeliveryInput dto) throws NotFoundException;

    DeliveryOutput update(DeliveryInput dto, Integer id) throws NotFoundException;

    DeliveryOutput findById(Integer id) throws NotFoundException;

    List<DeliveryOutput> findAll(String type);

    void deleteById(Integer id) throws NotFoundException;
}