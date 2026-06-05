package com.produk;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import java.io.IOException;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // 1. Tampilkan Halaman Loading Terlebih Dahulu
        FXMLLoader loadingLoader = new FXMLLoader(getClass().getResource("/com/produk/loading-view.fxml"));
        Scene loadingScene = new Scene(loadingLoader.load());
        
        stage.initStyle(StageStyle.UNDECORATED); // Window polos tanpa border
        stage.setScene(loadingScene);
        stage.show(); // Ambil tindakan untuk langsung memunculkan layar loading

        // 2. Gunakan Timeline untuk membuat delay 3 detik secara asynchronous (aman untuk UI)
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
            try {
                // Jalankan ini SETELAH 3 detik: Load halaman utama kamu (850 x 550)
                FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/com/produk/main-layout.fxml"));
                Scene mainScene = new Scene(mainLoader.load(), 850, 550);
                
                Stage mainStage = new Stage();
                mainStage.setTitle("Sistem Manajemen Produk v1.0");
                mainStage.setScene(mainScene);
                
                // Tampilkan halaman utama dan tutup loading screen-nya
                mainStage.show();
                stage.close(); 
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        
        // Jalankan timer delay-nya
        timeline.play();
    }

    public static void main(String[] args) {
        launch();
    }
}