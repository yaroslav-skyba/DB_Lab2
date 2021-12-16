package com.github.yaroslavskybadev.dao;

import com.github.yaroslavskybadev.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public abstract class AbstractDao<T> implements Dao<T> {
    protected abstract T getEntity(ResultSet resultSet) throws SQLException;

    protected abstract String getSqlForCreate();
    protected abstract String getSqlForUpdate();
    protected abstract String getSqlForRemove();
    protected abstract String getSqlForFindAll();
    protected abstract String getSqlForFindById();

    protected abstract void setValuesForCreate(PreparedStatement preparedStatement, T e);
    protected abstract void setValuesForUpdate(PreparedStatement preparedStatement, T e);
    protected abstract void setValuesForRemove(PreparedStatement preparedStatement, T e);

    protected abstract T createRandomizedEntity();

    @Override
    public void create(T entity) {
        modify(entity, this::getSqlForCreate, this::setValuesForCreate);
    }

    @Override
    public void update(T entity) {
        modify(entity, this::getSqlForUpdate, this::setValuesForUpdate);
    }

    @Override
    public void remove(T entity) {
        modify(entity, this::getSqlForRemove, this::setValuesForRemove);
    }

    @Override
    public List<T> findAll() {
        final List<T> entityList = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getSqlForFindAll());
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                entityList.add(getEntity(resultSet));
            }
        } catch (SQLException exception) {
            throw new IllegalArgumentException("Some errors occurred while connecting", exception);
        }

        return entityList;
    }

    @Override
    public T findById(Long id) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getSqlForFindById())) {
            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new IllegalStateException("No rows were found");
                }

                return getEntity(resultSet);
            }
        } catch (SQLException exception) {
            throw new IllegalArgumentException("Some errors occurred while connecting", exception);
        }
    }

    @Override
    public void generateEntities() {
       for (int i = 0; i < 10000; i++) {
           create(createRandomizedEntity());
       }
    }

    protected void modify(T entity, Supplier<String> sqlSupplier, BiConsumer<PreparedStatement, T> valuesBiConsumer) {
        try (Connection connection = ConnectionManager.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlSupplier.get())) {
                valuesBiConsumer.accept(preparedStatement, entity);

                if (preparedStatement.executeUpdate() == 0) {
                    throw new IllegalStateException("Rows to modify were not found");
                }
            }
        } catch (SQLException exception) {
            throw new IllegalArgumentException("Some errors occurred while connecting", exception);
        }
    }
}
