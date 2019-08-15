package ru.javawebinar.basejava.sql;

import ru.javawebinar.basejava.exception.StorageException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {
    private final ConnectionFactory connectionFactory;

    public SqlHelper(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public interface SqlOperation<T> {
        T doOperation(PreparedStatement ps) throws SQLException;
    }

    public <T> T execute(String command, SqlOperation<T> sqlOperation) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(command)) {
            return sqlOperation.doOperation(ps);
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }
}
