package com.produk.tambah;

import com.produk.model.Produk;
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
        // Hubungkan kolom tabel dengan properti getter (getNama, getHarga, getStok) yang ada di kelas Produk
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colHarga.setCellValueFactory(new PropertyValueFactory<>("harga"));
        colStok.setCellValueFactory(new PropertyValueFactory<>("stok"));

        // Masukkan list data ke dalam tabel
        tabelProduk.setItems(listProduk);
    }

    // Fungsi yang berjalan ketika tombol "Simpan Produk" diklik
    @FXML
    void onTambahClick() {
        // Ambil teks inputan user, lalu konversi tipe datanya sesuai model
        String nama = txtNama.getText();
        double harga = Double.parseDouble(txtHarga.getText());
        int stok = Integer.parseInt(txtStok.getText());

        // Bungkus data ke dalam objek Produk baru, lalu masukkan ke list
        listProduk.add(new Produk(nama, harga, stok));

        // Bersihkan kembali kotak input agar siap diisi data baru
        txtNama.clear();
        txtHarga.clear();
        txtStok.clear();
    }
}