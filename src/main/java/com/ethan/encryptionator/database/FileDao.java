package com.ethan.encryptionator.database;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
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
        List<String> files = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(CONNECTION_URL);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, user_id);
            var rs = stmt.executeQuery();
            while (rs.next()) {
                files.add(rs.getString("file_name"));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }
        return files;
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
}

