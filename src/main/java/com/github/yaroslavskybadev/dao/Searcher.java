package com.github.yaroslavskybadev.dao;

import com.github.yaroslavskybadev.ConnectionManager;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Searcher {
    public List<Map<String, Object>> searchWithFirstNameAndSecondNameAndRegistrationDate(String firstName, String secondName,
                                                                                         Date registrationDate) {
        return searchWithReaderAndSubscription(
                "select reader_id, first_name, second_name, s.id, registration_date, expiration_date from reader as r " +
                        "inner join subscription as s on r.id = reader_id " +
                        "where first_name = ? and second_name = ? and registration_date > ?",
                new Object[]{firstName, secondName, registrationDate}
        );
    }

    public List<Map<String, Object>> searchWithFirstNameAndSecondNameAndExpirationDate(String firstName, String secondName,
                                                                                       Date expirationDate) {
        return searchWithReaderAndSubscription(
                "select reader_id, first_name, second_name, s.id, registration_date, expiration_date from reader as r " +
                        "inner join subscription as s on r.id = reader_id " +
                        "where first_name = ? and second_name = ? and expiration_date > ?",
                new Object[]{firstName, secondName, expirationDate}
        );
    }

    public List<Map<String, Object>> searchWithSecondNameAndRegistrationDateAndExpirationDate(String secondName, Date registrationDate,
                                                                                              Date expirationDate) {
        return searchWithReaderAndSubscription(
                "select reader_id, first_name, second_name, s.id, registration_date, expiration_date from reader as r " +
                        "inner join subscription as s on r.id = reader_id " +
                        "where second_name = ? and registration_date > ? and expiration_date > ?",
                new Object[]{secondName, registrationDate, expirationDate}
        );
    }

    private List<Map<String, Object>> searchWithReaderAndSubscription(String sql, Object[] attributeValues) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            for (int i = 1; i <= attributeValues.length; i++) {
                preparedStatement.setObject(i, attributeValues[i - 1]);
            }

            final List<Map<String, Object>> resultList = new ArrayList<>();
            final String readerIdKey = "reader_id";
            final String firstNameKey = "first_name";
            final String secondNameKey = "second_name";
            final String subscriptionIdKey = "id";
            final String registrationDateKey = "registration_date";
            final String expirationDateKey = "expiration_date";

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    resultList.add(Map.of(
                            readerIdKey, resultSet.getLong(readerIdKey),
                            firstNameKey, resultSet.getString(firstNameKey),
                            secondNameKey, resultSet.getString(secondNameKey),
                            subscriptionIdKey, resultSet.getLong(subscriptionIdKey),
                            registrationDateKey, resultSet.getDate(registrationDateKey),
                            expirationDateKey, resultSet.getDate(expirationDateKey)
                    ));
                }
            }

            return resultList;
        } catch (SQLException e) {
            throw new IllegalArgumentException("Some errors occurred while connecting", e);
        }
    }
}
