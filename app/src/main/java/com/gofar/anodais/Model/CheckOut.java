package com.gofar.anodais.Model;

public class CheckOut {
    private String id_checkout,alamat,jumlah;
    private  Double subtotal;

    Menu menu=new Menu();

    User user=new User();

    Pesanan pesanan=new Pesanan();


    public CheckOut(String id_checkout, String alamat, String jumlah, Double subtotal) {
        this.id_checkout = id_checkout;
        this.alamat = alamat;
        this.jumlah = jumlah;
        this.subtotal = subtotal;
    }

    public  CheckOut()
    {

    }

    public String getId_checkout() {
        return id_checkout;
    }

    public void setId_checkout(String id_checkout) {
        this.id_checkout = id_checkout;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public String getNamaMenu() {
        return menu.getNama();
    }

    public void setNamaMenu(String namamenu) {
        menu.setNama(namamenu);
    }

    public Double getHarga() {
        return menu.getHarga();
    }

    public void setHarga(Double harga) {
        menu.setHarga(harga);
    }


    public String getImage()
    {
        return menu.getImage();
    }

    public void setImage(String image)
    {
        menu.setImage(image);
    }


    public String getIdUser()
    {
        return user.getId_user();
    }

    public void setIdUser(String iduser)
    {
        user.setId_user(iduser);
    }

    public String getUser()
    {
        return  user.getUsername();
    }

    public void setUser(String username)
    {
        user.setUsername(username);
    }


    public Double getTotal(){return pesanan.getTotal();}

    public void setTotal(Double total)
    {
        pesanan.setTotal(total);
    }

    public String getNoTelp()
    {
        return user.getNotelp();
    }

    public void setNoTelp(String notelp)
    {
        user.setNotelp(notelp);
    }


}
