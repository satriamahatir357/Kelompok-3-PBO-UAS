package com.produk.model;

// Kelas cetakan (blueprint) untuk objek Produk
public class Produk {
    private String nama;
    private double harga;
    private int stok;
    private String diskon; // Tambahan variabel untuk menampung diskon riil

    // Constructor untuk membuat objek produk baru
    public Produk(String nama, double harga, int stok) {
        this.nama = nama;
        this.harga = harga;
        this.stok = stok;
        this.diskon = "-"; // Default awal diatur strip (-) saat produk baru dibuat
    }

    // Getter dan Setter (Wajib ada agar propertinya bisa dibaca oleh TableView JavaFX)
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public double getHarga() { return harga; }
    public void setHarga(double harga) { this.harga = harga; }

    public int getStok() { return stok; }
    public void setStok(int stok) { this.stok = stok; }

    // Getter dan Setter Tambahan untuk Fitur Diskon Produk Mandiri
    public String getDiskon() { return diskon; }
    public void setDiskon(String diskon) { this.diskon = diskon; }
}