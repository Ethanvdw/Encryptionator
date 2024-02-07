package com.ethan.encryptionator.controller;

import com.ethan.encryptionator.FileUtils;
import com.ethan.encryptionator.database.FileDao;
import com.ethan.encryptionator.database.PermissionDao;
import com.ethan.encryptionator.database.UserDao;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class Home {

    public Button accountManagementButton;
    public ListView<String> fileListView;
    public Button addFileButton;
    public ListView sharedFileListView;
    private String username;
    @FXML
    private Label welcomeText;

    private int user_id;

    public void initialize(String username) {
        this.username = username;

        UserDao userDao = new UserDao();
        this.user_id = userDao.getUserId(username);
        welcomeText.setText("Welcome, " + username + "!");

        populateFileList();
        populateSharedFileList();

        fileListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            if (event.getClickCount() == 2) {
                editFile();
            }
            }
        });
    }

    private void populateFileList() {
        FileDao fileDao = new FileDao();
        List<String> files = fileDao.getFiles(user_id);
        fileListView.getItems().clear();
        fileListView.getItems().addAll(files);
    }

    public void populateSharedFileList() {
        List<String> sharedFiles = new PermissionDao().getSharedFiles(user_id);
        sharedFileListView.getItems().clear();
        sharedFileListView.getItems().addAll(sharedFiles);
    }

    @FXML
    public void onAccountManagementClick() {
        try {
            Stage stage = (Stage) welcomeText.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/account_management.fxml"));
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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/add_file.fxml"));
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

    public void editFile() {
        // Select file from list
        String selectedFile = fileListView.getSelectionModel().getSelectedItem();
        FileDao fileDao = new FileDao();
        String filePath = "app/" + user_id + "/" + selectedFile;

        // Open the file in the default text editor
        FileUtils.editFileContent(filePath);
    }

    @FXML
    public void openShareView() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/share_file.fxml"));
            Parent root = fxmlLoader.load();

            ShareFile controller = fxmlLoader.getController();
            controller.initialize(username, fileListView.getSelectionModel().getSelectedItem()); // Pass the Home controller instance

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}