package com.produk.tambah;

import com.produk.model.Produk;
import com.produk.model.DataRepository; // Mengimport Gudang Data Global
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import java.util.Optional;

public class TambahController {

    // Menghubungkan komponen ID dari FXML ke variabel Java
    @FXML private TextField txtNama;
    @FXML private TextField txtHarga;
    @FXML private TextField txtStok;

    @FXML
    public void initialize() {
        // Dikosongkan karena komponen tabel data diurus terpisah di Halaman Daftar.
    }

    // FUNGSI UTAMA: Berjalan ketika tombol "Simpan" diklik
    @FXML
    void onTambahClick() {
        // Ambil data input teks dan bersihkan spasi di ujungnya
        String nama = txtNama.getText().trim();
        String hargaRaw = txtHarga.getText().trim();
        String stokRaw = txtStok.getText().trim();

        // VALIDASI 1: Cek apakah ada field yang masih kosong murni
        if (nama.isEmpty() || hargaRaw.isEmpty() || stokRaw.isEmpty()) {
            tampilkanPopup(AlertType.WARNING, "Peringatan", "Data Belum Lengkap", 
                    "Formulir tidak dapat diproses karena terdapat kolom yang belum diisi. Mohon periksa kembali dan lengkapi seluruh data produk sebelum disimpan.");
            return;
        }

        try {
            // Konversi tipe data teks ke angka
            double harga = Double.parseDouble(hargaRaw);
            int stok = Integer.parseInt(stokRaw);

            // VALIDASI 2: Cek nilai Harga (Tidak boleh 0 atau kurang dari 0)
            if (harga <= 0) {
                tampilkanPopup(AlertType.WARNING, "Peringatan Nilai", "Nominal Harga Tidak Valid", 
                        "Harga produk harus lebih besar dari Rp 0. Mohon periksa kembali nominal yang Anda masukkan.");
                return;
            }

            // VALIDASI 3: Cek nilai Stok (Tidak boleh kurang dari 0, tapi 0 boleh jika barang habis)
            if (stok < 0) {
                tampilkanPopup(AlertType.WARNING, "Peringatan Nilai", "Jumlah Stok Tidak Valid", 
                        "Jumlah stok produk tidak boleh bernilai minus. Mohon masukkan kuantitas produk dengan benar.");
                return;
            }

            // JIKA SEMUA VALIDASI LOLOS: Bungkus data ke dalam objek Produk baru
            Produk produkBaru = new Produk(nama, harga, stok);

            // SIMPAN KE UTAMA: Masukkan ke DataRepository global
            DataRepository.tambahProduk(produkBaru);

            // POP-UP SUKSES: Berhasil disimpan dengan gaya kustom
            tampilkanPopup(AlertType.INFORMATION, "Sukses", "Produk Berhasil Disimpan", 
                    "Produk '" + nama + "' telah berhasil disimpan ke dalam sistem.");

            // Bersihkan kembali kotak input agar siap diisi data baru
            txtNama.clear();
            txtHarga.clear();
            txtStok.clear();

        } catch (NumberFormatException e) {
            // POP-UP ERROR: Jika user salah ketik huruf/simbol di kolom angka
            tampilkanPopup(AlertType.ERROR, "Kesalahan Input", "Format Data Tidak Valid", 
                    "Pastikan kolom Harga dan Stok hanya diisi menggunakan angka yang valid!");
        }
    }

    // FUNGSI KEDUA: Berjalan ketika tombol "Batal" diklik (Deteksi form kosong profesional)
    @FXML
    void onBatalClick() {
        // Cek kondisi apakah seluruh form inputan memang sudah kosong
        boolean namaKosong = txtNama.getText() == null || txtNama.getText().trim().isEmpty();
        boolean hargaKosong = txtHarga.getText() == null || txtHarga.getText().trim().isEmpty();
        boolean stokKosong = txtStok.getText() == null || txtStok.getText().trim().isEmpty();

        // Jika semua form sudah kosong, tampilkan pesan informatif
        if (namaKosong && hargaKosong && stokKosong) {
            tampilkanPopup(AlertType.INFORMATION, "Informasi", "Formulir Masih Kosong", 
                    "Seluruh kolom inputan belum diisi. Tidak ada data formulir yang perlu dibatalkan atau dibersihkan.");
            return; // Berhentikan fungsi agar konfirmasi hapus tidak muncul
        }

        // JIKA FORM MASIH ADA ISINYA: Tampilkan konfirmasi pembatalan
        Alert konfirmasi = new Alert(AlertType.CONFIRMATION);
        konfirmasi.setTitle("Konfirmasi Pembatalan");
        konfirmasi.setHeaderText("Batalkan Pengisian Form?");
        konfirmasi.setContentText("Apakah Anda yakin ingin mengosongkan kembali seluruh kolom inputan ini?");

        // Menyuntikkan style CSS kustom ke pop-up konfirmasi
        konfirmasi.getDialogPane().getStylesheets().add(getClass().getResource("tambah-style.css").toExternalForm());

        // Menunggu aksi klik dari user (OK atau Cancel)
        Optional<ButtonType> hasil = konfirmasi.showAndWait();
        
        if (hasil.isPresent() && hasil.get() == ButtonType.OK) {
            // Jika user memilih OK, kosongkan seluruh field inputan
            txtNama.clear();
            txtHarga.clear();
            txtStok.clear();
        }
    }

    // FUNGSI HELPER: Memanggil pop-up sekaligus menyuntikkan file CSS kustom secara otomatis
    private void tampilkanPopup(AlertType tipe, String judul, String header, String pesan) {
        Alert alert = new Alert(tipe);
        alert.setTitle(judul);
        alert.setHeaderText(header);
        alert.setContentText(pesan);
        
        // Menyuntikkan style CSS kustom ke pop-up info/warning/error
        alert.getDialogPane().getStylesheets().add(getClass().getResource("tambah-style.css").toExternalForm());
        
        alert.showAndWait();
    }
}