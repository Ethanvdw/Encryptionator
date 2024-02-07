package com.ethan.encryptionator.controller;

import com.ethan.encryptionator.FileUtils;
import com.ethan.encryptionator.database.FileDao;
import com.ethan.encryptionator.database.UserDao;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AddFile {
    @FXML
    private TextField fileNameField;
    @FXML
    private TextField fileTypeField;
    @FXML
    private TextField fileSizeField;
    @FXML
    private TextField filePathField;
    private String username;
    private Home homeController;
    private String filePath;

    public void setHomeController(Home homeController) {
        this.homeController = homeController;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void addFile() throws IOException {
        String fileName = fileNameField.getText();
        int userId = getUserId();
        FileDao fileDao = new FileDao();

        if (fileDao.fileExists(fileName, userId)) {
            showAlert(Alert.AlertType.ERROR, "Error", "File Already Exists", "A file with the same name already exists for this user.");
        } else {
            String fileType = fileTypeField.getText();
            filePath = "/app/" + userId; // Set the file path to /app/ followed by the user's id

            createDirectoryIfNotExists(filePath);

            long fileSize = Files.size(Paths.get(filePath));

            fileDao.register(fileName, fileType, String.valueOf(fileSize), filePath, userId);

            showAlert(Alert.AlertType.INFORMATION, "Success", null, "File has been successfully added.");

            closeWindow();
        }
        homeController.initialize(username);
    }

    @FXML
    public void editFileContent() {
        String fileName = fileNameField.getText();
        int userId = getUserId();
        filePath = "app/" + userId + "/" + fileName + ".txt"; // Add the file extension if needed

        FileUtils.editFileContent(filePath);
    }

    @FXML
    public void browseFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
                new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            registerFile(selectedFile);
            closeWindow();
            showAlert(Alert.AlertType.INFORMATION, "Success", null, "File has been successfully added.");
        }
    }

    private void registerFile(File file) throws IOException {
        String fileName = file.getName();
        String fileType = getFileExtension(file);
        long fileSize = Files.size(file.toPath());
        String filePath = file.getAbsolutePath();

        int userId = getUserId();
        String userDirectory = "app/" + userId;

        createDirectoryIfNotExists(userDirectory);

        Path sourcePath = file.toPath();
        Path targetPath = Paths.get(userDirectory, fileName);

        Files.move(sourcePath, targetPath);

        filePath = targetPath.toString();

        FileDao fileDao = new FileDao();
        fileDao.register(fileName, fileType, String.valueOf(fileSize), filePath, userId);

        homeController.initialize(username);
    }

    private int getUserId() {
        UserDao userDao = new UserDao();
        return userDao.getUserId(username);
    }

    private void createDirectoryIfNotExists(String path) {
        Path directoryPath = Paths.get(path);
        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) fileNameField.getScene().getWindow();
        stage.close();
    }

    private String getFileExtension(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }
}