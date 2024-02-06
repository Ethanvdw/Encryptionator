package com.ethan.encryptionator.controller;

import com.ethan.encryptionator.database.FileDao;
import com.ethan.encryptionator.database.UserDao;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class Home {

    public Button accountManagementButton;
    public ListView<String> fileListView;
    public Button addFileButton;
    private String username;
    @FXML
    private Label welcomeText;

    private int user_id;
    
    

    public List<String> getUserFiles(int user_id) {
        FileDao fileDao = new FileDao();
        return fileDao.getFiles(user_id);
    }

    public void initialize(String username) {
        this.username = username;

        UserDao userDao = new UserDao();
        this.user_id = userDao.getUserId(username);
        welcomeText.setText("Welcome, " + username + "!");

        List<String> files = getUserFiles(user_id);

        fileListView.getItems().clear();
        fileListView.getItems().addAll(files);
    }

    @FXML
    public void onAccountManagementClick() {
        try {
            Stage stage = (Stage) welcomeText.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/accountmanagement.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);

            AccountManagement accountManagement = fxmlLoader.getController();
            accountManagement.SetUsername(username);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

@FXML
public void openAddFileView() {
    try {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/addfile.fxml"));
        Parent root = fxmlLoader.load();

        AddFile controller = fxmlLoader.getController();
        controller.setUsername(username);
        controller.setHomeController(this); // Pass the Home controller instance

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();

    } catch (IOException e) {
        e.printStackTrace();
    }
}

@FXML
public void deleteFile() {
    String selectedFile = fileListView.getSelectionModel().getSelectedItem();
    FileDao fileDao = new FileDao();
    fileDao.deleteFile(selectedFile, user_id);
    initialize(username);

    // Display confirmation message
    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("File Deletion");
    alert.setHeaderText(null);
    alert.setContentText("File has been successfully deleted.");
    alert.showAndWait();
}

}