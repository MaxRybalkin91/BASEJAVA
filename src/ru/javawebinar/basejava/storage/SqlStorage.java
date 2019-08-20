package ru.javawebinar.basejava.storage;

import org.postgresql.util.PSQLException;
import ru.javawebinar.basejava.exception.ExistStorageException;
import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.ContactType;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.ConnectionFactory;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SqlStorage implements Storage {

    private SqlHelper sqlHelper;

    SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        ConnectionFactory connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        sqlHelper = new SqlHelper(connectionFactory);
    }

    @Override
    public void clear() {
        sqlHelper.execute("DELETE FROM resume", PreparedStatement::execute);
    }

    @Override
    public void update(Resume resume) {
        String uuid = resume.getUuid();
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE resume SET full_name = ? WHERE uuid = ?")) {
                ps.setString(1, resume.getFullName());
                ps.setString(2, uuid);
                int result = ps.executeUpdate();
                if (result == 0) {
                    throw new NotExistStorageException(uuid);
                }
            }
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE contact SET type = ?, value = ? WHERE resume_uuid = ?")) {
                for (Map.Entry<ContactType, String> e : resume.getContacts().entrySet()) {
                    ps.setString(1, e.getKey().name());
                    ps.setString(2, e.getValue());
                    ps.setString(3, uuid);
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            return null;
        });
    }

    @Override
    public void save(Resume resume) {
        String uuid = resume.getUuid();
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
                ps.setString(1, uuid);
                ps.setString(2, resume.getFullName());
                try {
                    ps.execute();
                } catch (PSQLException e) {
                    if (e.getSQLState().equals("23505")) {
                        throw new ExistStorageException(uuid);
                    }
                }
            }
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)")) {
                for (Map.Entry<ContactType, String> e : resume.getContacts().entrySet()) {
                    ps.setString(1, uuid);
                    ps.setString(2, e.getKey().name());
                    ps.setString(3, e.getValue());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.execute("" +
                        " SELECT * FROM resume r\n " +
                        " LEFT JOIN contact c " +
                        " ON  r.uuid = c.resume_uuid " +
                        " WHERE r.uuid = ? ",
                ps -> {
                    ps.setString(1, uuid);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new NotExistStorageException(uuid);
                    }
                    Resume resume = new Resume(uuid, rs.getString("full_name"));
                    do {
                        ContactType contactType = ContactType.valueOf(rs.getString("type"));
                        String value = rs.getString("value");
                        resume.setContacts(contactType, value);
                    } while (rs.next());
                    return resume;
                });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.execute("DELETE FROM resume WHERE uuid = ?",
                ps -> {
                    ps.setString(1, uuid);
                    if (ps.executeUpdate() == 0) {
                        throw new NotExistStorageException(uuid);
                    }
                    return null;
                });
    }

    @Override
    public List<Resume> getSortedStorage() {
        return sqlHelper.execute("SELECT * FROM resume ORDER BY full_name, uuid",
                ps -> {
                    ResultSet rs = ps.executeQuery();
                    List<Resume> storage = new ArrayList<>();
                    while (rs.next()) {
                        String uuid = rs.getString("uuid");
                        String fullName = rs.getString("full_name");
                        Resume resume = new Resume(uuid, fullName);
                        storage.add(sqlHelper.execute("SELECT * FROM contact WHERE resume_uuid = ?",
                                ps1 -> {
                                    ps1.setString(1, uuid);
                                    ResultSet rs1 = ps1.executeQuery();
                                    while (rs1.next()) {
                                        ContactType contactType = ContactType.valueOf(rs1.getString("type"));
                                        String value = rs1.getString("value");
                                        resume.setContacts(contactType, value);
                                    }
                                    return resume;
                                }));
                    }
                    return storage;
                });
    }

    @Override
    public int size() {
        return sqlHelper.execute("SELECT COUNT(uuid) FROM resume",
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