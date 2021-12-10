package com.github.yaroslavskybadev;

import org.apache.commons.dbcp2.BasicDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
    private static final BasicDataSource BASIC_DATA_SOURCE = new BasicDataSource();

    static {
        final Properties properties = new Properties();

        try (InputStream inputStream = ConnectionManager.class.getClassLoader().getResourceAsStream("application.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new IllegalStateException("Some errors occurred while loading input stream", e);
        }

        BASIC_DATA_SOURCE.setDriverClassName(properties.getProperty("jdbc.driverClassName"));
        BASIC_DATA_SOURCE.setUrl(properties.getProperty("jdbc.url"));
        BASIC_DATA_SOURCE.setUsername(properties.getProperty("jdbc.username"));
        BASIC_DATA_SOURCE.setPassword(properties.getProperty("jdbc.password"));
    }

    private ConnectionManager() {
    }

    public static Connection getConnection() throws SQLException {
        return BASIC_DATA_SOURCE.getConnection();
    }
}
