package repository.impl;

import db.HibernateUtil;
import exception.NotFoundException;
import lombok.NoArgsConstructor;
import model.Order;
import org.hibernate.Session;
import repository.AbstractDao;

import java.util.*;

@NoArgsConstructor
public class OrderDao extends AbstractDao<Order, Integer> {
    public void delete(Order order) throws NotFoundException {
        super.delete(order);
    }

    public Optional<Order> findById(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Order.class, id));
        }
    }

    public List<Order> findAll() {
        return findAll("from Order");
    }
}