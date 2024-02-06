package com.ethan.encryptionator.controller;

import com.ethan.encryptionator.database.FileDao;
import com.ethan.encryptionator.database.UserDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddFile {
    @FXML
    private TextField fileNameField;
    @FXML
    private TextField fileTypeField;
    @FXML
    private TextField fileSizeField;
    @FXML
    private TextField filePathField;
    @FXML
    private TextField uploadDateField;
    private String username;
    private Home homeController;

    public void setHomeController(Home homeController) {
        this.homeController = homeController;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void addFile(ActionEvent actionEvent) {
        String fileName = fileNameField.getText();
        UserDao userDao = new UserDao();
        int userId = userDao.getUserId(username);

        FileDao fileDao = new FileDao();
        if (fileDao.fileExists(fileName, userId)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("File Already Exists");
            alert.setContentText("A file with the same name already exists for this user.");
            alert.showAndWait();
        } else {
            String fileType = fileTypeField.getText();
            String fileSize = fileSizeField.getText();
            String filePath = filePathField.getText();

            fileDao.register(fileName, fileType, fileSize, filePath, userId);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("File has been successfully added.");
            alert.showAndWait();

            Stage stage = (Stage) fileNameField.getScene().getWindow();
            stage.close();
        }
        homeController.initialize(username);
    }
}

