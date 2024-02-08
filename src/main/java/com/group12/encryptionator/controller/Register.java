package com.group12.encryptionator.controller;

import com.group12.encryptionator.database.UserDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class Register {
    @FXML
    public Label infoText;
    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public PasswordField confirmPasswordField;
    @FXML
    public TextField emailField;

    private final UserDao userDao = new UserDao();

    @FXML
public void onRegisterButtonClick() {
    String username = usernameField.getText();
    String password = passwordField.getText();
    String confirmPassword = confirmPasswordField.getText();
    String email = emailField.getText();

    if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()) {
        infoText.setText("All fields are required!");
        return;
    }

    if (!password.equals(confirmPassword)) {
        infoText.setText("Passwords do not match!");
        return;
    }

    String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
    if (!email.matches(emailRegex)) {
        infoText.setText("Invalid email format!");
        return;
    }

    if (userDao.usernameTaken(username)) {
        infoText.setText("Username is already taken!");
        return;
    }

    if (userDao.emailInUse(email)) {
        infoText.setText("Email is already in use!");
        return;
    }

    String hashedPassword = userDao.hashPassword(password);
    if (userDao.register(username, hashedPassword, email)) {
        infoText.setText("Registration successful!");
    } else {
        infoText.setText("Registration failed!");
    }
}

    public void onBackToLoginClick(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) infoText.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setScene(scene);
    }
}