package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistStorageException;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.sql.ConnectionFactory;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.*;
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
        sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement("UPDATE resume SET full_name = ? WHERE uuid = ?")) {
                ps.setString(1, resume.getFullName());
                ps.setString(2, resume.getUuid());
                if (ps.executeUpdate() == 0) {
                    throw new NotExistStorageException(resume.getUuid());
                }
            }
            deleteContacts(conn, resume);
            saveContacts(conn, resume);
            deleteSections(conn, resume);
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
            try (PreparedStatement ps = conn.prepareStatement("" +
                    " SELECT * FROM resume r WHERE r.uuid = ? ")) {
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
        return sqlHelper.transactionalExecute(conn -> {
            List<Resume> storage = new ArrayList<>();
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume r ORDER BY full_name, uuid ")) {
                ps.execute();
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Resume resume = new Resume(rs.getString("uuid"), rs.getString("full_name"));
                    getContacts(conn, resume);
                    getSections(conn, resume);
                    storage.add(resume);
                }
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

    private void saveSections(Connection conn, Resume resume) throws SQLException {
        for (Map.Entry<SectionType, AbstractSection> e : resume.getSections().entrySet()) {
            SectionType sectionType = e.getKey();
            switch (sectionType) {
                case OBJECTIVE:
                case PERSONAL:
                    saveTextSections(conn, resume, sectionType, e.getValue().toString());
            }
        }
    }

    private void saveTextSections(Connection conn, Resume resume, SectionType sectionType, String value) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO text_section (resume_uuid, type, value) VALUES (?,?,?)")) {
            ps.setString(1, resume.getUuid());
            ps.setString(2, sectionType.name());
            ps.setString(3, value);
            ps.execute();
        }
    }

    private void deleteContacts(Connection conn, Resume resume) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM contact WHERE resume_uuid=?")) {
            ps.setString(1, resume.getUuid());
            ps.execute();
        }
    }

    private void deleteSections(Connection conn, Resume resume) throws SQLException {
        for (Map.Entry<SectionType, AbstractSection> e : resume.getSections().entrySet()) {
            SectionType sectionType = e.getKey();
            String uuid = resume.getUuid();
            switch (sectionType) {
                case OBJECTIVE:
                case PERSONAL:
                    deleteSection("DELETE FROM text_section WHERE resume_uuid = ?", conn, uuid);
            }
        }
    }

    private void deleteSection(String sqlCommand, Connection conn, String uuid) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sqlCommand)) {
            ps.setString(1, uuid);
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

    private void getSections(Connection conn, Resume resume) throws SQLException {
        for (Map.Entry<SectionType, AbstractSection> e : resume.getSections().entrySet()) {
            SectionType sectionType = e.getKey();
            switch (sectionType) {
                case OBJECTIVE:
                case PERSONAL:
                    getTextSections(conn, resume);
            }
        }
    }

    private void getTextSections(Connection conn, Resume resume) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM text_section WHERE resume_uuid = ?")) {
            ps.setString(1, resume.getUuid());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                resume.setSections(SectionType.valueOf(rs.getString("type")), new TextSection(rs.getString("value")));
            }
        }
    }
}