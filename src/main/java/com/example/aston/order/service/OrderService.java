package com.example.aston.order.service;

import com.example.aston.exception.NotFoundException;
import com.example.aston.item.model.Item;
import com.example.aston.item.service.ItemService;
import com.example.aston.order.dto.input.CreateOrderInput;
import com.example.aston.order.dto.input.UpdateOrderInput;
import com.example.aston.order.dto.output.OrderOutput;
import com.example.aston.order.mapper.OrderToOutputMapper;
import com.example.aston.order.repository.OrderRepository;
import com.example.aston.user.model.User;
import com.example.aston.user.service.UserService;
import com.example.aston.order.model.Order;
import com.example.aston.order.model.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.aston.order.mapper.CreationDtoToOrderMapper.toOrderFromCreationDto;
import static com.example.aston.order.mapper.OrderToOutputMapper.toDtoFromOrder;

@Service
@RequiredArgsConstructor
public class OrderService implements OrderServiceInterface {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ItemService itemService;

    @Override
    @Transactional
    public OrderOutput save(CreateOrderInput dto) throws NotFoundException {
        Order order = toOrderFromCreationDto(dto);
        order.setBuyer(userService.getExistingUser(dto.getBuyerId()));

        List<Item> items = getItemsForOrder(dto.getItemsIds());
        checkBuyerIsNotVendor(items, order.getBuyer().getId());
        order.setItems(items);

        order = orderRepository.save(order);
        return toDtoFromOrder(order);
    }

    @Override
    @Transactional
    public OrderOutput update(UpdateOrderInput dto, Integer id) throws NotFoundException {
        Order order = getExistingOrder(id);
        updateOrderProps(dto, order);
        order = orderRepository.save(order);

        return toDtoFromOrder(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderOutput findById(Integer id) throws NotFoundException {
        Order order = getExistingOrder(id);
        return toDtoFromOrder(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderOutput> findAll() {
        return orderRepository.findAll().stream()
            .map(OrderToOutputMapper::toDtoFromOrder)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteById(Integer id) throws NotFoundException {
        getExistingOrder(id);
        orderRepository.deleteById(id);
    }

    public Order getExistingOrder(Integer id) throws NotFoundException {
        return orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Order not found."));
    }

    private void updateOrderProps(UpdateOrderInput dto, Order order) throws NotFoundException {
        User buyer = userService.getExistingUser(dto.getBuyerId());
        order.setBuyer(buyer);

        List<Item> items = getItemsForOrder(dto.getItemsIds());
        checkBuyerIsNotVendor(items, order.getBuyer().getId());
        order.setItems(items);

        if (dto.getStatus() != null && !dto.getStatus().isBlank()) {
            order.setStatus(OrderStatus.valueOf(dto.getStatus()));
        }
    }

    private List<Item> getItemsForOrder(List<Integer> itemsIds) throws NotFoundException {
        List<Item> items = new ArrayList<>();
        for (Integer id : itemsIds) {
            if (!itemService.getExistingItem(id).isAvailable()) {
                throw new IllegalArgumentException("Товар недоступен для заказа.");
            }

            items.add(itemService.getExistingItem(id));
        }

        return items;
    }

    private void checkBuyerIsNotVendor(List<Item> items, int buyerId) {
        for (Item item : items) {
            if (Objects.equals(item.getVendor().getId(), buyerId)) {
                throw new RuntimeException("Продавец не может купить свою вещь.");
            }
        }
    }
}