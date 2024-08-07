package service;

import dto.OrderDto;
import exception.NotFoundException;

import java.util.List;

public interface OrderService {
    OrderDto save(OrderDto dto) throws NotFoundException;

    void update(OrderDto order) throws NotFoundException;

    OrderDto findById(Integer id) throws NotFoundException;

    List<OrderDto> findAll();

    void delete(Integer id) throws NotFoundException;
}