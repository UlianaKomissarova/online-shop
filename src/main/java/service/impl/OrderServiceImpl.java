package service.impl;

import dto.OrderDto;
import exception.NotFoundException;
import model.Order;
import repository.OrderRepository;
import repository.impl.OrderRepositoryImpl;
import service.OrderService;

import java.util.*;

import static service.mapper.OrderMapper.*;

public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository = OrderRepositoryImpl.getInstance();
    private static OrderService instance;

    public static synchronized OrderService getInstance() {
        if (instance == null) {
            instance = new OrderServiceImpl();
        }
        return instance;
    }

    @Override
    public OrderDto save(OrderDto dto) throws NotFoundException {
        Order order = orderRepository.save(toOrderFromDto(dto));
        return toDtoFromOrder(orderRepository.findById(order.getId()).orElse(order));
    }

    @Override
    public void update(OrderDto dto) throws NotFoundException {
        if (dto == null || dto.getId() == 0) {
            throw new IllegalArgumentException();
        }
        checkExistOrder(dto.getId());
        orderRepository.update(toOrderFromDto(dto));
    }

    @Override
    public OrderDto findById(Integer id) throws NotFoundException {
        checkExistOrder(id);
        Order order = orderRepository.findById(id).orElseThrow();
        return toDtoFromOrder(order);
    }

    @Override
    public List<OrderDto> findAll() {
        List<Order> orders = orderRepository.findAll();
        List<OrderDto> dtos = new ArrayList<>();

        for (Order order : orders) {
            dtos.add(toDtoFromOrder(order));
        }

        return dtos;
    }

    @Override
    public void delete(Integer id) throws NotFoundException {
        checkExistOrder(id);
        orderRepository.deleteById(id);
    }

    private void checkExistOrder(Integer id) throws NotFoundException {
        if (!orderRepository.findById(id).isPresent()) {
            throw new NotFoundException("Order not found.");
        }
    }
}