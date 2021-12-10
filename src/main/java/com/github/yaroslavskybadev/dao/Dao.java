package com.github.yaroslavskybadev.dao;

import java.util.List;

public interface Dao<T> {
    void create(T entity);
    void update(T entity);
    void remove(T entity);

    List<T> findAll();
    T findById(Long id);
}
