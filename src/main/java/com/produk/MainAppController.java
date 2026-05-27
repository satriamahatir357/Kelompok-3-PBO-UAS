package com.produk;

// Mengimport semua library JavaFX yang dibutuhkan untuk perpindahan halaman
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import java.io.IOException;

public class MainAppController {

    // Menghubungkan variabel ini dengan <StackPane fx:id="contentArea"> yang ada di main-layout.fxml
    @FXML
    private StackPane contentArea;

    // Method initialize() otomatis berjalan pertama kali saat layout utama berhasil dimuat
    @FXML
    public void initialize() {
        // Saat aplikasi pertama kali terbuka, langsung arahkan ke halaman Home
        tampilkanHome();
    }

    // Fungsi untuk memuat halaman Home
    @FXML
    public void tampilkanHome() {
        try {
            // Membaca file fxml halaman home dari foldernya sendiri
            Parent halamanHome = FXMLLoader.load(getClass().getResource("home/home-view.fxml"));
            // Memasukkan halaman home ke dalam area konten utama di sebelah kanan
            contentArea.getChildren().setAll(halamanHome);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Gagal memuat halaman home!");
        }
    }

    // Fungsi untuk memuat halaman Tambah Produk
    @FXML
    public void tampilkanTambah() {
        try {
            // Membaca file fxml yang berada di dalam folder tambah
            Parent halamanTambah = FXMLLoader.load(getClass().getResource("tambah/tambah-view.fxml"));
            // Ganti isi area konten utama dengan halaman tambah
            contentArea.getChildren().setAll(halamanTambah);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Gagal memuat halaman tambah produk!");
        }
    }

    // Fungsi untuk memuat halaman About
    @FXML
    public void tampilkanAbout() {
        try {
            Parent halamanAbout = FXMLLoader.load(getClass().getResource("about/about-view.fxml"));
            contentArea.getChildren().setAll(halamanAbout);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Gagal memuat halaman about!");
        }
    }

    // Fungsi untuk memuat halaman Diskon Produk
    @FXML
    public void tampilkanDiskon() {
        try {
            // Membaca file fxml yang berada di dalam folder diskon
            Parent halamanDiskon = FXMLLoader.load(getClass().getResource("diskon/diskon-view.fxml"));
            // Ganti isi area konten utama dengan halaman diskon
            contentArea.getChildren().setAll(halamanDiskon);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Gagal memuat halaman diskon produk!");
        }
    }

    // Fungsi untuk memuat halaman Lihat Daftar Produk
    @FXML
    public void tampilkanDaftar() {
        try {
            // Membaca file fxml yang berada di dalam folder daftar (akan kita buat nanti)
            Parent halamanDaftar = FXMLLoader.load(getClass().getResource("daftar/daftar-view.fxml"));
            contentArea.getChildren().setAll(halamanDaftar);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Gagal memuat halaman lihat daftar!");
        }
    }
} 