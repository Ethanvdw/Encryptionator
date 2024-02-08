module com.group12.encryptionator {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.group12.encryptionator to javafx.fxml;
    exports com.group12.encryptionator;
    exports com.group12.encryptionator.database;
    opens com.group12.encryptionator.database to javafx.fxml;
    exports com.group12.encryptionator.controller;
    opens com.group12.encryptionator.controller to javafx.fxml;
}