package com.github.yaroslavskybadev.dao.impl;

import com.github.yaroslavskybadev.ConnectionManager;
import com.github.yaroslavskybadev.dao.AbstractDao;
import com.github.yaroslavskybadev.dto.Author;
import com.github.yaroslavskybadev.dto.Book;
import org.apache.commons.lang3.RandomStringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthorDao extends AbstractDao<Author> {
    private static final BookDao BOOK_DAO = new BookDao();

    private long lastId = findAll().stream().mapToLong(Author::getId).max().getAsLong();

    @Override
    protected Author getEntity(ResultSet resultSet) throws SQLException {
        final Author author = new Author();
        author.setId(resultSet.getLong("id"));
        author.setFirstName(resultSet.getString("first_name"));
        author.setSecondName(resultSet.getString("second_name"));

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("select book_id from book_author where author_id = ?")) {
            preparedStatement.setLong(1, author.getId());

            try (ResultSet bookResultSet = preparedStatement.executeQuery()) {
                while (bookResultSet.next()) {
                    author.addBook(BOOK_DAO.findById(bookResultSet.getLong("book_id")));
                }
            }
        }

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

    @Override
    protected Author createRandomizedEntity() {
        final Author author = new Author();
        author.setId(++lastId);

        final int stringLength = 50;
        author.setFirstName(RandomStringUtils.randomAlphanumeric(stringLength));
        author.setSecondName(RandomStringUtils.randomAlphanumeric(stringLength));

        return author;
    }

    public void addBooks(Author author) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("insert into book_author values (?, ?)")) {
            for (Book book : author.getBookList()) {
                preparedStatement.setLong(1, book.getId());
                preparedStatement.setLong(2, author.getId());

                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
        } catch (SQLException exception) {
            throw new IllegalArgumentException("Some errors occurred while connecting", exception);
        }
    }

    private void setSpecificValuesForCreate(PreparedStatement preparedStatement, Author e) throws SQLException {
        preparedStatement.setString(2, e.getFirstName());
        preparedStatement.setString(3, e.getSecondName());
    }
}
