package com.produk.daftar;

import com.produk.model.Produk;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import java.util.function.UnaryOperator;

public class EditController {

    @FXML private TextField txtNama;
    @FXML private TextField txtHarga;
    @FXML private TextField txtStok;

    private Produk produkTarget;
    private boolean statusSimpanTerklik = false;

    @FXML
    public void initialize() {
        // Filter angka murni untuk harga dan stok
        UnaryOperator<TextFormatter.Change> filterAngka = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) return change;
            return null;
        };
        txtHarga.setTextFormatter(new TextFormatter<>(filterAngka));
        txtStok.setTextFormatter(new TextFormatter<>(filterAngka));
    }

    // Fungsi untuk menerima lemparan data produk yang mau diedit dari daftar utama
    public void setProduk(Produk produk) {
        this.produkTarget = produk;
        txtNama.setText(produk.getNama());
        txtHarga.setText(String.format("%.0f", produk.getHarga()));
        txtStok.setText(String.valueOf(produk.getStok()));
    }

    public boolean isStatusSimpanTerklik() {
        return statusSimpanTerklik;
    }

    @FXML
    void onSimpanClick() {
        if (txtNama.getText().trim().isEmpty() || txtHarga.getText().trim().isEmpty() || txtStok.getText().trim().isEmpty()) {
            return; // Validasi kosong simpel
        }

        // Update langsung isi data objek produknya
        produkTarget.setNama(txtNama.getText().trim());
        produkTarget.setHarga(Double.parseDouble(txtHarga.getText().trim()));
        produkTarget.setStok(Integer.parseInt(txtStok.getText().trim()));

        statusSimpanTerklik = true;
        
        // Tutup jendela pop-up modal
        Stage stage = (Stage) txtNama.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onBatalClick() {
        Stage stage = (Stage) txtNama.getScene().getWindow();
        stage.close();
    }
}