package ru.aston.company.dao;

import java.util.List;

public interface BaseDao<T> {
    List<T> findAll();

    List<T> findById(long id);

    List<T> findByName(String name);

    T save(T entity);

    int update(T entity);

    boolean deleteById(long id);
}
