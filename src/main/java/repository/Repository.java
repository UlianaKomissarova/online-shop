package repository;

import exception.NotFoundException;

import java.util.*;

public interface Repository<T, K> {
    T save(T t) throws NotFoundException;

    void update(T t) throws NotFoundException;

    boolean deleteById(K id) throws NotFoundException;

    Optional<T> findById(K id);

    List<T> findAll();
}