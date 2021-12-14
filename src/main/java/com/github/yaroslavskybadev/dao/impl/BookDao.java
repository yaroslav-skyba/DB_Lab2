package com.github.yaroslavskybadev.dao.impl;

import com.github.yaroslavskybadev.ConnectionManager;
import com.github.yaroslavskybadev.dao.AbstractDao;
import com.github.yaroslavskybadev.dto.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public List<Book> findBooksByName(String name) {
        final List<Book> bookList = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("select * from book where name = ?")) {
            preparedStatement.setString(1, name);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    bookList.add(getEntity(resultSet));
                }
            }
        } catch (SQLException exception) {
            throw new IllegalArgumentException("Some errors occurred while connecting", exception);
        }

        return bookList;
    }

    private void setSpecificValuesForCreate(PreparedStatement preparedStatement, Book e) throws SQLException {
        preparedStatement.setString(2, e.getName());
        preparedStatement.setInt(3, e.getPageCount());
    }
}
