package com.produk.home;

import com.produk.model.Produk;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.util.ArrayList;

public class HomeController {

    @FXML private Label lblTotalProduk;
    @FXML private Label lblTotalStok;
    @FXML private Label lblProdukBaru;

    // initialize() otomatis berjalan saat halaman dimuat
    @FXML
    public void initialize() {
        // 1. Membuat data simulasi menggunakan ArrayList sesuai permintaanmu
        ArrayList<Produk> daftarProduk = new ArrayList<>();
        
        // 2. Memasukkan beberapa objek produk (Nama, Harga, Stok) ke dalam ArrayList
        daftarProduk.add(new Produk("Kemeja Flanel", 150000, 45));
        daftarProduk.add(new Produk("Celana Chino", 200000, 30));
        daftarProduk.add(new Produk("Jaket Bomber", 250000, 25));
        daftarProduk.add(new Produk("Kaos Polos", 60000, 20)); // Data tiruan baru

        // 3. Menghitung ukuran Array (Total Jenis Produk)
        int totalJenis = daftarProduk.size();
        
        // 4. Melakukan perulangan (Looping) untuk mengakumulasikan total kuantitas stok
        int akumulasiStok = 0;
        for (Produk p : daftarProduk) {
            akumulasiStok += p.getStok();
        }

        // 5. Menentukan angka penanda tiruan untuk Produk Baru (Misal sisa kuota data)
        int produkBaru = totalJenis - 1; 

        // 6. Mengirim data hasil perhitungan dari ArrayList ke komponen Label di UI Monitor
        lblTotalProduk.setText(String.valueOf(totalJenis));
        lblTotalStok.setText(String.valueOf(akumulasiStok));
        lblProdukBaru.setText(String.valueOf(produkBaru));
    }
}