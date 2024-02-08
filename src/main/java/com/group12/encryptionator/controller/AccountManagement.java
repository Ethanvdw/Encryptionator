package com.group12.encryptionator.controller;

import com.group12.encryptionator.database.UserDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class AccountManagement {
    public Label usernameLabel;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField oldPasswordField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField confirmNewPasswordField;
    @FXML
    private Label infoText;

    @FXML
    private String username;

    private final UserDao userDao = new UserDao();

    public void SetUsername(String username) {
        this.username = username;
        usernameLabel.setText("Username: " + username);
    }

    @FXML
    public void onChangeEmailClick(ActionEvent actionEvent) {
        String newEmail = emailField.getText();
        if (userDao.changeEmail(username, newEmail)) {
            infoText.setText("Email changed successfully!");
        } else {
            infoText.setText("Failed to change email!");
        }
    }

    @FXML
    public void onChangePasswordClick(ActionEvent actionEvent) {
        String oldPassword = userDao.hashPassword(oldPasswordField.getText());
        String newPassword = userDao.hashPassword(newPasswordField.getText());
        String confirmNewPassword = userDao.hashPassword(confirmNewPasswordField.getText());

        if (!newPassword.equals(confirmNewPassword)) {
            infoText.setText("New passwords do not match!");
            return;
        }

        if (userDao.changePassword(username, oldPassword, newPassword)) {
            infoText.setText("Password changed successfully!");
        } else {
            infoText.setText("Failed to change password!");
        }
    }

    @FXML
    public void onLogoutClick(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) infoText.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 320, 240);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

@FXML
public void onHomeClick(ActionEvent actionEvent) {
    try {
        Stage stage = (Stage) infoText.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/home.fxml"));
        Parent root = fxmlLoader.load();
        stage.setScene(new Scene(root));
        Home homeController = fxmlLoader.getController();
        homeController.initialize(username);
    } catch (IOException e) {
        e.printStackTrace();
    }
}
}