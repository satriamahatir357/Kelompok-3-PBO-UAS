package com.produk.home;

import com.produk.MainAppController;
import com.produk.model.Produk;
import com.produk.model.DataRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField; // Import ditambahkan untuk membaca TextField pencarian
import java.util.ArrayList;

public class HomeController {

    @FXML private Label lblTotalProduk;
    @FXML private Label lblTotalStok;
    @FXML private Label lblProdukBaru;
    
    // 1. TAMBAHKAN BARIS INI: Menghubungkan id="txtCari" dari FXML ke Java
    @FXML private TextField txtCari;

    // initialize() otomatis berjalan saat halaman home dimuat pertama kali
    @FXML
    public void initialize() {
        refreshDataData();
    }

    // FUNGSI UTAMA: Menghitung statistik produk secara real-time dari DataRepository
    public void refreshDataData() {
        // Mengambil data terupdate dari Gudang Data global
        ArrayList<Produk> daftarProduk = DataRepository.getDaftarProduk();
        
        // 1. Menghitung Ukuran Riil ArrayList saat ini
        int totalJenis = daftarProduk.size();
        
        // 2. Melakukan kalkulasi total akumulasi stok barang
        int akumulasiStok = 0;
        for (Produk p : daftarProduk) {
            akumulasiStok += p.getStok();
        }

        // 3. Menghitung indikator produk baru secara dinamis
        int produkBaru = totalJenis; 

        // 4. Update teks di layar UI secara real-time
        if (lblTotalProduk != null) lblTotalProduk.setText(String.valueOf(totalJenis));
        if (lblTotalStok != null) lblTotalStok.setText(String.valueOf(akumulasiStok));
        if (lblProdukBaru != null) lblProdukBaru.setText(String.valueOf(produkBaru));
    }

    // Aksi ketika tombol "Tambah Produk" hitam di dashboard diklik
    @FXML
    private void handleMenuTambah() {
        if (MainAppController.getInstance() != null) {
            MainAppController.getInstance().tampilkanTambah();
        } else {
            System.out.println("MainAppController belum siap!");
        }
    }

    // 2. TAMBAHKAN FUNGSI INI: Aksi ketika tombol "Lihat Produk" hitam diklik
    @FXML
    private void handleCariProduk() {
        // Mengambil teks yang diketik user di kolom pencarian dan menghapus spasi kosong di awal/akhir
        String kataKunci = txtCari.getText().trim();
        
        if (MainAppController.getInstance() != null) {
            // Perintahkan controller utama untuk langsung membuka halaman Lihat Daftar
            MainAppController.getInstance().tampilkanDaftar();
            
            // Mencetak log pencarian di terminal VS Code untuk memastikan fungsi berjalan
            System.out.println("Mencari produk dengan kata kunci: " + kataKunci);
        } else {
            System.out.println("MainAppController belum siap!");
        }
    }
}