package repository;

import db.HibernateUtil;
import exception.NotFoundException;
import org.hibernate.*;

import java.util.*;

public abstract class AbstractDao<T, K> {
    protected abstract Optional<T> findById(K id);

    public void delete(T t) throws NotFoundException {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(t);
            transaction.commit();
        } catch (Exception e) {
            rollbackTransaction(transaction);
            e.printStackTrace();
        }
    }

    public void save(T t) throws NotFoundException {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(t);
            transaction.commit();
        } catch (Exception e) {
            rollbackTransaction(transaction);
            e.printStackTrace();
        }
    }

    public void update(T t) throws NotFoundException {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(t);
            transaction.commit();
        } catch (Exception e) {
            rollbackTransaction(transaction);
            e.printStackTrace();
        }
    }

    public List<T> findAll(String from) {
        Transaction transaction = null;
        List<T> objects = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            objects = session.createQuery(from).getResultList();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }

        return objects;
    }

    protected void rollbackTransaction(Transaction transaction) {
        if (transaction != null) {
            transaction.rollback();
        }
    }
}