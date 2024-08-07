package repository;

import model.Order;

public interface OrderRepository extends Repository<Order, Integer> {
    boolean deleteByBuyerId(int id);

    boolean deleteByItemId(int id);
}