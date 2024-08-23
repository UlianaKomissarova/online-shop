package repository.impl;

import db.HibernateUtil;
import exception.NotFoundException;
import lombok.NoArgsConstructor;
import model.Delivery;
import org.hibernate.Session;
import repository.AbstractDao;

import java.util.*;

@NoArgsConstructor
public class DeliveryDao extends AbstractDao<Delivery, Integer> {
    public void delete(Delivery delivery) throws NotFoundException {
        super.delete(delivery);
    }

    public Optional<Delivery> findById(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Delivery.class, id));
        }
    }

    public List<Delivery> findAll() {
        return findAll("from Delivery");
    }
}