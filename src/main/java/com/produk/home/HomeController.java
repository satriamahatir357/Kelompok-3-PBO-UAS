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
import javafx.scene.input.MouseEvent; // Library pendeteksi klik mouse
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;

public class HomeController {

    @FXML private Label lblTotalProduk;
    @FXML private Label lblTotalStok;
    @FXML private Label lblProdukBaru;
    @FXML private TextField txtCari;

    @FXML private VBox boxHasilCari;
    @FXML private TableView<Produk> tblHasilCari;
    @FXML private TableColumn<Produk, String> colNama;
    @FXML private TableColumn<Produk, Double> colHarga;
    @FXML private TableColumn<Produk, Integer> colStok;
    @FXML private TableColumn<Produk, String> colDiskon;

    @FXML private HBox containerCardDiskon;

    // Menghubungkan ID avatar dari FXML ke Java Controller
    @FXML private VBox avatarClickable;

    @FXML
    public void initialize() {
        if (colNama != null) colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        if (colStok != null) colStok.setCellValueFactory(new PropertyValueFactory<>("stok"));
        if (colDiskon != null) colDiskon.setCellValueFactory(new PropertyValueFactory<>("diskon"));

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

        if (txtCari != null) {
            txtCari.textProperty().addListener((observable, oldValue, newValue) -> {
                filterPencarianDinamis(newValue);
            });
        }

        refreshDataData();
    }

    public void refreshCardPromoDiskon() {
        if (containerCardDiskon == null) return;
        
        containerCardDiskon.getChildren().clear();

        ArrayList<Produk> semuaProduk = DataRepository.getDaftarProduk();

        for (Produk p : semuaProduk) {
            if (p.getDiskon() != null && !p.getDiskon().equals("-") && !p.getDiskon().trim().isEmpty()) {
                
                VBox card = new VBox();
                card.setSpacing(6.0);
                card.setStyle(
                    "-fx-background-color: #1a202c; " + 
                    "-fx-background-radius: 12px; " +
                    "-fx-border-color: #ff0055; " +   
                    "-fx-border-width: 1.5px; " +
                    "-fx-border-radius: 12px; " +
                    "-fx-padding: 12px; " +
                    "-fx-min-width: 140px; " +          
                    "-fx-pref-width: 140px; " +
                    "-fx-min-height: 130px; " +        
                    "-fx-pref-height: 130px; " +
                    "-fx-alignment: center;"
                );

                Label lblNama = new Label(p.getNama());
                lblNama.setStyle("-fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-font-size: 14px; -fx-text-alignment: center;");
                lblNama.setWrapText(true); 

                String hargaFormat = String.format("%,.0f", p.getHarga()).replace(',', '.');
                Label lblHargaProduk = new Label("Rp " + hargaFormat);
                lblHargaProduk.setStyle("-fx-text-fill: #cbd5e0; -fx-font-size: 12px;");

                Label lblBadgeDiskon = new Label("DISKON " + p.getDiskon());
                lblBadgeDiskon.setStyle(
                    "-fx-background-color: #ffcc00; " +
                    "-fx-text-fill: #000000; " +
                    "-fx-font-weight: bold; " +
                    "-fx-font-size: 11px; " +
                    "-fx-padding: 4px 8px; " +
                    "-fx-background-radius: 5px;"
                );

                card.getChildren().addAll(lblNama, lblHargaProduk, lblBadgeDiskon);
                containerCardDiskon.getChildren().add(card);
            }
        }
    }

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
        
        refreshCardPromoDiskon();
    }

    @FXML
    private void handleLihatProduk() {
        if (MainAppController.getInstance() != null) {
            MainAppController.getInstance().tampilkanDaftar();
        }
    }

    @FXML
    private void handleMenuTambah() {
        if (MainAppController.getInstance() != null) {
            MainAppController.getInstance().tampilkanTambah();
        }
    }

    // FUNGSI KLIK AVATAR: Mengarahkan navigasi ke About lewat MainAppController global instance
    @FXML
    private void handleKeHalamanAbout(MouseEvent event) {
        if (MainAppController.getInstance() != null) {
            MainAppController.getInstance().tampilkanAbout();
            System.out.println("Navigasi sukses: Avatar diklik -> Menuju Halaman About.");
        } else {
            System.out.println("Gagal navigasi: MainAppController belum siap!");
        }
    }
}