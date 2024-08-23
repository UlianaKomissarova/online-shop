package repository.impl;

import db.HibernateUtil;
import exception.NotFoundException;
import lombok.NoArgsConstructor;
import model.Item;
import org.hibernate.Session;
import repository.AbstractDao;

import java.util.*;

@NoArgsConstructor
public class ItemDao extends AbstractDao<Item, Integer> {
    public void delete(Item item) throws NotFoundException {
        super.delete(item);
    }

    public Optional<Item> findById(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Item.class, id));
        }
    }

    public List<Item> findAll() {
        return findAll("from Item");
    }
}