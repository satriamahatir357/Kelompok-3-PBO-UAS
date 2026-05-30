package com.produk;

// Mengimport semua library JavaFX yang dibutuhkan untuk perpindahan halaman
import java.io.IOException;

import com.produk.daftar.DaftarController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader; // Jangan lupa import Button agar tidak error
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class MainAppController {

    // 1. KUNCI UTAMA: Menyimpan instance global agar bisa dipanggil oleh controller lain
    private static MainAppController instance;

    // Menghubungkan variabel ini dengan <StackPane fx:id="contentArea"> yang ada di main-layout.fxml
    @FXML
    private StackPane contentArea;

    // Menghubungkan komponen tombol navigasi sidebar sesuai fx:id di main-layout.fxml
    @FXML private Button btnNavHome;
    @FXML private Button btnNavTambah;
    @FXML private Button btnNavDaftar;
    @FXML private Button btnNavDiskon;
    @FXML private Button btnNavAbout;

    // Method initialize() otomatis berjalan pertama kali saat layout utama berhasil dimuat
    @FXML
    public void initialize() {
        // Mencatat instance controller utama saat aplikasi pertama kali disetup
        instance = this;
        
        // Saat aplikasi pertama kali terbuka, langsung arahkan ke halaman Home
        tampilkanHome();
    }

    // 2. KUNCI KEDUA: Method Getter untuk membagikan instance MainAppController ke HomeController
    public static MainAppController getInstance() {
        return instance;
    }

    // Fungsi pembantu untuk memindahkan style class "active" (Kapsul Putih) secara dinamis
    private void aturTombolAktif(Button tombolAktif) {
        Button[] semuaTombol = {btnNavHome, btnNavTambah, btnNavDaftar, btnNavDiskon, btnNavAbout};
        
        for (Button btn : semuaTombol) {
            if (btn != null) {
                // Bersihkan class active dari semua tombol sidebar terlebih dahulu
                btn.getStyleClass().remove("sidebar-button-active");
            }
        }
        
        if (tombolAktif != null) {
            // Pasang class active ke tombol yang halamannya sedang dibuka saat ini
            tombolAktif.getStyleClass().add("sidebar-button-active");
        }

        // SOLUSI UTAMA: Paksa fokus keluar dari sidebar dan pindah ke area konten kanan
        if (contentArea != null) {
            contentArea.requestFocus();
        }
    }

    // Fungsi untuk memuat halaman Home dengan fitur Auto-Refresh Data
    @FXML
    public void tampilkanHome() {
        try {
            // 1. Siapkan FXMLLoader secara manual agar kita bisa berinteraksi dengan controllernya
            FXMLLoader loader = new FXMLLoader(getClass().getResource("home/home-view.fxml"));
            Parent halamanHome = loader.load();
            
            // 2. Ambil "otak" atau controller dari halaman Home tersebut
            com.produk.home.HomeController homeController = loader.getController();
            
            // 3. PAKSA REFRESH: Perintahkan controller home untuk menghitung ulang ArrayList detik ini juga
            homeController.refreshDataData();
            
            // 4. Masukkan halaman home ke dalam area konten utama di sebelah kanan
            contentArea.getChildren().setAll(halamanHome);
            
            // 5. SINKRONISASI NAVIGASI: Set tombol Home menjadi putih aktif
            aturTombolAktif(btnNavHome);
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
            
            // SINKRONISASI NAVIGASI: Set tombol Tambah menjadi putih aktif
            aturTombolAktif(btnNavTambah);
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
            
            // SINKRONISASI NAVIGASI: Set tombol About menjadi putih aktif
            aturTombolAktif(btnNavAbout);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Gagal memuat halaman about!");
        }
    }

    // Fungsi untuk memuat halaman Diskon Produk
    // Fungsi untuk memuat halaman Diskon Produk dengan Auto-Refresh Data
    @FXML
    public void tampilkanDiskon() {
        try {
            // Gunakan FXMLLoader manual agar bisa melakukan sinkronisasi data repositori sebelum scene dirender
            FXMLLoader loader = new FXMLLoader(getClass().getResource("diskon/diskon-view.fxml"));
            Parent halamanDiskon = loader.load();
            
            // Masukkan halaman diskon ke dalam area konten utama
            contentArea.getChildren().setAll(halamanDiskon);
            
            // SINKRONISASI NAVIGASI: Set tombol Diskon menjadi putih aktif
            aturTombolAktif(btnNavDiskon);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Gagal memuat halaman diskon produk!");
        }
    }

    // Fungsi untuk memuat halaman Lihat Daftar Produk
    // Fungsi untuk memuat halaman Lihat Daftar Produk (Sudah diperbaiki ke PUBLIC dan CONTENTAREA)
    @FXML
    public void tampilkanDaftar() {
        try {
            // Menggunakan jalur relative path agar aman dan konsisten dengan halaman lain
            FXMLLoader loader = new FXMLLoader(getClass().getResource("daftar/daftar-view.fxml"));
            Parent view = loader.load();
            
            // 1. Ambil instance controller dari fxml yang baru dimuat
            DaftarController controller = loader.getController();
            
            // 2. JALANKAN TRIGGER REFRESH AGAR DATA TERBARU DARI TAMBAH PRODUK MASUK
            controller.muatUlangData();
            
            // 3. Masukkan halaman ke penampung StackPane (contentArea) di sebelah kanan
            contentArea.getChildren().setAll(view);
            
            // 4. SINKRONISASI NAVIGASI: Set tombol Lihat Daftar menjadi putih aktif
            aturTombolAktif(btnNavDaftar);
            
        } catch (IOException e) {
            System.out.println("Gagal memuat halaman lihat daftar!");
            e.printStackTrace();
        }
    }
}