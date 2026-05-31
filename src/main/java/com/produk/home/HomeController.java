package com.produk.home;

import com.produk.MainAppController;
import com.produk.model.Produk;
import com.produk.model.DataRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;

public class HomeController {

    @FXML private Label lblTotalProduk;
    @FXML private Label lblTotalStok;
    @FXML private Label lblProdukBaru;
    @FXML private TextField txtCari;

    // FXML Inject untuk komponen area tabel live search dinamis (Atas)
    @FXML private VBox boxHasilCari;
    @FXML private TableView<Produk> tblHasilCari;
    @FXML private TableColumn<Produk, String> colNama;
    @FXML private TableColumn<Produk, Double> colHarga;
    @FXML private TableColumn<Produk, Integer> colStok;
    @FXML private TableColumn<Produk, String> colDiskon;

    // FXML Inject untuk kontainer HBox tempat Card Promo nongol (Bawah)
    @FXML private HBox containerCardDiskon;

    @FXML
    public void initialize() {
        // 1. Hubungkan variabel properti model ke kolom TableView pencarian
        if (colNama != null) colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        if (colStok != null) colStok.setCellValueFactory(new PropertyValueFactory<>("stok"));
        if (colDiskon != null) colDiskon.setCellValueFactory(new PropertyValueFactory<>("diskon"));

        // 2. Format kolom harga agar desimal murni tanpa format ilmiah huruf E
        if (colHarga != null) {
            colHarga.setCellValueFactory(new PropertyValueFactory<>("harga"));
            colHarga.setCellFactory(column -> new TableCell<Produk, Double>() {
                @Override
                protected void updateItem(Double harga, boolean empty) {
                    super.updateItem(harga, empty);
                    if (empty || harga == null) {
                        setText(null);
                    } else {
                        setText(String.format("%,.0f", harga).replace(',', '.')); 
                    }
                }
            });
        }

        // 3. LIVE SEARCH LISTENER
        if (txtCari != null) {
            txtCari.textProperty().addListener((observable, oldValue, newValue) -> {
                filterPencarianDinamis(newValue);
            });
        }

        // Jalankan kalkulasi angka statistik awal & generate card promo pertama
        refreshDataData();
    }

    // FUNGSI UTAMA 1: Membuat Card/Kotak Promo secara dinamis (Tinggi dikunci aman)
    public void refreshCardPromoDiskon() {
        if (containerCardDiskon == null) return;
        
        // Bersihkan area lama agar card tidak ter-duplikasi saat data diupdate
        containerCardDiskon.getChildren().clear();

        ArrayList<Produk> semuaProduk = DataRepository.getDaftarProduk();

        for (Produk p : semuaProduk) {
            // Validasi: Hanya buatkan card jika produk punya diskon (bukan strip atau kosong)
            if (p.getDiskon() != null && !p.getDiskon().equals("-") && !p.getDiskon().trim().isEmpty()) {
                
                // Pembuatan container Box Card secara dynamic
                VBox card = new VBox();
                card.setSpacing(6.0);
                card.setStyle(
                    "-fx-background-color: #1a202c; " + 
                    "-fx-background-radius: 12px; " +
                    "-fx-border-color: #ff0055; " +    // Aksen warna pink neon khas diskon promo
                    "-fx-border-width: 1.5px; " +
                    "-fx-border-radius: 12px; " +
                    "-fx-padding: 12px; " +
                    "-fx-min-width: 140px; " +          
                    "-fx-pref-width: 140px; " +
                    "-fx-min-height: 130px; " +        // Mengunci tinggi minimal card agar teks tidak amblas kebawah
                    "-fx-pref-height: 130px; " +
                    "-fx-alignment: center;"
                );

                // Elemen 1: Judul Nama Produk
                Label lblNama = new Label(p.getNama());
                lblNama.setStyle("-fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-font-size: 14px; -fx-text-alignment: center;");
                lblNama.setWrapText(true); 

                // Elemen 2: Teks Harga Berformat Rupiah Bersih
                String hargaFormat = String.format("%,.0f", p.getHarga()).replace(',', '.');
                Label lblHargaProduk = new Label("Rp " + hargaFormat);
                lblHargaProduk.setStyle("-fx-text-fill: #cbd5e0; -fx-font-size: 12px;");

                // Elemen 3: Badge Diskon Menyala
                Label lblBadgeDiskon = new Label("DISKON " + p.getDiskon());
                lblBadgeDiskon.setStyle(
                    "-fx-background-color: #ffcc00; " +
                    "-fx-text-fill: #000000; " +
                    "-fx-font-weight: bold; " +
                    "-fx-font-size: 11px; " +
                    "-fx-padding: 4px 8px; " +
                    "-fx-background-radius: 5px;"
                );

                // Masukkan komponen teks ke dalam wadah card
                card.getChildren().addAll(lblNama, lblHargaProduk, lblBadgeDiskon);

                // Tampilkan card promo ke dalam HBox kontainer utama halaman Home
                containerCardDiskon.getChildren().add(card);
            }
        }
    }

    // FUNGSI UTAMA 2: Live Search menyaring semua produk yang mengandung huruf yang diketik
    private void filterPencarianDinamis(String teksInput) {
        String kataKunci = teksInput.trim().toLowerCase();

        if (kataKunci.isEmpty()) {
            boxHasilCari.setVisible(false);
            boxHasilCari.setManaged(false);
            return;
        }

        ArrayList<Produk> semuaProduk = DataRepository.getDaftarProduk();
        ObservableList<Produk> hasilFilter = FXCollections.observableArrayList();

        for (Produk p : semuaProduk) {
            if (p.getNama() != null && p.getNama().toLowerCase().contains(kataKunci)) {
                hasilFilter.add(p);
            }
        }

        tblHasilCari.setItems(hasilFilter);
        boxHasilCari.setVisible(true);
        boxHasilCari.setManaged(true);
    }

    @FXML
    private void handleCariProduk() {
        filterPencarianDinamis(txtCari.getText());
    }

    // Menghitung statistik dashboard secara real-time dari DataRepository
    public void refreshDataData() {
        ArrayList<Produk> daftarProduk = DataRepository.getDaftarProduk();
        int totalJenis = daftarProduk.size();
        
        int akumulasiStok = 0;
        for (Produk p : daftarProduk) {
            akumulasiStok += p.getStok();
        }

        if (lblTotalProduk != null) lblTotalProduk.setText(String.valueOf(totalJenis));
        if (lblTotalStok != null) lblTotalStok.setText(String.valueOf(akumulasiStok));
        if (lblProdukBaru != null) lblProdukBaru.setText(String.valueOf(totalJenis));
        
        // Picu pembuatan ulang komponen card promo berdiskon
        refreshCardPromoDiskon();
    }

    @FXML
    private void handleMenuTambah() {
        if (MainAppController.getInstance() != null) {
            MainAppController.getInstance().tampilkanTambah();
        }
    }
}