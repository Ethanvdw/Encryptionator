package com.group12.encryptionator.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PermissionDao {
    private static final String CONNECTION_URL = "jdbc:sqlite:file_management.db";
    private static final Logger LOGGER = Logger.getLogger(UserDao.class.getName());

    public static void shareFile(int sharerId, int recipientId, int fileId, String permissionType) {
        String sql = "INSERT INTO Permissions(sharer_id, recipient_id, file_id, permission_type) VALUES(?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(CONNECTION_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, sharerId);
            pstmt.setInt(2, recipientId);
            pstmt.setInt(3, fileId);
            pstmt.setString(4, permissionType);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to share file", e);
        }
    }


    public List<String> getSharedFiles(int recipientId) {
        List<String> sharedFiles = new ArrayList<>();
        String sql = "SELECT Files.file_id, file_name, file_type, file_size, file_path, upload_date FROM Files " +
                "JOIN Permissions ON Files.file_id = Permissions.file_id " +
                "WHERE Permissions.recipient_id = ?";

        try (Connection conn = DriverManager.getConnection(CONNECTION_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, recipientId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String fileDetails = "File ID: " + rs.getInt("file_id") +
                        ", File Name: " + rs.getString("file_name") +
                        ", File Type: " + rs.getString("file_type") +
                        ", File Size: " + rs.getInt("file_size") +
                        ", File Path: " + rs.getString("file_path") +
                        ", Upload Date: " + rs.getString("upload_date");
                sharedFiles.add(fileDetails);
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to retrieve shared files", e);
        }
        System.out.println("Shared files: " + sharedFiles);
        return sharedFiles;
    }
}


