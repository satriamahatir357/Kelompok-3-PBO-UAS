package com.produk.diskon;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class DiskonController {

    @FXML private TableView<Produk> tableProduk;
    @FXML private TableColumn<Produk, Integer> colNomor;
    @FXML private TableColumn<Produk, String> colNama;
    @FXML private TableColumn<Produk, Integer> colHarga;
    @FXML private TableColumn<Produk, Integer> colStok;

    @FXML private TextField txtPersenDiskon;
    @FXML private Label lblHargaAkhir;

    @FXML
    public void initialize() {
        colNomor.setCellValueFactory(new PropertyValueFactory<>("nomor"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colHarga.setCellValueFactory(new PropertyValueFactory<>("harga"));
        colStok.setCellValueFactory(new PropertyValueFactory<>("stok"));

        ObservableList<Produk> data = FXCollections.observableArrayList();
                
        tableProduk.setItems(data);
    }

    @FXML
    void onHitungClick() {
        try {
            Produk produk = tableProduk.getSelectionModel().getSelectedItem();

            if (produk == null) {
                lblHargaAkhir.setText("Pilih produk dulu!");
                return;
            }

            double diskon = Double.parseDouble(txtPersenDiskon.getText());
            double hargaAkhir = produk.getHarga() - (produk.getHarga() * diskon / 100);

            lblHargaAkhir.setText("Harga akhir: Rp " + String.format("%.0f", hargaAkhir));

        } catch (NumberFormatException e) {
            lblHargaAkhir.setText("Diskon harus angka!");
        }
    }

    public static class Produk {
        private int nomor;
        private String nama;
        private int harga;
        private int stok;

        public Produk(int nomor, String nama, int harga, int stok) {
            this.nomor = nomor;
            this.nama = nama;
            this.harga = harga;
            this.stok = stok;
        }

        public int getNomor() { return nomor; }
        public String getNama() { return nama; }
        public int getHarga() { return harga; }
        public int getStok() { return stok; }
    }
}