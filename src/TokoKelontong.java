import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class TokoKelontong {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Barang> menuBarang = new ArrayList<>();
        HashMap<String, Integer> pesanan = new HashMap<>();
        int totalHarga = 0;

        menuBarang.add(new Barang("Minyak Goreng", 12000));
        menuBarang.add(new Barang("Sabun", 5000));
        menuBarang.add(new Barang("Gula", 16000));
        menuBarang.add(new Barang("Garam", 6000));

        int pilihMenu;
        do {
            tampilkanMenuUtama();
            System.out.print("Pilih Menu Utama (1-3):");
            pilihMenu = sc.nextInt();
            sc.nextLine();

            switch (pilihMenu) {
                case 1:
                    pesanBarang(menuBarang, pesanan, totalHarga, sc);
                    break;
                case 2:
                    tampilkanPesanan(pesanan, menuBarang);
                    break;
                case 3:
                    konfirmasiDanPembayaran(pesanan, menuBarang, totalHarga);
                    break;
                default:
                    System.out.println("Pilihan Tidak Valid. Silahkan Pilih Lagi");
            }

        } while (pilihMenu != 3);
    }

    public static void tampilkanMenuUtama() {
        System.out.println("=========== Selamat Datang di Toko Kelontong ===========");
        System.out.println(">> Menu Utama <<");
        System.out.println("1. Pesan Barang");
        System.out.println("2. Lihat Pesanan");
        System.out.println("3. Konfirmasi dan Pembayaran");
    }

    public static void tampilkanMenuBarang(ArrayList<Barang> menuBarang) {
        System.out.println("Berikut Merupakan Menu Barang yang Tersedia: ");
        for (int i = 0; i < menuBarang.size(); i++) {
            Barang barang = menuBarang.get(i);
            System.out.println((i + 1) + ". " + barang.getNama() + " - Rp. " + barang.getHarga());
        }
        System.out.println("0. Selesai Memesan");
    }

    public static void pesanBarang(ArrayList<Barang> menuBarang, HashMap<String, Integer> pesanan, int totalHarga, Scanner sc) {
        int pilihBarang;
        do {
            tampilkanMenuBarang(menuBarang);
            System.out.println("Pilih Menu: ");
            pilihBarang = sc.nextInt();
            sc.nextLine();

            if (pilihBarang >= 1 && pilihBarang <= menuBarang.size()) {
                System.out.println("Masukkan Jumlah Pesanan Untuk Barang: " + menuBarang.get(pilihBarang - 1).getNama() + " (0 untuk Batal)");
                int jumlah = sc.nextInt();
                sc.nextLine();

                if (jumlah > 0) {
                    Barang barangTerpilih = menuBarang.get(pilihBarang - 1);
                    pesanBarang(pesanan, barangTerpilih, jumlah);
                    totalHarga += barangTerpilih.getHarga() * jumlah;
                    System.out.println("Pesanan " + barangTerpilih.getNama() + " (berjumlah " + jumlah + ") berhasil dipesan.");
                } else {
                    System.out.println("Pesanan Dibatalkan");
                }
            }
        } while (pilihBarang != 0);
    }


    public static void pesanBarang(HashMap<String, Integer> pesananBarang, Barang barang, int jumlah) {
        if (pesananBarang.containsKey(barang.getNama())) {
            int jumlahSebelumnya = pesananBarang.get(barang.getNama());
            pesananBarang.put(barang.getNama(), jumlahSebelumnya + jumlah);
        } else {
            pesananBarang.put(barang.getNama(), jumlah);
        }
    }

    public static void tampilkanPesanan(HashMap<String, Integer> pesananBarang, ArrayList<Barang> menuBarang) {
        System.out.println("Pilihan Barang Anda : ");
        for (Barang barang : menuBarang) {
            String namaBarang = barang.getNama();
            int jumlah = pesananBarang.getOrDefault(namaBarang, 0);
            if (jumlah > 0) {
                System.out.println(namaBarang + " - Rp. " + barang.getHarga() + " (Jumlah: " + jumlah + ")");
            }
        }
    }

    public static void konfirmasiDanPembayaran(HashMap<String, Integer> pesananBarang, ArrayList<Barang> menuBarang, int totalHarga) {
        tampilkanPesanan(pesananBarang, menuBarang);
        System.out.println("Total Harga " + totalHarga);

        Scanner sc = new Scanner(System.in);
        System.out.print("Apakah Anda ingin mengkonfirmasi pesanan (Y/N)? ");
        String konfirmasi = sc.nextLine();

        if (konfirmasi.equalsIgnoreCase("Y")) {
            simpanStrukPembelian(pesananBarang, menuBarang, totalHarga);
            System.out.println("Terima Kasih Telah Membeli di Toko Kelontong Kami!");
            System.exit(0);
        } else {
            System.out.println("Pesanan Anda belum dikonfirmasi. Silahkan kembali ke menu utama atau keluar.");
        }
    }

    public static void simpanStrukPembelian(HashMap<String, Integer> pesananBarang, ArrayList<Barang> menuBarang, int totalHarga) {
        try (PrintWriter printStruk = new PrintWriter(new FileWriter("struk_pembelian_barang.txt"))) {
            printStruk.println("Struk Pembelian");
            printStruk.println("================");
            printStruk.println("Pembelian Barang Anda : ");
            for (Barang barang : menuBarang) {
                String namaBarang = barang.getNama();
                int jumlah = pesananBarang.getOrDefault(namaBarang, 0);
                if (jumlah > 0) {
                    printStruk.println(namaBarang + " - Rp. " + barang.getHarga() + " (Jumlah: " + jumlah + ")");
                }
            }
            printStruk.println("Total Harga : Rp. " + totalHarga);
            System.out.println();
            System.out.println("Struk pembelian telah disimpan di struk_pembelian_barang.txt");
        } catch (IOException e) {
            System.err.println("Error saat menyimpan struk pembelian: " + e.getMessage());
        }
    }
}

class Barang {
    private String nama;
    private int harga;

    public Barang(String nama, int harga) {
        this.nama = nama;
        this.harga = harga;
    }

    public String getNama() {
        return nama;
    }

    public int getHarga() {
        return harga;
    }
}
