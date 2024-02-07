package com.ethan.encryptionator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {

    public static void editFileContent(String filePath) {
        Path path = Paths.get(filePath);
        // Check if the directory exists, if not create it
        if (!Files.exists(path.getParent())) {
            try {
                Files.createDirectories(path.getParent());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Check if the file exists, if not create it
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            // Open the file with xdg-open
            ProcessBuilder processBuilder = new ProcessBuilder("xdg-open", filePath);
            processBuilder.start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}