package com.github.yaroslavskybadev.dao.impl;

import com.github.yaroslavskybadev.ConnectionManager;
import com.github.yaroslavskybadev.dao.AbstractDao;
import com.github.yaroslavskybadev.dto.Book;
import com.github.yaroslavskybadev.dto.Reader;
import com.github.yaroslavskybadev.dto.Subscription;
import org.apache.commons.math3.random.RandomDataGenerator;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

public class SubscriptionDao extends AbstractDao<Subscription> {
    private static final RandomDataGenerator RANDOM_DATA_GENERATOR = new RandomDataGenerator();
    private static final long LAST_READER_ID = new ReaderDao().findAll().stream().mapToLong(Reader::getId).max().getAsLong();

    private long lastId = findAll().stream().mapToLong(Subscription::getId).max().getAsLong();

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

    @Override
    protected Subscription createRandomizedEntity() {
        final Subscription subscription = new Subscription();
        subscription.setId(++lastId);
        subscription.setReaderId(RANDOM_DATA_GENERATOR.nextLong(1, LAST_READER_ID));

        final ThreadLocalRandom current = ThreadLocalRandom.current();
        final long registrationTime = current.nextLong(Date.valueOf("1500-1-1").getTime(), new java.util.Date().getTime());

        subscription.setRegistrationDate(new Date(registrationTime));
        subscription.setExpirationDate(new Date(current.nextLong(registrationTime, Date.valueOf("3000-1-1").getTime())));

        return subscription;
    }

    public void addBooks(Subscription subscription) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("insert into book_subscription values (?, ?)")) {

            for (Book book : subscription.getBookList()) {
                preparedStatement.setLong(1, book.getId());
                preparedStatement.setLong(2, subscription.getId());

                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
        } catch (SQLException exception) {
            throw new IllegalArgumentException("Some errors occurred while connecting", exception);
        }
    }

    private void setSpecificValuesForCreate(PreparedStatement preparedStatement, Subscription e) throws SQLException {
        preparedStatement.setLong(2, e.getReaderId());
        preparedStatement.setDate(3, e.getRegistrationDate());
        preparedStatement.setDate(4, e.getExpirationDate());
    }
}
