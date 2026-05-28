package com.produk.daftar;

import com.produk.model.Produk;
import com.produk.model.DataRepository;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.util.Comparator;

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

    // List pusat penampung data asli dari gudang repositori
    private final ObservableList<Produk> masterData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // 1. Pemetaan properti dari objek model ke kolom tabel
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colHarga.setCellValueFactory(new PropertyValueFactory<>("harga"));
        colStok.setCellValueFactory(new PropertyValueFactory<>("stok"));

        // Membuat kolom nama produk agak bergeser ke kiri agar rapi dibaca
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

        // 2. Format Kolom Diskon: AMBIL NILAI RIIL DARI OBJEK PRODUK (MANDIRI)
        colDiskon.setCellValueFactory(new PropertyValueFactory<>("diskon"));

        // 3. Konstruksi Tombol Aksi Kustom (📝 & 🗑️)
        buatKolomAksi();

        // 4. Membaca Data Gudang Utama
        muatDataAwal();

        // 5. AKTIFKAN FITUR PENCARIAN & PENGURUTAN SECARA SEKALIGUS
        setupFiturCariDanUrut();
    }

    private void muatDataAwal() {
        masterData.clear();
        masterData.addAll(DataRepository.getDaftarProduk());
    }

    private void setupFiturCariDanUrut() {
        // A. Bungkus data utama ke FilteredList (untuk memfilter teks)
        FilteredList<Produk> filteredData = new FilteredList<>(masterData, p -> true);

        // B. Dengarkan setiap ketikan user di kolom TextField 'txtSearch'
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(produk -> {
                // Jika kolom pencarian kosong, tampilkan semua produk
                if (newValue == null || newValue.isEmpty() || newValue.isBlank()) {
                    return true;
                }

                String kataKunci = newValue.toLowerCase().trim();

                // COCOKKAN: Berdasarkan nama produk atau harga barang
                if (produk.getNama().toLowerCase().contains(kataKunci)) {
                    return true; 
                } else if (String.valueOf(produk.getHarga()).contains(kataKunci)) {
                    return true;
                }
                
                return false; // Tidak cocok
            });
            
            // Setiap kali filter berubah, nomor urut (1, 2, 3) akan dihitung ulang secara otomatis
            tableProduk.refresh();
        });

        // C. Bungkus FilteredList ke SortedList (untuk mengurutkan baris)
        SortedList<Produk> sortedData = new SortedList<>(filteredData);

        // D. Dengarkan setiap perubahan pilihan di ComboBox 'cmbUrutkan'
        cmbUrutkan.valueProperty().addListener((observable, oldValue, pilihanBaru) -> {
            if (pilihanBaru == null) return;

            sortedData.setComparator((p1, p2) -> {
                switch (pilihanBaru) {
                    case "A-Z":
                        return p1.getNama().compareToIgnoreCase(p2.getNama());
                    case "Z-A":
                        return p2.getNama().compareToIgnoreCase(p1.getNama());
                    case "Harga Termurah":
                        return Double.compare(p1.getHarga(), p2.getHarga());
                    case "Harga Termahal":
                        return Double.compare(p2.getHarga(), p1.getHarga());
                    default:
                        return 0;
                }
            });
            
            tableProduk.refresh();
        });

        // E. Pasang SortedList yang sudah dikombinasikan ke dalam TableView
        tableProduk.setItems(sortedData);

        // F. LOGIKA NOMOR URUT DINAMIS (Mengikuti urutan list yang tampil di layar saat ini)
        colNomor.setCellValueFactory(column -> 
            new ReadOnlyObjectWrapper<>(tableProduk.getItems().indexOf(column.getValue()) + 1)
        );
    }

    private void buatKolomAksi() {
        Callback<TableColumn<Produk, Void>, TableCell<Produk, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Produk, Void> call(final TableColumn<Produk, Void> param) {
                return new TableCell<>() {
                    private final Button btnEdit = new Button("📝");
                    private final Button btnHapus = new Button("🗑️");
                    private final HBox container = new HBox(8, btnEdit, btnHapus);

                    {
                        btnEdit.getStyleClass().add("btn-aksi-edit");
                        btnHapus.getStyleClass().add("btn-aksi-hapus");
                        container.setStyle("-fx-alignment: center;");

                        // Logika interaktif tombol hapus ketika diklik
                        btnHapus.setOnAction(event -> {
                            Produk produkDipilih = getTableView().getItems().get(getIndex());
                            
                            // Hapus langsung dari repositori data induk global
                            DataRepository.getDaftarProduk().remove(produkDipilih); 
                            
                            // Sinkronisasi ulang data lokal agar filter mendeteksi perubahan
                            muatDataAwal(); 
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