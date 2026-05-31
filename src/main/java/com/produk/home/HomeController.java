package com.produk.home;

import com.produk.MainAppController;
import com.produk.model.Produk;
import com.produk.model.DataRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox; // Diimport untuk menghubungkan box besar pembungkus diskon
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;

public class HomeController {

    @FXML private Label lblTotalProduk;
    @FXML private Label lblTotalStok;
    @FXML private Label lblProdukBaru;
    @FXML private TextField txtCari;

    // 1. HUBUNGKAN ID BOX BESAR: Pastikan di FXML, boks besar Produk Diskon diberi id="boxDiskon"
    @FXML private VBox boxDiskon;

    // initialize() otomatis berjalan saat halaman home dimuat pertama kali
    @FXML
    public void initialize() {
        refreshDataData();
        tampilkanDaftarDiskon(); // Jalankan fungsi untuk merender list produk diskon
    }

    // FUNGSI UTAMA: Menghitung statistik produk secara real-time dari DataRepository
    public void refreshDataData() {
        ArrayList<Produk> daftarProduk = DataRepository.getDaftarProduk();
        
        int totalJenis = daftarProduk.size();
        
        int akumulasiStok = 0;
        for (Produk p : daftarProduk) {
            akumulasiStok += p.getStok();
        }

        // 2. PERBAIKAN LOGIKA: Menyaring string diskon "0", "0%", "none", ataupun tanda strip "-"
        int totalDiskon = 0;
        for (Produk p : daftarProduk) {
            String diskon = p.getDiskon();
            if (diskon != null && !diskon.trim().isEmpty() && 
                !diskon.equals("0") && !diskon.equalsIgnoreCase("0%") && 
                !diskon.equalsIgnoreCase("none") && !diskon.equals("-")) {
                totalDiskon++;
            }
        }

        if (lblTotalProduk != null) lblTotalProduk.setText(String.valueOf(totalJenis));
        if (lblTotalStok != null) lblTotalStok.setText(String.valueOf(akumulasiStok));
        
        // Sekarang angka boks "Produk Baru" di UI hanya akan menghitung produk dengan diskon aktif
        if (lblProdukBaru != null) lblProdukBaru.setText(String.valueOf(totalDiskon));
    }

    // 3. FUNGSI BARU: Menyaring produk diskon dan memasukkannya ke dalam box besar di UI
    private void tampilkanDaftarDiskon() {
        if (boxDiskon == null) return;

        // Ambil data gudang global
        ArrayList<Produk> daftarProduk = DataRepository.getDaftarProduk();
        
        // Buat tempat penampung khusus item yang diskonnya aktif
        ArrayList<Produk> listTerfilter = new ArrayList<>();
        for (Produk p : daftarProduk) {
            String diskon = p.getDiskon();
            // PERBAIKAN FILTER: Menambahkan kondisi !diskon.equals("-") agar produk tak berdiskon tidak lolos
            if (diskon != null && !diskon.trim().isEmpty() && 
                !diskon.equals("0") && !diskon.equalsIgnoreCase("0%") && 
                !diskon.equalsIgnoreCase("none") && !diskon.equals("-")) {
                listTerfilter.add(p);
            }
        }

        // Buat komponen ListView baru secara dinamis lewat kode
        ListView<Produk> listViewDiskon = new ListView<>();
        listViewDiskon.getItems().addAll(listTerfilter);
        
        // Atur ukuran ListView agar memenuhi area dalam boks melengkung bawaan FXML kamu
        listViewDiskon.setPrefHeight(300); 
        listViewDiskon.setMaxWidth(Double.MAX_VALUE);

        // Kustomisasi style tampilan baris teks di dalam ListView
        listViewDiskon.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Produk produk, boolean empty) {
                super.updateItem(produk, empty);

                if (empty || produk == null) {
                    setText(null);
                    setStyle("-fx-background-color: transparent;");
                } else {
                    // Format tampilan nominal harga ke Rupiah (Rp #.###)
                    DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                    symbols.setGroupingSeparator('.');
                    DecimalFormat df = new DecimalFormat("Rp #,##0", symbols);
                    String hargaFormat = df.format(produk.getHarga());

                    // PERBAIKAN TEKS: Mengganti emoji label "🏷️" menjadi text tag "[Diskon]" demi menghindari kotak kosong/pecah bawaan font OS
                    setText(" [Diskon]  " + produk.getNama() + "   |   Potongan: " + produk.getDiskon() + "   |   Harga Asli: " + hargaFormat);
                    
                    // Styling: Teks putih, ukuran pas, background baris transparan agar menyatu dengan boks biru tuamu
                    setStyle("-fx-text-fill: white; -fx-font-size: 15px; -fx-padding: 12px; -fx-background-color: transparent;");
                }
            }
        });

        // Setel agar background utama dari ListView bawaan JavaFX menjadi transparan/mengikuti warna boks luarnya
        listViewDiskon.setStyle("-fx-background-color: transparent; -fx-control-inner-background: transparent; -fx-background-insets: 0; -fx-padding: 10;");

        // Masukkan komponen ListView yang sudah jadi ke dalam boks kontainer FXML kamu
        boxDiskon.getChildren().clear(); // Bersihkan sisa renderan lama jika ada
        boxDiskon.getChildren().add(listViewDiskon);
    }

    @FXML
    private void handleMenuTambah() {
        if (MainAppController.getInstance() != null) {
            MainAppController.getInstance().tampilkanTambah();
        } else {
            System.out.println("MainAppController belum siap!");
        }
    }

    @FXML
    private void handleCariProduk() {
        String kataKunci = txtCari.getText().trim();
        if (MainAppController.getInstance() != null) {
            MainAppController.getInstance().tampilkanDaftar();
            System.out.println("Mencari produk dengan kata kunci: " + kataKunci);
        } else {
            System.out.println("MainAppController haven't set up yet!");
        }
    }
}