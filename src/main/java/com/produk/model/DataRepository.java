package com.produk.model;

import java.util.ArrayList;

public class DataRepository {
    
    // ArrayList global yang menampung data produk riil selama aplikasi berjalan.
    // Dimulai dari kosongan (0) sesuai keinginanmu.
    private static ArrayList<Produk> daftarProduk = new ArrayList<>();

    // Method untuk mengambil data ArrayList dari controller mana pun
    public static ArrayList<Produk> getDaftarProduk() {
        return daftarProduk;
    }

    // Method untuk menambahkan produk baru ke dalam ArrayList saat tombol simpan diklik
    public static void tambahProduk(Produk produk) {
        daftarProduk.add(produk);
    }
}