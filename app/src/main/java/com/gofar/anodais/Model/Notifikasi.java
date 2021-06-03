package com.gofar.anodais.Model;

public class Notifikasi {

    private String idNotifikasi, pesan, tanggal;

    User user=new User();

    public Notifikasi()
    {

    }

    public String getIdNotifikasi() {
        return idNotifikasi;
    }

    public void setIdNotifikasi(String idNotifikasi) {
        this.idNotifikasi = idNotifikasi;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }


    public String getIdUser()
    {
        return user.getId_user();
    }

    public void setIduser(String idUser)
    {
        user.setId_user(idUser);
    }


}
