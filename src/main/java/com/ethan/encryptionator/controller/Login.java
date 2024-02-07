package com.ethan.encryptionator.controller;

import com.ethan.encryptionator.database.UserDao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class Login {
    @FXML
    private Label welcomeText;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    private final UserDao userDao = new UserDao();

    @FXML
    protected void onLoginButtonClick() throws IOException {
        String username = usernameField.getText();
        String password = userDao.hashPassword(passwordField.getText());

        if (userDao.login(username, password)) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/home.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Home homeController = fxmlLoader.getController();
            homeController.initialize(username);
            Stage stage = (Stage) welcomeText.getScene().getWindow();
            stage.setScene(scene);
        } else {
            welcomeText.setText("Invalid username or password!");
        }
    }

    @FXML
    private void onRegisterLinkClick() throws IOException {
        Stage stage = (Stage) welcomeText.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/register.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }
}