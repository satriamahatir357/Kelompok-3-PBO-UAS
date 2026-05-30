package com.produk.tambah;

import com.produk.model.Produk;
import com.produk.model.DataRepository; // Mengimport Gudang Data Global
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class TambahController {

    // Menghubungkan komponen ID dari FXML ke variabel Java
    @FXML private TextField txtNama;
    @FXML private TextField txtHarga;
    @FXML private TextField txtStok;

    // initialize() otomatis berjalan saat halaman tambah dimuat
    @FXML
    public void initialize() {
        // Dikosongkan karena halaman ini murni hanya form input, 
        // komponen tabel data sudah diurus secara terpisah di Halaman Daftar.
    }

    // SATU FUNGSI UTAMA: Berjalan ketika tombol "Simpan" diklik
    @FXML
    void onTambahClick() {
        try {
            // 1. Ambil teks inputan user, lalu konversi tipe datanya
            String nama = txtNama.getText();
            double harga = Double.parseDouble(txtHarga.getText());
            int stok = Integer.parseInt(txtStok.getText());

            // 2. Bungkus data ke dalam objek Produk baru
            Produk produkBaru = new Produk(nama, harga, stok);

            // 3. SIMPAN KE UTAMA: Masukkan ke DataRepository agar semua halaman bisa mendeteksi & menghitung
            DataRepository.tambahProduk(produkBaru);

            // 4. Bersihkan kembali kotak input agar siap diisi data baru
            txtNama.clear();
            txtHarga.clear();
            txtStok.clear();
            
            System.out.println("Produk berhasil disimpan ke sistem sentral!");

        } catch (NumberFormatException e) {
            System.out.println("Error: Pastikan input Harga dan Stok diisi menggunakan angka yang valid!");
        }
    }
}