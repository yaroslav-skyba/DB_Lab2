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

public interface Dao<T> {
    void create(T entity);
    void update(T entity);
    void remove(T entity);
    List<T> findAll();
    T findById(Long id);

    void generateEntities();

    static List<List<Map<String, Object>>> searchWithReaderAndSubscriptionEntities(String firstName, String secondName, Date registrationDate) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "select reader_id, first_name, second_name, s.id, registration_date, expiration_date from reader as r " +
                             "inner join subscription as s on r.id = reader_id " +
                             "where first_name = ? and second_name = ? and registration_date > ?")) {
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, secondName);
            preparedStatement.setDate(3, registrationDate);

            final List<List<Map<String, Object>>> resultList = new ArrayList<>();
            final String readerIdKey = "reader_id";
            final String firstNameKey = "first_name";
            final String secondNameKey = "second_name";
            final String subscriptionIdKey = "id";
            final String registrationDateKey = "registration_date";
            final String expirationDateKey = "expiration_date";

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    resultList.add(List.of(
                            Map.of(readerIdKey, resultSet.getString(readerIdKey)),
                            Map.of(firstNameKey, resultSet.getString(firstNameKey)),
                            Map.of(secondNameKey, resultSet.getString(secondNameKey)),
                            Map.of(subscriptionIdKey, resultSet.getString(subscriptionIdKey)),
                            Map.of(registrationDateKey, resultSet.getDate(registrationDateKey)),
                            Map.of(expirationDateKey, resultSet.getDate(expirationDateKey))
                    ));
                }
            }

            return resultList;
        } catch (SQLException e) {
            throw new IllegalArgumentException("Some errors occurred while connecting", e);
        }
    }

    static List<List<Map<String, Object>>> searchWithBookAndReaderAndSubscriptionEntities(String bookName, String secondName,
                                                                                    Date registrationDate) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "select b.name, b.page_count, reader_id, first_name, second_name, s.id, s.registration_date, s.expiration_date" +
                             " from reader as r " +
                             " inner join subscription as s on r.id = reader_id" +
                             " inner join book as b on s.id = b.id" +
                             " where b.name = ? and second_name = ? and registration_date > ?")) {
            preparedStatement.setString(1, bookName);
            preparedStatement.setString(2, secondName);
            preparedStatement.setDate(3, registrationDate);

            final List<List<Map<String, Object>>> resultList = new ArrayList<>();
            final String bookNameKey = "name";
            final String pageCountKey = "page_count";
            final String readerIdKey = "reader_id";
            final String firstNameKey = "first_name";
            final String secondNameKey = "second_name";
            final String subscriptionIdKey = "id";
            final String registrationDateKey = "registration_date";
            final String expirationDateKey = "expiration_date";

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    resultList.add(List.of(
                            Map.of(bookNameKey, resultSet.getString(bookNameKey)),
                            Map.of(bookNameKey, resultSet.getInt(pageCountKey)),
                            Map.of(readerIdKey, resultSet.getLong(readerIdKey)),
                            Map.of(firstNameKey, resultSet.getString(firstNameKey)),
                            Map.of(secondNameKey, resultSet.getString(secondNameKey)),
                            Map.of(subscriptionIdKey, resultSet.getLong(subscriptionIdKey)),
                            Map.of(registrationDateKey, resultSet.getDate(registrationDateKey)),
                            Map.of(expirationDateKey, resultSet.getDate(expirationDateKey))
                    ));
                }
            }

            return resultList;
        } catch (SQLException e) {
            throw new IllegalArgumentException("Some errors occurred while connecting", e);
        }
    }
//    List<Map<String, Object>> searchWith4Tables(Reader reader, Subscription subscription, Book book, Author author);
}
