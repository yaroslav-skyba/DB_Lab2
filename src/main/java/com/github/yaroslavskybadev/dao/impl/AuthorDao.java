package com.github.yaroslavskybadev.dao.impl;

import com.github.yaroslavskybadev.dao.AbstractDao;
import com.github.yaroslavskybadev.dto.Author;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorDao extends AbstractDao<Author> {
    @Override
    protected Author getEntity(ResultSet resultSet) throws SQLException {
        final Author author = new Author();
        author.setId(resultSet.getLong("id"));
        author.setFirstName(resultSet.getString("first_name"));
        author.setSecondName(resultSet.getString("second_name"));

        return author;
    }

    @Override
    protected String getSqlForCreate() {
        return "insert into author (id, first_name, second_name) values (?, ?, ?)";
    }

    @Override
    protected String getSqlForUpdate() {
        return "update author set id = ?, first_name = ?, second_name = ? where id = ?";
    }

    @Override
    protected String getSqlForRemove() {
        return "delete from author where id = ?";
    }

    @Override
    protected String getSqlForFindAll() {
        return "select id, first_name, second_name from author";
    }

    @Override
    protected String getSqlForFindById() {
        return "select id, first_name, second_name from author where id = ?";
    }

    @Override
    protected void setValuesForCreate(PreparedStatement preparedStatement, Author e) {
        try {
            preparedStatement.setLong(1, e.getId());
            setSpecificValuesForCreate(preparedStatement, e);
        } catch (SQLException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    protected void setValuesForUpdate(PreparedStatement preparedStatement, Author e) {
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
    protected void setValuesForRemove(PreparedStatement preparedStatement, Author e) {
        try {
            preparedStatement.setLong(1, e.getId());
        } catch (SQLException ex) {
            throw new IllegalStateException(ex);
        }
    }

    private void setSpecificValuesForCreate(PreparedStatement preparedStatement, Author e) throws SQLException {
        preparedStatement.setString(2, e.getFirstName());
        preparedStatement.setString(3, e.getSecondName());
    }
}
