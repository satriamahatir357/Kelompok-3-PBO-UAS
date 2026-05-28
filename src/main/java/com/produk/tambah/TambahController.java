package com.produk.tambah;

import com.produk.model.Produk;
import com.produk.model.DataRepository; // Mengimport Gudang Data Global
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class TambahController {

    // Menghubungkan komponen ID dari FXML ke variabel Java
    @FXML private TextField txtNama;
    @FXML private TextField txtHarga;
    @FXML private TextField txtStok;

    @FXML private TableView<Produk> tabelProduk;
    @FXML private TableColumn<Produk, String> colNama;
    @FXML private TableColumn<Produk, Double> colHarga;
    @FXML private TableColumn<Produk, Integer> colStok;

    // ObservableList: List khusus JavaFX yang otomatis mengupdate UI Tabel saat datanya berubah
    private final ObservableList<Produk> listProduk = FXCollections.observableArrayList();

    // initialize() otomatis berjalan saat halaman tambah dimuat
    @FXML
    public void initialize() {
        // Hubungkan kolom tabel dengan properti getter yang ada di kelas Produk
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colHarga.setCellValueFactory(new PropertyValueFactory<>("harga"));
        colStok.setCellValueFactory(new PropertyValueFactory<>("stok"));

        // Supaya saat pindah menu lalu kembali lagi ke sini datanya tidak hilang di tabel,
        // kita muat ulang isi listProduk dari DataRepository pusat
        listProduk.setAll(DataRepository.getDaftarProduk());

        // Masukkan list data ke dalam tabel
        tabelProduk.setItems(listProduk);
    }

    // SATU FUNGSI UTAMA: Berjalan ketika tombol "Simpan Produk" diklik
    @FXML
    void onTambahClick() {
        try {
            // 1. Ambil teks inputan user, lalu konversi tipe datanya
            String nama = txtNama.getText();
            double harga = Double.parseDouble(txtHarga.getText());
            int stok = Integer.parseInt(txtStok.getText());

            // 2. Bungkus data ke dalam objek Produk baru
            Produk produkBaru = new Produk(nama, harga, stok);

            // 3. SIMPAN KE UTAMA: Masukkan ke DataRepository agar halaman HOME bisa mendeteksi & menghitung
            DataRepository.tambahProduk(produkBaru);

            // 4. TAMPILKAN DI FORM: Masukkan ke list lokal tabel agar langsung muncul di layar saat itu juga
            listProduk.add(produkBaru);

            // 5. Bersihkan kembali kotak input agar siap diisi data baru
            txtNama.clear();
            txtHarga.clear();
            txtStok.clear();
            
            System.out.println("Produk berhasil disimpan ke sistem sentral!");

        } catch (NumberFormatException e) {
            System.out.println("Error: Pastikan input Harga dan Stok diisi menggunakan angka yang valid!");
        }
    }
}