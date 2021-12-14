package com.github.yaroslavskybadev.dao.impl;

import com.github.yaroslavskybadev.ConnectionManager;
import com.github.yaroslavskybadev.dao.AbstractDao;
import com.github.yaroslavskybadev.dto.Author;
import com.github.yaroslavskybadev.dto.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuthorDao extends AbstractDao<Author> {
    private static final BookDao BOOK_DAO = new BookDao();

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

    public List<Author> findAuthorsByFirstName(String firstName) {
        final List<Author> authorList = new ArrayList<>();

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("select * from author where first_name = ?")) {
            preparedStatement.setString(1, firstName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    authorList.add(getEntity(resultSet));
                }
            }
        } catch (SQLException exception) {
            throw new IllegalArgumentException("Some errors occurred while connecting", exception);
        }

        return authorList;
    }

    public void addBooks(Author author) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement("insert into book_author (book_id, author_id) values (?, ?)")) {
            for (Book book : author.getBookList()) {
                statement.setLong(1, book.getId());
                statement.setLong(2, author.getId());

                statement.addBatch();
            }

            statement.executeBatch();
        } catch (SQLException exception) {
            throw new IllegalArgumentException("Some errors occurred while connecting", exception);
        }
    }

    private void setSpecificValuesForCreate(PreparedStatement preparedStatement, Author e) throws SQLException {
        preparedStatement.setString(2, e.getFirstName());
        preparedStatement.setString(3, e.getSecondName());
    }
}
