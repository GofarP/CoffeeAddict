package com.gofar.anodais.Model;

public class History {

    String nama, tglpesan, image;
    Double harga, subtotal;
    int jumlah;

    public History()
    {}

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public String getTglpesan() {
        return tglpesan;
    }

    public void setTglpesan(String tglpesan) {
        this.tglpesan = tglpesan;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
