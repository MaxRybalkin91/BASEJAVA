package ru.javawebinar.basejava.storage;

import org.postgresql.util.PSQLException;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.ConnectionFactory;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage implements Storage {

    public final ConnectionFactory connectionFactory;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void clear() {
        new SqlHelper(connectionFactory).execute("DELETE FROM resume", PreparedStatement::execute);
    }

    @Override
    public void update(Resume resume) {
        String uuid = resume.getUuid();
        new SqlHelper(connectionFactory).execute("UPDATE resume SET full_name = ?" +
                "WHERE uuid = ?", (SqlHelper.SqlOperation<Resume>) ps -> {
            ps.setString(1, resume.getFullName());
            ps.setString(2, uuid);
            int result = ps.executeUpdate();
            if (result == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public void save(Resume resume) {
        String uuid = resume.getUuid();
        new SqlHelper(connectionFactory).execute("INSERT INTO resume (uuid, full_name) VALUES (?,?)",
                (SqlHelper.SqlOperation<Resume>) ps -> {
                    ps.setString(1, uuid);
                    ps.setString(2, resume.getFullName());
                    try {
                        ps.execute();
                    } catch (PSQLException e) {
                        if (e.getSQLState().equals("23505")) {
                            throw new ExistStorageException(uuid);
                        }
                    }
                    return null;
                });
    }

    @Override
    public Resume get(String uuid) {
        return new Resume(uuid,
                new SqlHelper(connectionFactory).execute("SELECT * FROM resume WHERE uuid = ?",
                        ps -> {
                            ps.setString(1, uuid);
                            ResultSet rs = ps.executeQuery();
                            if (!rs.next()) {
                                throw new NotExistStorageException(uuid);
                            }
                            return rs.getString("full_name");
                        }));
    }

    @Override
    public void delete(String uuid) {
        new SqlHelper(connectionFactory).execute("DELETE FROM resume WHERE uuid = ?",
                (SqlHelper.SqlOperation<Resume>) ps -> {
                    ps.setString(1, uuid);
                    if (ps.executeUpdate() == 0) {
                        throw new NotExistStorageException(uuid);
                    }
                    return null;
                });
    }

    @Override
    public List<Resume> getSortedStorage() {
        List<Resume> storage = new ArrayList<>();
        new SqlHelper(connectionFactory).execute("SELECT * FROM resume ORDER BY full_name, uuid",
                (SqlHelper.SqlOperation<Resume>) ps -> {
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        String uuid = rs.getString("uuid");
                        String fullName = rs.getString("full_name");
                        storage.add(new Resume(uuid, fullName));
                    }
                    return null;
                });
        return storage;
    }

    @Override
    public int size() {
        return new SqlHelper(connectionFactory).execute("SELECT COUNT(uuid) FROM resume",
                ps -> {
                    int size = 0;
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        size = rs.getInt(1);
                    }
                    return size;
                });
    }
}