package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlStorage implements Storage {

    public final ConnectionFactory connectionFactory;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void clear() {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement("delete from resume")) {
            ps.execute();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public void update(Resume resume) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement("update resume set uuid = ? and full_name = ?" +
                     "where uuid = ? and full_name = ?")) {
            ps.setString(1, resume.getUuid());
            ps.setString(2, resume.getFullName());
            Resume resume1 = get(resume.getUuid());
            ps.setString(3, resume1.getUuid());
            ps.setString(4, resume1.getFullName());
            ps.execute();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public void save(Resume resume) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement("insert into resume(uuid, full_name) values (?,?)")) {
            ps.setString(1, resume.getUuid());
            if (ps.executeQuery().next()) {
                throw new ExistStorageException(resume.getUuid());
            }
            ps.setString(2, resume.getFullName());
            ps.execute();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public Resume get(String uuid) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement("select * from resume r where r.uuid = ?")) {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new NotExistStorageException(uuid);
            }
            return new Resume(uuid, rs.getNString("full_name"));
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public void delete(String uuid) {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement("delete from resume where uuid = ?")) {
            ps.setString(1, uuid);
            if (!ps.executeQuery().next()) {
                throw new NotExistStorageException(uuid);
            }
            ps.execute();
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public List<Resume> getSortedStorage() {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement("select from resume order by uuid")) {
            ResultSet rs = ps.executeQuery();
            List<Resume> storage = new ArrayList<>();
            while (rs.next()) {
                String uuid = rs.getNString(1);
                String fullName = rs.getNString(2);
                storage.add(new Resume(uuid, fullName));
            }
            return storage;
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public int size() {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement("select count(*) from resume")) {
            ResultSet rs = ps.executeQuery();
            return rs.last() ? rs.getRow() : 0;
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }
}
