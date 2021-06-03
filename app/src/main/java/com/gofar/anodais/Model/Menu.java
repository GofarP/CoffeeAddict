package com.gofar.anodais.Model;

public class Menu {
    private String id_menu,nama,status,jenis,deskripsi,image;
    private Double harga;
    private int stock;

    public Menu(){

    }

//    public Menu(String id_menu, String nama, Double harga, String status,String jenis,String deskripsi, String image, int stock) {
//        this.id_menu = id_menu;
//        this.nama = nama;
//        this.harga = harga;
//        this.status=status;
//        this.jenis=jenis;
//        this.deskripsi=deskripsi;
//        this.image=image;
//        this.stock=stock;
//
//    }

    public String getId() {
        return id_menu;
    }

    public void setId(String id_menu) {
        this.id_menu = id_menu;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Double getHarga() {
        return harga;
    }

    public void setHarga(Double harga) {
        this.harga = harga;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
