package com.gofar.anodais.Model;

public class Keranjang {

    private String idkeranjang;
    private int jumlah;
    private Double subtotal;

    User pelanggan=new User();

    Menu menu=new Menu();

    Catatan catatan=new Catatan();

    public Keranjang()
    {

    }


    public Keranjang(String idkeranjang, int jumlah, Double subtotal ) {
        this.idkeranjang = idkeranjang;
        this.jumlah = jumlah;
        this.subtotal = subtotal;

    }

    public String getIdkeranjang() {
        return idkeranjang;
    }

    public void setIdkeranjang(String idkeranjang) {
        this.idkeranjang = idkeranjang;
    }

    public String getIdpelanggan() {
        return pelanggan.getId_user();
    }

    public void setIdpelanggan(String idpelanggan) {
        pelanggan.setId_user(idpelanggan);
    }

    public String getIdmenu() {
        return menu.getId();
    }

    public void setIdmenu(String idmenu) {
        menu.setId(idmenu);
    }

    public String getJenis() {
        return menu.getJenis();
    }

    public void setJenis(String jenis) {
        menu.setJenis(jenis);
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getHarga() {
        return menu.getHarga();
    }

    public void setHarga(Double harga) {
        menu.setHarga(harga);
    }

    public String getNama() {
        return menu.getNama();
    }

    public void setNama(String nama) {
        menu.setNama(nama);
    }

    public String getImage() {
        return menu.getImage();
    }

    public void setImage(String image) {
        menu.setImage(image);
    }

    public int getStock()
    {
        return menu.getStock();
    }

    public void setStock(int stock)
    {
        menu.setStock(stock);
    }

    public String getCatatan()
    {
        return catatan.getCatatan();
    }

    public void setCatatan(String cat)
    {
        catatan.setCatatan(cat);
    }

}
