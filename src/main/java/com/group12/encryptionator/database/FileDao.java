package com.group12.encryptionator.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileDao {

    private static final String CONNECTION_URL = "jdbc:sqlite:file_management.db";
    private static final Logger LOGGER = Logger.getLogger(FileDao.class.getName());


    public void register(String file_name, String file_type, String file_size, String file_path, int user_id) {
        String sql = "INSERT INTO Files(file_name, file_type, file_size, file_path, upload_date, user_id) VALUES(?, ?, ?, ?, ?, ?)";

        LocalDateTime now = LocalDateTime.now();
        String upload_date = now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        try (Connection conn = DriverManager.getConnection(CONNECTION_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, file_name);
            stmt.setString(2, file_type);
            stmt.setString(3, file_size);
            stmt.setString(4, file_path);
            stmt.setString(5, upload_date);
            stmt.setInt(6, user_id);

            stmt.executeUpdate();


        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }
    }

    public boolean fileExists(String file_name, int user_id) {
        String query = "SELECT * FROM Files WHERE file_name = ? AND user_id = ?";
        try (Connection conn = DriverManager.getConnection(CONNECTION_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, file_name);
            stmt.setInt(2, user_id);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }
        return false;
    }

    public List<String> getFiles(int user_id) {
        String query = "SELECT file_name FROM Files WHERE user_id = ?";
        return getStrings(user_id, query);
    }

    public void deleteFile(String selectedFile, int user_id) {
        String sql = "DELETE FROM Files WHERE file_name = ? AND user_id = ?";

        try (Connection conn = DriverManager.getConnection(CONNECTION_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, selectedFile);
            stmt.setInt(2, user_id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }
    }

    public String getFilePath(String fileName, int userId) {
        String query = "SELECT file_path FROM Files WHERE file_name = ? AND user_id = ?";
        try (Connection conn = DriverManager.getConnection(CONNECTION_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, fileName);
            stmt.setInt(2, userId);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("file_path");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }
        return null;
    }


    private List<String> getStrings(int userId, String query) {
        List<String> files = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(CONNECTION_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            var rs = stmt.executeQuery();
            while (rs.next()) {
                files.add(rs.getString("file_name"));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }
        return files;
    }


    public int getFileId(String fileName, int userId) {
        String query = "SELECT file_id FROM Files WHERE file_name = ? AND user_id = ?";
        try (Connection conn = DriverManager.getConnection(CONNECTION_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, fileName);
            stmt.setInt(2, userId);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("file_id");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }
        return -1;
    }
}
