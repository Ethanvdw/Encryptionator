module com.ethan.encryptionator {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.ethan.encryptionator to javafx.fxml;
    exports com.ethan.encryptionator;
    exports com.ethan.encryptionator.database;
    opens com.ethan.encryptionator.database to javafx.fxml;
    exports com.ethan.encryptionator.controller;
    opens com.ethan.encryptionator.controller to javafx.fxml;
}