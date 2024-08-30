package aston.order.service;

import aston.exception.NotFoundException;
import aston.order.dto.input.*;
import aston.order.dto.output.OrderOutput;

import java.util.List;

public interface OrderServiceInterface {
    OrderOutput save(CreateOrderInput dto) throws NotFoundException;

    OrderOutput update(UpdateOrderInput order, Integer id) throws NotFoundException;

    OrderOutput findById(Integer id) throws NotFoundException;

    List<OrderOutput> findAll();

    void deleteById(Integer id) throws NotFoundException;
}