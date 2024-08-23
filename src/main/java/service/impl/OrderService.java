package service.impl;

import dto.OrderDto;
import exception.NotFoundException;
import model.*;
import repository.impl.OrderDao;
import service.OrderServiceInterface;

import java.util.*;

import static service.mapper.OrderMapper.*;

public class OrderService implements OrderServiceInterface {
    private final OrderDao dao = new OrderDao();
    private static OrderServiceInterface orderService;
    private final UserService userService = (UserService) UserService.getInstance();
    private final ItemService itemService = (ItemService) ItemService.getInstance();

    public static synchronized OrderServiceInterface getInstance() {
        if (orderService == null) {
            orderService = new OrderService();
        }
        return orderService;
    }

    @Override
    public void save(OrderDto dto) throws NotFoundException {
        Order order = toOrderFromDto(dto);
        order.setBuyer(userService.getExistingUser(dto.getBuyerId()));

        List<Item> items = getItemsForOrder(dto.getItemsIds());
        checkBuyerIsNotVendor(items, dto.getBuyerId());
        order.setItems(items);

        dao.save(order);
    }

    @Override
    public void update(OrderDto dto) throws NotFoundException {
        if (dto == null || dto.getId() == 0) {
            throw new IllegalArgumentException();
        }
        getExistingOrder(dto.getId());

        Order orderForUpdate = toOrderFromDto(dto);

        User buyer = userService.getExistingUser(dto.getBuyerId());
        orderForUpdate.setBuyer(buyer);

        List<Item> items = getItemsForOrder(dto.getItemsIds());
        checkBuyerIsNotVendor(items, buyer.getId());
        orderForUpdate.setItems(items);

        dao.update(orderForUpdate);
    }

    @Override
    public OrderDto findById(Integer id) throws NotFoundException {
        Order order = getExistingOrder(id);
        return toDtoFromOrder(order);
    }

    @Override
    public List<OrderDto> findAll() {
        List<Order> orders = dao.findAll();
        List<OrderDto> dtos = new ArrayList<>();

        for (Order order : orders) {
            dtos.add(toDtoFromOrder(order));
        }

        return dtos;
    }

    @Override
    public void delete(Integer id) throws NotFoundException {
        Order order = getExistingOrder(id);
        dao.delete(order);
    }

    public Order getExistingOrder(Integer id) throws NotFoundException {
        return dao.findById(id).orElseThrow(() -> new NotFoundException("Order not found."));
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