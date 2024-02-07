package com.ethan.encryptionator.database;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDao {
    private static final String CONNECTION_URL = "jdbc:sqlite:file_management.db";
    private static final Logger LOGGER = Logger.getLogger(UserDao.class.getName());

    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            LOGGER.log(Level.SEVERE, "Could not hash password: " + e.getMessage());
            return null;
        }
    }

    public int getUserId(String username) {
        String sql = "SELECT user_id FROM Users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(CONNECTION_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("user_id");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }
        return -1;
    }


    public void insertUser(String username, String password, String email) {
        String sql = "INSERT INTO Users(username, password, email) VALUES(?, ?, ?)";

        executeUpdate(email, username, password, sql);
    }

    public void deleteUser(String username) {
        String sql = "DELETE FROM Users WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(CONNECTION_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }
    }

    public void updateUser(String username, String password, String email) {
        String sql = "UPDATE Users SET password = ?, email = ? WHERE username = ?";

        executeUpdate(username, password, email, sql);
    }

    private void executeUpdate(String username, String password, String email, String sql) {
        try (Connection conn = DriverManager.getConnection(CONNECTION_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, password);
            stmt.setString(2, email);
            stmt.setString(3, username);
            stmt.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }
    }

    public String[] getUser(String username) {
        String sql = "SELECT * FROM Users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(CONNECTION_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new String[]{rs.getString("username"), rs.getString("password"), rs.getString("email")};
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }
        return null;
    }

    public List<String[]> getAllUsers() {
        List<String[]> users = new ArrayList<>();
        String sql = "SELECT * FROM Users";

        try (Connection conn = DriverManager.getConnection(CONNECTION_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                users.add(new String[]{
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email")});
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }
        return users;
    }

    public boolean login(String username, String password) {
        String sql = "SELECT * FROM Users WHERE username = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(CONNECTION_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }
        return false;
    }

    public boolean register(String username, String password, String email) {
        String sql = "SELECT 1 FROM Users WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(CONNECTION_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return false;
            }

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }

        insertUser(username, password, email);
        return true;
    }

    public boolean usernameTaken(String username) {
        String sql = "SELECT 1 FROM Users WHERE username = ?";

        return recordExists(username, sql);
    }

    public boolean emailInUse(String email) {
        String sql = "SELECT 1 FROM Users WHERE email = ?";

        return recordExists(email, sql);
    }

    private boolean recordExists(String email, String sql) {
        try (Connection conn = DriverManager.getConnection(CONNECTION_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return true;
            }

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }
        return false;
    }

public boolean changeEmail(String username, String newEmail) {
    if (emailInUse(newEmail)) {
        return false;
    }

    String sql = "UPDATE Users SET email = ? WHERE username = ?";
    return executeUpdate(username, newEmail, sql);
}

    public boolean changePassword(String username, String oldPassword, String newPassword) {
    if (!login(username, oldPassword)) {
        return false;
    }

    String sql = "UPDATE Users SET password = ? WHERE username = ?";
        return executeUpdate(username, newPassword, sql);
    }

    private boolean executeUpdate(String username, String newPassword, String sql) {
        try (Connection conn = DriverManager.getConnection(CONNECTION_URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newPassword);
            stmt.setString(2, username);
            stmt.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }
        return true;
    }

    public String[] getAllUsernames() {
        List<String[]> users = getAllUsers();
        String[] usernames = new String[users.size()];
        for (int i = 0; i < users.size(); i++) {
            usernames[i] = users.get(i)[0];
        }
        return usernames;
    }

}

