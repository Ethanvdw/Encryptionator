package com.ethan.encryptionator.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private static final String CONNECTION_URL = "jdbc:sqlite:file_management.db";

    public void initialize() {
        try (Connection conn = DriverManager.getConnection(CONNECTION_URL);
             Statement stmt = conn.createStatement()) {

            createUsersTable(stmt);
            createFilesTable(stmt);
            createFoldersTable(stmt);
            createFileFolderRelationTable(stmt);
            createPermissionsTable(stmt);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void executeSQL(Statement stmt, String sql) {
        try {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void createUsersTable(Statement stmt) {
        String sql = "CREATE TABLE IF NOT EXISTS Users (" +
                "user_id INTEGER PRIMARY KEY," +
                "username TEXT NOT NULL," +
                "password TEXT NOT NULL," +
                "email TEXT NOT NULL" +
                ");";
        executeSQL(stmt, sql);
    }

    private void createFilesTable(Statement stmt) {
        String sql = "CREATE TABLE IF NOT EXISTS Files (" +
                "file_id INTEGER PRIMARY KEY," +
                "file_name TEXT NOT NULL," +
                "file_type TEXT NOT NULL," +
                "file_size INTEGER NOT NULL," +
                "file_path TEXT NOT NULL," +
                "upload_date TEXT NOT NULL," +
                "user_id INTEGER," +
                "FOREIGN KEY(user_id) REFERENCES Users(user_id)" +
                ");";
        executeSQL(stmt, sql);
    }

    private void createFoldersTable(Statement stmt) {
        String sql = "CREATE TABLE IF NOT EXISTS Folders (" +
                "folder_id INTEGER PRIMARY KEY," +
                "folder_name TEXT NOT NULL," +
                "creation_date TEXT NOT NULL," +
                "user_id INTEGER," +
                "FOREIGN KEY(user_id) REFERENCES Users(user_id)" +
                ");";
        executeSQL(stmt, sql);
    }

    private void createFileFolderRelationTable(Statement stmt) {
        String sql = "CREATE TABLE IF NOT EXISTS File_Folder_Relation (" +
                "file_id INTEGER," +
                "folder_id INTEGER," +
                "FOREIGN KEY(file_id) REFERENCES Files(file_id)," +
                "FOREIGN KEY(folder_id) REFERENCES Folders(folder_id)" +
                ");";
        executeSQL(stmt, sql);
    }

    private void createPermissionsTable(Statement stmt) {
        String sql = "CREATE TABLE IF NOT EXISTS Permissions (" +
                "permission_id INTEGER PRIMARY KEY," +
                "user_id INTEGER," +
                "file_id INTEGER," +
                "permission_type TEXT NOT NULL," +
                "access_level TEXT," +
                "FOREIGN KEY(user_id) REFERENCES Users(user_id)," +
                "FOREIGN KEY(file_id) REFERENCES Files(file_id)," +
                "FOREIGN KEY(folder_id) REFERENCES Folders(folder_id)" +
                ");";
        executeSQL(stmt, sql);
    }
}