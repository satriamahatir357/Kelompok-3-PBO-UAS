package com.produk;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Load layout utama yang sudah kita buat tadi
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("main-layout.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 850, 550);
        
        stage.setTitle("Sistem Manajemen Produk v1.0");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}