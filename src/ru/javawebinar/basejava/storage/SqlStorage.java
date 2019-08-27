package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.sql.ConnectionFactory;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.*;
import java.util.*;

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
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("UPDATE resume SET full_name = ? WHERE uuid = ?")) {
                ps.setString(1, resume.getFullName());
                ps.setString(2, resume.getUuid());
                if (ps.executeUpdate() == 0) {
                    throw new NotExistStorageException(resume.getUuid());
                }
            }
            deleteContacts(conn, resume);
            deleteSections(conn, resume);
            saveContacts(conn, resume);
            saveSections(conn, resume);
            return null;
        });
    }

    @Override
    public void save(Resume resume) {
        sqlHelper.transactionalExecute(conn -> {
            String uuid = resume.getUuid();
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resume (uuid, full_name) VALUES (?,?)")) {
                ps.setString(1, uuid);
                ps.setString(2, resume.getFullName());
                ps.execute();
            }
            saveContacts(conn, resume);
            saveSections(conn, resume);
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.transactionalExecute(conn -> {
            Resume resume;
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume r WHERE r.uuid = ?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new NotExistStorageException(uuid);
                }
                resume = new Resume(uuid, rs.getString("full_name"));
            }
            getContacts(conn, resume);
            getSections(conn, resume);
            return resume;
        });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.execute("DELETE FROM resume WHERE uuid=?", ps -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public List<Resume> getSortedStorage() {
        Map<String, Resume> resumeMap = sqlHelper.transactionalExecute(connection -> {
            Map<String, Resume> map = new LinkedHashMap<>();
            try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM resume ORDER BY full_name, uuid")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    map.put(rs.getString("uuid"),
                            new Resume(rs.getString("uuid"), rs.getString("full_name")));
                }
            }

            try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM contact")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String uuid = rs.getString("resume_uuid");
                    map.get(uuid).setContacts(ContactType.valueOf(rs.getString("type")),
                            rs.getString("value"));
                }
            }

            try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM section")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String uuid = rs.getString("resume_uuid");
                    SectionType sectionType = SectionType.valueOf(rs.getString("type"));
                    setSection(map.get(uuid), rs, sectionType);
                }
            }
            return map;
        });
        return new ArrayList<>(resumeMap.values());

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

    private void saveContacts(Connection conn, Resume resume) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO contact (resume_uuid, type, value) VALUES (?,?,?)")) {
            for (Map.Entry<ContactType, String> e : resume.getContacts().entrySet()) {
                ps.setString(1, resume.getUuid());
                ps.setString(2, e.getKey().name());
                ps.setString(3, e.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void deleteContacts(Connection conn, Resume resume) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM contact WHERE resume_uuid = ?")) {
            ps.setString(1, resume.getUuid());
            ps.execute();
        }
    }

    private void getContacts(Connection conn, Resume resume) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM contact WHERE resume_uuid = ? AND value IS NOT NULL")) {
            ps.setString(1, resume.getUuid());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                resume.setContacts(ContactType.valueOf(rs.getString("type")), rs.getString("value"));
            }
        }
    }

    private void saveSections(Connection conn, Resume resume) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO section (resume_uuid, type, value) VALUES (?,?,?)")) {
            for (Map.Entry<SectionType, AbstractSection> e : resume.getSections().entrySet()) {
                SectionType sectionType = e.getKey();
                switch (sectionType) {
                    case OBJECTIVE:
                    case PERSONAL:
                    case QUALIFICATION:
                    case ACHIEVEMENT:
                        ps.setString(1, resume.getUuid());
                        ps.setString(2, sectionType.name());
                        ps.setString(3, e.getValue().toString());
                        ps.addBatch();
                }
            }
            ps.executeBatch();
        }
    }

    private void deleteSections(Connection conn, Resume resume) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM section WHERE resume_uuid = ?")) {
            ps.setString(1, resume.getUuid());
            ps.execute();
        }
    }

    private void getSections(Connection conn, Resume resume) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM section WHERE resume_uuid = ? AND value IS NOT NULL")) {
            ps.setString(1, resume.getUuid());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SectionType sectionType = SectionType.valueOf(rs.getString("type"));
                setSection(resume, rs, sectionType);
            }
        }
    }

    private void setSection(Resume resume, ResultSet rs, SectionType sectionType) throws SQLException {
        switch (sectionType) {
            case OBJECTIVE:
            case PERSONAL:
                resume.setSections(sectionType, new TextSection(rs.getString("value")));
                break;
            case QUALIFICATION:
            case ACHIEVEMENT:
                String[] values = rs.getString("value").split("\n");
                resume.setSections(sectionType, new ListSection(Arrays.asList(values)));
                break;
        }
    }
}