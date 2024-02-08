package com.group12.encryptionator.controller;

import com.group12.encryptionator.database.FileDao;
import com.group12.encryptionator.database.PermissionDao;
import com.group12.encryptionator.database.UserDao;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.List;

public class ShareFile {
    public Label infoText;
    public Label usernameLabel;
    public Label fileNameLabel;
    public ComboBox userList;
    public ComboBox permissionList;
    public Button shareButton;
    private String username;

    private Home homeController;

    private String sharedFile;

    @FXML
    private Button backButton;

    private final UserDao userDao = new UserDao();

    public void setUsername(String username) {
        this.username = username;
        this.usernameLabel.setText(username);
    }

    private void setSharedFile(String sharedFile) {
        this.sharedFile = sharedFile;
        this.fileNameLabel.setText(sharedFile);
    }

    public void setHomeController(Home home) {
        this.homeController = home;
    }

    public void initialize(String username, String sharedFile) {
        setUsername(username);
        setSharedFile(sharedFile);
        setHomeController(homeController);
        populateUserDropdown();
        populatePermissionDropdown();
    }

    private void populatePermissionDropdown() {
        List<String> permissions = List.of("Read", "Write", "Read/Write");
        permissionList.getItems().clear();
        permissionList.getItems().addAll(permissions);
    }

    private void populateUserDropdown() {
        List<String> users = List.of(userDao.getAllUsernames());
        userList.getItems().clear();
        userList.getItems().addAll(users);
    }


    @FXML
    public void onBackButtonClick() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();
    }

    public void onShareButtonClick() {
        String selectedUser = userList.getSelectionModel().getSelectedItem().toString();
        String selectedPermission = permissionList.getSelectionModel().getSelectedItem().toString();

        int sharerId = userDao.getUserId(username);
        int recipientId = userDao.getUserId(selectedUser);
        FileDao fileDao = new FileDao();
        int fileId = fileDao.getFileId(sharedFile, sharerId);

        PermissionDao.shareFile(sharerId, recipientId, fileId, selectedPermission);

        infoText.setText("File shared successfully with " + selectedUser);
    }
}