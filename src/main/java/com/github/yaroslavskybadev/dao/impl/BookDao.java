package com.github.yaroslavskybadev.dao.impl;

import com.github.yaroslavskybadev.ConnectionManager;
import com.github.yaroslavskybadev.dao.AbstractDao;
import com.github.yaroslavskybadev.dto.Author;
import com.github.yaroslavskybadev.dto.Book;
import com.github.yaroslavskybadev.dto.Subscription;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.LongSupplier;

public class BookDao extends AbstractDao<Book> {
    @Override
    protected Book getEntity(ResultSet resultSet) throws SQLException {
        final Book book = new Book();
        book.setId(resultSet.getLong("id"));
        book.setName(resultSet.getString("name"));
        book.setPageCount(resultSet.getInt("page_count"));

        return book;
    }

    @Override
    protected String getSqlForCreate() {
        return "insert into book (id, name, page_count) values (?, ?, ?)";
    }

    @Override
    protected String getSqlForUpdate() {
        return "update book set id = ?, name = ?, page_count = ? where id = ?";
    }

    @Override
    protected String getSqlForRemove() {
        return "delete from book where id = ?";
    }

    @Override
    protected String getSqlForFindAll() {
        return "select id, name, page_count from book";
    }

    @Override
    protected String getSqlForFindById() {
        return "select id, name, page_count from book where id = ?";
    }

    @Override
    protected void setValuesForCreate(PreparedStatement preparedStatement, Book e) {
        try {
            preparedStatement.setLong(1, e.getId());
            setSpecificValuesForCreate(preparedStatement, e);
        } catch (SQLException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    protected void setValuesForUpdate(PreparedStatement preparedStatement, Book e) {
        final Long id = e.getId();

        try {
            preparedStatement.setLong(1, id);
            setSpecificValuesForCreate(preparedStatement, e);
            preparedStatement.setLong(4, id);
        } catch (SQLException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    protected void setValuesForRemove(PreparedStatement preparedStatement, Book e) {
        try {
            preparedStatement.setLong(1, e.getId());
        } catch (SQLException ex) {
            throw new IllegalStateException(ex);
        }
    }

    public void addAuthors(Book book) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("insert into book_author values (?, ?)")) {

            for (Author author : book.getAuthorList()) {
                addBatch(preparedStatement, book, author::getId);
            }

            preparedStatement.executeBatch();
        } catch (SQLException exception) {
            throw new IllegalArgumentException("Some errors occurred while connecting", exception);
        }
    }

    public void addSubscriptions(Book book) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("insert into book_subscription values (?, ?)")) {

            for (Subscription subscription : book.getSubscriptionList()) {
                addBatch(preparedStatement, book, subscription::getId);
            }

            preparedStatement.executeBatch();
        } catch (SQLException exception) {
            throw new IllegalArgumentException("Some errors occurred while connecting", exception);
        }
    }

    private void addBatch(PreparedStatement preparedStatement, Book book, LongSupplier idSupplier) throws SQLException {
        preparedStatement.setLong(1, book.getId());
        preparedStatement.setLong(2, idSupplier.getAsLong());

        preparedStatement.addBatch();
    }

    private void setSpecificValuesForCreate(PreparedStatement preparedStatement, Book book) throws SQLException {
        preparedStatement.setString(2, book.getName());
        preparedStatement.setInt(3, book.getPageCount());
    }
}
