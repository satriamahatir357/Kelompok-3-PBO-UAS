package com.produk.diskon; // <-- Alamat rumah baru di folder diskon

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class DiskonController {

    // Menghubungkan ID komponen dari diskon-view.fxml ke variabel Java
    @FXML private TextField txtHargaAsli;
    @FXML private TextField txtPersenDiskon;
    @FXML private Label lblHargaAkhir;

    // Fungsi yang otomatis berjalan saat tombol "Hitung Harga Akhir" diklik
    @FXML
    void onHitungClick() {
        try {
            // 1. Mengambil teks input lalu dikonversi menjadi angka desimal (double)
            double hargaAsli = Double.parseDouble(txtHargaAsli.getText());
            double persenDiskon = Double.parseDouble(txtPersenDiskon.getText());

            // 2. Rumus Matematika: Menghitung nilai potongan dan harga akhir setelah diskon
            double potongan = hargaAsli * (persenDiskon / 100);
            double hargaAkhir = hargaAsli - potongan;

            // 3. Menampilkan hasil ke komponen Label di UI
            // String.format("%.0f", ...) berfungsi membuang angka desimal .0 di belakang koma
            lblHargaAkhir.setText("Rp " + String.format("%.0f", hargaAkhir));
            
        } catch (NumberFormatException e) {
            // Antisipasi jika user memasukkan huruf atau membiarkan kotak input kosong
            lblHargaAkhir.setText("Input harus berupa angka!");
        }
    }
}