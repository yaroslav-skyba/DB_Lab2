package com.github.yaroslavskybadev.dao.impl;

import com.github.yaroslavskybadev.dao.AbstractDao;
import com.github.yaroslavskybadev.dto.Reader;
import org.apache.commons.lang3.RandomStringUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReaderDao extends AbstractDao<Reader> {
    private long lastId = findAll().stream().mapToLong(Reader::getId).max().getAsLong();

    @Override
    protected Reader getEntity(ResultSet resultSet) throws SQLException {
        final Reader reader = new Reader();
        reader.setId(resultSet.getLong("id"));
        reader.setFirstName(resultSet.getString("first_name"));
        reader.setSecondName(resultSet.getString("second_name"));

        return reader;
    }

    @Override
    protected String getSqlForCreate() {
        return "insert into reader (id, first_name, second_name) values (?, ?, ?)";
    }

    @Override
    protected String getSqlForUpdate() {
        return "update reader set id = ?, first_name = ?, second_name = ? where id = ?";
    }

    @Override
    protected String getSqlForRemove() {
        return "delete from reader where id = ?";
    }

    @Override
    protected String getSqlForFindAll() {
        return "select id, first_name, second_name from reader";
    }

    @Override
    protected String getSqlForFindById() {
        return "select id, first_name, second_name from reader where id = ?";
    }

    @Override
    protected void setValuesForCreate(PreparedStatement preparedStatement, Reader e) {
        try {
            preparedStatement.setLong(1, e.getId());
            setSpecificValuesForCreate(preparedStatement, e);
        } catch (SQLException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    protected void setValuesForUpdate(PreparedStatement preparedStatement, Reader e) {
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
    protected void setValuesForRemove(PreparedStatement preparedStatement, Reader e) {
        try {
            preparedStatement.setLong(1, e.getId());
        } catch (SQLException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    protected Reader createRandomizedEntity() {
        final Reader reader = new Reader();
        reader.setId(++lastId);

        final int stringLength = 50;
        reader.setFirstName(RandomStringUtils.randomAlphanumeric(stringLength));
        reader.setSecondName(RandomStringUtils.randomAlphanumeric(stringLength));

        return reader;
    }

    private void setSpecificValuesForCreate(PreparedStatement preparedStatement, Reader e) throws SQLException {
        preparedStatement.setString(2, e.getFirstName());
        preparedStatement.setString(3, e.getSecondName());
    }
}
