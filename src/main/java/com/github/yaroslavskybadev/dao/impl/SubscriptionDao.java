package com.github.yaroslavskybadev.dao.impl;

import com.github.yaroslavskybadev.ConnectionManager;
import com.github.yaroslavskybadev.dao.AbstractDao;
import com.github.yaroslavskybadev.dto.Subscription;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SubscriptionDao extends AbstractDao<Subscription> {
    @Override
    protected Subscription getEntity(ResultSet resultSet) throws SQLException {
        final Subscription subscription = new Subscription();
        subscription.setId(resultSet.getLong("id"));
        subscription.setReaderId(resultSet.getLong("reader_id"));
        subscription.setRegistrationDate(resultSet.getDate("registration_date"));
        subscription.setExpirationDate(resultSet.getDate("expiration_date"));

        return subscription;
    }

    @Override
    protected String getSqlForCreate() {
        return "insert into subscription (id, reader_id, registration_date, expiration_date) values (?, ?, ?, ?)";
    }

    @Override
    protected String getSqlForUpdate() {
        return "update subscription set id = ?, reader_id = ?, registration_date = ?, expiration_date = ? where id = ?";
    }

    @Override
    protected String getSqlForRemove() {
        return "delete from subscription where id = ?";
    }

    @Override
    protected String getSqlForFindAll() {
        return "select id, reader_id, registration_date, expiration_date from subscription";
    }

    @Override
    protected String getSqlForFindById() {
        return "select id, reader_id, registration_date, expiration_date from subscription where id = ?";
    }

    @Override
    protected void setValuesForCreate(PreparedStatement preparedStatement, Subscription e) {
        try {
            preparedStatement.setLong(1, e.getId());
            setSpecificValuesForCreate(preparedStatement, e);
        } catch (SQLException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    protected void setValuesForUpdate(PreparedStatement preparedStatement, Subscription e) {
        final Long id = e.getId();

        try {
            preparedStatement.setLong(1, id);
            setSpecificValuesForCreate(preparedStatement, e);
            preparedStatement.setLong(5, id);
        } catch (SQLException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    protected void setValuesForRemove(PreparedStatement preparedStatement, Subscription e) {
        try {
            preparedStatement.setLong(1, e.getId());
        } catch (SQLException ex) {
            throw new IllegalStateException(ex);
        }
    }

    public List<Subscription> findSubscriptionsByFirstName(Date registrationDate) {
        final List<Subscription> subscriptionList = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("select * from subscription where registration_date = ?")) {
            preparedStatement.setDate(1, registrationDate);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    subscriptionList.add(getEntity(resultSet));
                }
            }
        } catch (SQLException exception) {
            throw new IllegalArgumentException("Some errors occurred while connecting", exception);
        }

        return subscriptionList;
    }

    private void setSpecificValuesForCreate(PreparedStatement preparedStatement, Subscription e) throws SQLException {
        preparedStatement.setLong(2, e.getReaderId());
        preparedStatement.setDate(3, e.getRegistrationDate());
        preparedStatement.setDate(4, e.getExpirationDate());
    }
}
