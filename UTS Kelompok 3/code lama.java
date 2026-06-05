import java.util.Scanner;

//Class Identitas
class Identitas {
    static void tampil() {
        System.out.println("\n");
        System.out.println("Nama  : Kelompok 3 PBO");
        System.out.println("Kelas : Teknik Informatika 2025 I");
        System.out.println("\n");
    }
}
//Parent Class (Enskapsulasi)
class Item {
    String nama;
    int harga;

    Item(String nama,int harga) {
        this.nama = nama;
        this.harga = harga;
    }
    void tampil() {
        System.out.println(nama+"|Harga:Rp." + harga);
    }

    //Enskapsulasi
    public String getNama() {
        return nama;
    }
    public int getHarga() {
        return harga;
    }
    public void setHarga(int harga) {
        if (harga > 0){
            this.harga = harga;
        } else{
            System.out.println("Harga tidak valid!");
        }
    }
    public String getKategoriHarga() {
        if (harga > 100000) return "Mahal";
        else if (harga > 50000) return "Sedang";
        else return "Murah";
    }

    //Polimorfisme (Overloading)
    void tampil(boolean detail) {
        tampil();
        if (detail){
            System.out.println("Kategori: " + getKategoriHarga());
        }
    }
}

//Child Class (Inheritance)
class Produk extends Item {
    Produk(String nama,int harga) {
        super(nama,harga);
    }
}

//Pewarisan Lanjutan + Polimorfisme
class ProdukDiskon extends Produk {
    int diskon;

    ProdukDiskon(String nama, int harga, int diskon) {
        super(nama, harga);
        this.diskon = diskon;
    }
    
    int hargaSetelahDiskon() {
        return harga - (harga*diskon/100);
    }

    @Override
    void tampil() {
        super.tampil();
        System.out.println("Diskon:" + diskon +"%");
        System.out.println("Harga setelah diskon: Rp." + hargaSetelahDiskon());
    }
}

public class Main {
    //Bubble Sort berdasarkan harga
    public static void bubbleSort(Produk[] data) {
        for (int i = 0; i < data.length - 1; i++) {
            for (int j = 0; j < data.length - 1 - i; j++) {
                if (data[j].harga > data[j+1].harga) {
                    Produk temp = data[j];
                    data[j] = data[j+1];
                    data[j+1] = temp;
                }
            }
        }
    }

    //SORT TAMBAHAN (BERDASARKAN NAMA)
    public static void sortNama(Produk[] data) {
        for (int i = 0; i < data.length - 1; i++) {
            for (int j = 0; j < data.length - 1 - i; j++) {
                if (data[j].nama.compareToIgnoreCase(data[j+1].nama) > 0) {
                    Produk temp = data[j];
                    data[j] = data[j+1];
                    data[j+1] = temp;
                }
            }
        }
    }
    //Linear Search berdasarkan nama
    public static int linearSearch(Produk[] data, String key) {
        for (int i = 0; i < data.length; i++) {
            if (data[i].nama.equalsIgnoreCase(key)) {
                return i;
            }
        }
        return -1;
    }

    //SEARCH TAMBAHAN (BERDASARKAN HARGA)
    public static void cariHarga(Produk[] data, int harga) {
        boolean ditemukan = false;

        for (Produk p : data) {
            if (p.harga == harga) {
                p.tampil();
                ditemukan = true;
            }
        }

        if (!ditemukan) {
            System.out.println("Produk dengan harga tersebut tidak ditemukan.");
        }
    }

    public static void main(String[] args) {

        // Menampilkan identitas
        Identitas.tampil ();

        Scanner input = new Scanner(System.in);

        System.out.print("Jumlah produk: ");
        int n = input.nextInt();
        input.nextLine();

        Produk[] daftar = new Produk[n];

        // Input data
        for (int i = 0; i < n; i++) {
            System.out.print("Nama produk: ");
            String nama = input.nextLine();

            System.out.print("Harga: ");
            int harga = input.nextInt();
            input.nextLine();

            daftar[i] = new Produk(nama, harga);
        }

        //Tampilkan sebelum sorting
        System.out.println("\n=== Data Produk Sebelum Sorting ===");
        for (Produk p : daftar) {
            p.tampil();
        }

        //Sorting harga
        bubbleSort(daftar);

        System.out.println("\n=== Data Produk Setelah Sorting (Termurah) ===");
        for (Produk p : daftar) {
            p.tampil();
        }

        //Sorting nama (improvisasi)
        sortNama(daftar);

        System.out.println("\n=== Data Produk Setelah Sorting Nama ===");
        for (Produk p : daftar) {
            p.tampil();
        }

        //Searching nama
        System.out.print("\nMasukkan nama produk yang ingin dicari: ");
        String cari = input.nextLine();

        int hasil = linearSearch(daftar, cari);

        if (hasil != -1) {
            System.out.println("Produk ditemukan:");
            daftar[hasil].tampil();
        } else {
            System.out.println("Produk tidak ditemukan.");
        }

        //DEMO POLIMORFISME
        System.out.println("\n=== Tampilan Detail (Polimorfisme) ===");
        for (Produk p : daftar) {
            p.tampil(true);
            System.out.println("-------------------");
        }

        //DEMO PEWARISAN
        System.out.println("\n=== Contoh Produk Diskon ===");
        ProdukDiskon pd = new ProdukDiskon("Laptop", 8000000, 10);
        pd.tampil();

        //SEARCH BERDASARKAN HARGA
        System.out.print("\nCari berdasarkan harga: ");
        int h = input.nextInt();
        cariHarga(daftar, h);

        input.close();
    }
}