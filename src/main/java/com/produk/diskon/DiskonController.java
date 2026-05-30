package com.produk.diskon;

// Mengimport model Produk dan gudang data utama agar bisa diakses di halaman ini
import com.produk.model.Produk;
import com.produk.model.DataRepository;

// Mengimport library JavaFX yang dibutuhkan untuk tabel, list, fxml, dan komponen kontrol
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class DiskonController {

    // @FXML menandakan bahwa variabel ini terhubung langsung dengan fx:id yang ada di file diskon-view.fxml
    @FXML private TableView<Produk> tableProduk;
    @FXML private TableColumn<Produk, Number> colNomor; // Kolom untuk nomor urut otomatis (1, 2, 3...)
    @FXML private TableColumn<Produk, String> colNama;   // Kolom untuk menampilkan nama produk
    @FXML private TableColumn<Produk, Double> colHarga;  // Kolom untuk menampilkan harga asli produk
    @FXML private TableColumn<Produk, Integer> colStok;  // Kolom untuk menampilkan jumlah stok produk

    @FXML private TextField txtSearch;        // Kotak input pencarian produk
    @FXML private TextField txtPersenDiskon;  // Kotak input untuk memasukkan angka persen diskon
    @FXML private Label lblHargaAkhir;        // Label di bagian bawah untuk menampilkan status/hasil hitung

    // Tempat penampung data lokal JavaFX yang mengambil data dari DataRepository pusat
    private final ObservableList<Produk> masterDataDiskon = FXCollections.observableArrayList();

    /**
     * Method initialize() ini otomatis berjalan SATU KALI oleh JavaFX 
     * tepat saat halaman diskon-view.fxml berhasil dimuat di layar.
     */
    @FXML
    public void initialize() {
        // 1. PETAKAN KOLOM: Menghubungkan kolom tabel fxml dengan properti getter pada kelas Produk
        // Variabel di dalam ("...") harus sama persis dengan nama properti di kelas Produk.java
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colHarga.setCellValueFactory(new PropertyValueFactory<>("harga"));
        colStok.setCellValueFactory(new PropertyValueFactory<>("stok"));

        // 2. AMBIL DATA: Memanggil fungsi untuk menarik data produk dari gudang utama
        muatDataDariGudangPusat();

        // 3. SEtUP SEARCH & NOMOR: Menyalakan fitur pencarian dan penomoran otomatis
        setupFiturPencarianDanNomor();
    }

    /**
     * Fungsi untuk membersihkan list lokal dan mengambil ulang seluruh data produk 
     * terbaru yang telah diinput oleh user melalui TambahController.
     */
    private void muatDataDariGudangPusat() {
        masterDataDiskon.clear(); // Bersihkan list lama agar tidak terjadi duplikasi data
        masterDataDiskon.addAll(DataRepository.getDaftarProduk()); // Ambil data segar dari repositori pusat
    }

    /**
     * Fungsi utama untuk menangani logika pencarian (Searching) 
     * dan memberikan nomor urut secara otomatis pada tabel.
     */
    private void setupFiturPencarianDanNomor() {
        // Membungkus master data ke dalam FilteredList (secara default semua data bernilai 'true' alias muncul)
        FilteredList<Produk> filteredData = new FilteredList<>(masterDataDiskon, p -> true);

        // Jika komponen txtSearch ditemukan di FXML, pasang pendengar (listener) ketikan keyboard
        if (txtSearch != null) {
            txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                // setPredicate akan berjalan setiap kali user mengetik atau menghapus huruf di kotak search
                filteredData.setPredicate(produk -> {
                    // Jika kotak pencarian kosong atau spasi doang, tampilkan semua baris data produk
                    if (newValue == null || newValue.isEmpty() || newValue.isBlank()) {
                        return true;
                    }

                    // Ubah ketikan user menjadi huruf kecil semua dan hapus spasi di ujungnya agar pencarian akurat
                    String kataKunci = newValue.toLowerCase().trim();

                    // Cek apakah nama produk atau nominal harga mengandung kata kunci yang dicari
                    if (produk.getNama().toLowerCase().contains(kataKunci)) {
                        return true; // Cocok berdasarkan nama
                    } else if (String.valueOf(produk.getHarga()).contains(kataKunci)) {
                        return true; // Cocok berdasarkan harga
                    }
                    
                    return false; // Produk tidak cocok dengan kata kunci, sembunyikan dari tabel
                });
                // Paksa tabel menggambar ulang nomor urutnya saat hasil pencarian berubah
                tableProduk.refresh(); 
            });
        }

        // Masukkan data hasil filter (FilteredList) ke dalam komponen TableView
        tableProduk.setItems(filteredData);

        // LOGIKA NOMOR OTOMATIS: Menghitung indeks posisi baris di dalam tabel saat ini lalu ditambah 1
        if (colNomor != null) {
            colNomor.setCellValueFactory(column -> 
                new javafx.beans.property.ReadOnlyObjectWrapper<>(tableProduk.getItems().indexOf(column.getValue()) + 1)
            );
        }
    }

    /**
     * TOMBOL 1: HITUNG DISKON (Hanya Simulasi)
     * Berjalan ketika tombol "Hitung Diskon %" diklik oleh user.
     */
    @FXML
    void onHitungClick() {
        try {
            // 1. Ambil baris produk yang saat ini sedang diklik/dipilih oleh user di tabel
            Produk produkTerpilih = tableProduk.getSelectionModel().getSelectedItem();
            if (produkTerpilih == null) {
                lblHargaAkhir.setStyle("-fx-text-fill: red;");
                lblHargaAkhir.setText("Peringatan: Silakan pilih produk di tabel terlebih dahulu!");
                return;
            }

            // 2. Ambil text input diskon dan lakukan validasi apakah kosong
            String inputDiskon = txtPersenDiskon.getText().trim();
            if (inputDiskon.isEmpty()) {
                lblHargaAkhir.setStyle("-fx-text-fill: red;");
                lblHargaAkhir.setText("Peringatan: Persentase diskon tidak boleh kosong!");
                return;
            }

            // 3. Konversi teks diskon menjadi angka double
            double persenDiskon = Double.parseDouble(inputDiskon);
            if (persenDiskon < 0 || persenDiskon > 100) {
                lblHargaAkhir.setStyle("-fx-text-fill: red;");
                lblHargaAkhir.setText("Peringatan: Diskon harus berkisar antara 0% hingga 100%!");
                return;
            }

            // 4. Jalankan Rumus Matematika Kalkulasi Diskon
            double hargaAsli = produkTerpilih.getHarga();
            double potongan = hargaAsli * (persenDiskon / 100);
            double hargaAkhir = hargaAsli - potongan;

            // 5. TAMPILKAN SEBAGAI SIMULASI (Warna Biru Info)
            // Format %,.0f berguna memberikan titik pemisah ribuan otomatis (cth: Rp 15.000.000)
            lblHargaAkhir.setStyle("-fx-text-fill: #3498db; -fx-font-weight: bold;"); 
            lblHargaAkhir.setText(String.format("[Simulasi] Harga Akhir %s: Rp %,.0f", 
                    produkTerpilih.getNama(), hargaAkhir));

        } catch (NumberFormatException e) {
            // Berjalan jika user memasukkan input teks/huruf/simbol ke dalam kotak diskon
            lblHargaAkhir.setStyle("-fx-text-fill: red;");
            lblHargaAkhir.setText("Error: Input diskon harus berupa angka yang valid!");
        }
    }

    /**
     * TOMBOL 2: TETAPKAN DISKON (Permanen ke Database Aplikasi)
     * Berjalan ketika tombol "Tetapkan Diskon" diklik oleh user.
     */
    @FXML
    void onTetapkanClick() {
        try {
            // 1. Ambil produk terpilih dari baris tabel
            Produk produkTerpilih = tableProduk.getSelectionModel().getSelectedItem();
            if (produkTerpilih == null) {
                lblHargaAkhir.setStyle("-fx-text-fill: red;");
                lblHargaAkhir.setText("Peringatan: Silakan pilih produk di tabel terlebih dahulu!");
                return;
            }

            // 2. Ambil teks persen diskon dan validasi kelayakan input
            String inputDiskon = txtPersenDiskon.getText().trim();
            if (inputDiskon.isEmpty()) {
                lblHargaAkhir.setStyle("-fx-text-fill: red;");
                lblHargaAkhir.setText("Peringatan: Persentase diskon tidak boleh kosong!");
                return;
            }

            double persenDiskon = Double.parseDouble(inputDiskon);
            if (persenDiskon < 0 || persenDiskon > 100) {
                lblHargaAkhir.setStyle("-fx-text-fill: red;");
                lblHargaAkhir.setText("Peringatan: Diskon harus berkisar antara 0% hingga 100%!");
                return;
            }

            // 3. Hitung nilai akhir diskon
            double hargaAsli = produkTerpilih.getHarga();
            double potongan = hargaAsli * (persenDiskon / 100);
            double hargaAkhir = hargaAsli - potongan;

            // 4. KUNCI DATA: Menyimpan teks diskon permanen langsung ke dalam properti objek Produk tersebut
            // Ini berdampak langsung pada halaman "Lihat Daftar Produk" (Kolom diskon berubah dari '-' jadi isi nilai ini)
            String teksDiskonUntukDaftar = String.format("%.0f%% (Rp %,.0f)", persenDiskon, hargaAkhir);
            produkTerpilih.setDiskon(teksDiskonUntukDaftar);

            // 5. BERI FEEDBACK SUKSES (Warna Hijau Berhasil)
            lblHargaAkhir.setStyle("-fx-text-fill: #2ecc71; -fx-font-weight: bold;"); 
            lblHargaAkhir.setText(String.format("Sukses! Diskon untuk '%s' telah resmi ditetapkan.", produkTerpilih.getNama()));
            
            // 6. REFRESH VISUAL: Memaksa JavaFX menggambar ulang isi tabel detik ini juga agar perubahan langsung nampak
            tableProduk.refresh();

        } catch (NumberFormatException e) {
            lblHargaAkhir.setStyle("-fx-text-fill: red;");
            lblHargaAkhir.setText("Error: Input diskon harus berupa angka yang valid!");
        }
    }
}