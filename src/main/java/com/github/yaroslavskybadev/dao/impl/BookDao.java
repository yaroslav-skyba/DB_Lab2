package com.github.yaroslavskybadev.dao.impl;

import com.github.yaroslavskybadev.dao.AbstractDao;
import com.github.yaroslavskybadev.dto.Book;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    private void setSpecificValuesForCreate(PreparedStatement preparedStatement, Book e) throws SQLException {
        preparedStatement.setString(2, e.getName());
        preparedStatement.setInt(3, e.getPageCount());
    }
}
