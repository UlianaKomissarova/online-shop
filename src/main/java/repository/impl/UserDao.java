package repository.impl;

import db.HibernateUtil;
import exception.NotFoundException;
import lombok.NoArgsConstructor;
import model.User;
import org.hibernate.Session;
import repository.AbstractDao;

import java.util.*;

@NoArgsConstructor
public class UserDao extends AbstractDao<User, Integer> {
    public void delete(User user) throws NotFoundException {
        super.delete(user);
    }

    public Optional<User> findById(Integer id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(User.class, id));
        }
    }

    public List<User> findAll() {
        return findAll("from User");
    }
}