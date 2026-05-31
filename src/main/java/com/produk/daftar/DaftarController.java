package com.produk.daftar;

import com.produk.model.Produk;
import com.produk.model.DataRepository;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Optional;

public class DaftarController {

    @FXML private TableView<Produk> tableProduk;
    @FXML private TableColumn<Produk, Number> colNomor;
    @FXML private TableColumn<Produk, String> colNama;
    @FXML private TableColumn<Produk, Double> colHarga;
    @FXML private TableColumn<Produk, Integer> colStok;
    @FXML private TableColumn<Produk, String> colDiskon;
    @FXML private TableColumn<Produk, Void> colAksi;

    @FXML private TextField txtSearch;
    @FXML private ComboBox<String> cmbUrutkan;

    private final ObservableList<Produk> masterData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colHarga.setCellValueFactory(new PropertyValueFactory<>("harga"));
        colStok.setCellValueFactory(new PropertyValueFactory<>("stok"));

        colNama.setCellFactory(column -> {
            TableCell<Produk, String> cell = new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item);
                    }
                }
            };
            cell.getStyleClass().add("col-nama-kiri");
            return cell;
        });

        // Format Rupiah rapi
        colHarga.setCellFactory(column -> {
            return new TableCell<>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                        symbols.setGroupingSeparator('.');
                        DecimalFormat df = new DecimalFormat("Rp #,##0", symbols);
                        df.setMaximumFractionDigits(0);
                        setText(df.format(item));
                    }
                }
            };
        });

        colDiskon.setCellValueFactory(new PropertyValueFactory<>("diskon"));

        // Mengaktifkan konstruksi tombol aksi edit dan hapus baru
        buatKolomAksi();
        muatDataAwal();
        setupFiturCariDanUrut();
    }

    private void muatDataAwal() {
        masterData.clear();
        masterData.addAll(DataRepository.getDaftarProduk());
    }

    private void setupFiturCariDanUrut() {
        FilteredList<Produk> filteredData = new FilteredList<>(masterData, p -> true);

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(produk -> {
                if (newValue == null || newValue.isEmpty() || newValue.isBlank()) {
                    return true;
                }
                String kataKunci = newValue.toLowerCase().trim();
                if (produk.getNama().toLowerCase().contains(kataKunci)) {
                    return true; 
                } else if (String.valueOf(produk.getHarga()).contains(kataKunci)) {
                    return true;
                }
                return false;
            });
            tableProduk.refresh();
        });

        SortedList<Produk> sortedData = new SortedList<>(filteredData);

        cmbUrutkan.valueProperty().addListener((observable, oldValue, pilihanBaru) -> {
            if (pilihanBaru == null) return;
            sortedData.setComparator((p1, p2) -> {
                switch (pilihanBaru) {
                    case "A-Z": return p1.getNama().compareToIgnoreCase(p2.getNama());
                    case "Z-A": return p2.getNama().compareToIgnoreCase(p1.getNama());
                    case "Harga Termurah": return Double.compare(p1.getHarga(), p2.getHarga());
                    case "Harga Termahal": return Double.compare(p2.getHarga(), p1.getHarga());
                    default: return 0;
                }
            });
            tableProduk.refresh();
        });

        tableProduk.setItems(sortedData);

        colNomor.setCellValueFactory(column -> 
            new ReadOnlyObjectWrapper<>(tableProduk.getItems().indexOf(column.getValue()) + 1)
        );
    }

    private void buatKolomAksi() {
        Callback<TableColumn<Produk, Void>, TableCell<Produk, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Produk, Void> call(final TableColumn<Produk, Void> param) {
                return new TableCell<>() {
                    private final Button btnEdit = new Button("✎");
                    private final Button btnHapus = new Button("✕");
                    private final HBox container = new HBox(12, btnEdit, btnHapus);

                    {
                        btnEdit.getStyleClass().add("btn-aksi-edit");
                        btnHapus.getStyleClass().add("btn-aksi-hapus");
                        container.setStyle("-fx-alignment: center;");

                        // 1. LOGIKA TOMBOL EDIT (POP-UP MODAL SESUAI FIGMA)
                        btnEdit.setOnAction(event -> {
                            Produk produkDipilih = getTableView().getItems().get(getIndex());
                            
                            try {
                                // Load file FXML modal edit
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("edit-view.fxml"));
                                Parent root = loader.load();
                                
                                // Ambil controller-nya dan kirim data produk terpilih
                                EditController editController = loader.getController();
                                editController.setProduk(produkDipilih);
                                
                                // Set up window pop-up baru (Stage)
                                Stage stageModal = new Stage();
                                stageModal.initModality(Modality.APPLICATION_MODAL); // Mengunci halaman belakangnya
                                stageModal.initStyle(StageStyle.UNDECORATED);       // Menghilangkan frame bar atas bawaan OS windows
                                stageModal.initOwner(btnEdit.getScene().getWindow());
                                
                                Scene scene = new Scene(root);
                                stageModal.setScene(scene);
                                stageModal.showAndWait(); // Tampilkan pop-up dan tunggu aksi user
                                
                                // Jika user mengklik simpan di pop-up, segarkan tabel
                                if (editController.isStatusSimpanTerklik()) {
                                    tableProduk.refresh();
                                }
                                
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });

                        // 2. LOGIKA TOMBOL HAPUS (ALERT KONFIRMASI ACCORDING TO MOCKUP)
                        btnHapus.setOnAction(event -> {
                            Produk produkDipilih = getTableView().getItems().get(getIndex());
                            
                            // Inisialisasi jenis tombol secara manual sesuai urutan (OK dulu baru Cancel)
                            ButtonType btnTypeOK = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                            ButtonType btnTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                            
                            // Buat Alert tanpa jenis bawaan, kita masukkan tombol kustom kita sendiri
                            Alert alertKonfirmasi = new Alert(AlertType.CONFIRMATION, "", btnTypeOK, btnTypeCancel);
                            alertKonfirmasi.setTitle("Konfirmasi Pembatalan");
                            alertKonfirmasi.setHeaderText("Hapus Produk Ini?");
                            alertKonfirmasi.setContentText("Apakah Anda yakin ingin menghapus '" + produkDipilih.getNama() + "'?");
                            
                            // Menghilangkan frame/border putih jendela windows (Opsional, biar clean melengkung)
                            Stage alertStage = (Stage) alertKonfirmasi.getDialogPane().getScene().getWindow();
                            alertStage.initStyle(StageStyle.UNDECORATED); 
                            
                            // Ambil dialog pane-nya lalu injek file CSS nya ke sana
                            DialogPane dialogPane = alertKonfirmasi.getDialogPane();
                            dialogPane.getStylesheets().add(getClass().getResource("daftar-style.css").toExternalForm());
                            
                            // Membuka alert dan menunggu respon (OK / Cancel)
                            Optional<ButtonType> keputusan = alertKonfirmasi.showAndWait();
                            if (keputusan.isPresent() && keputusan.get() == btnTypeOK) {
                                // Eksekusi hapus dari gudang utama data global jika memilih OK
                                DataRepository.getDaftarProduk().remove(produkDipilih); 
                                muatDataAwal(); // Refresh total list lokal
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(container);
                        }
                    }
                };
            }
        };
        colAksi.setCellFactory(cellFactory);
    }
    
    public void muatUlangData() {
        muatDataAwal();
        tableProduk.refresh();
    }
}