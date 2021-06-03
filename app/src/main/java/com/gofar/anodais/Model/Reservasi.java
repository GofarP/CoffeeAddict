package com.gofar.anodais.Model;

import java.math.BigInteger;

public class Reservasi  {

    private String id_reservasi, tgl_sewa,status,jenis;

    Pembatalan pembatalan=new Pembatalan();

    Meja meja=new Meja();

    User user=new User();

    public Reservasi(String id_reservasi, String tgl_sewa, String status ) {

        this.id_reservasi = id_reservasi;
        this.tgl_sewa = tgl_sewa;
        this.status = status;

    }

    public Reservasi()
    {

    }



    public String getId_reservasi() {
        return id_reservasi;
    }

    public void setId_reservasi(String id_reservasi) {
        this.id_reservasi = id_reservasi;
    }

    public String getId_meja() {
        return meja.getId();
    }

    public void setId_meja(String id_meja) {
        meja.setId(id_meja);
    }

    public String getId_user() {
        return user.getId_user();
    }

    public void setId_user(String id_user) {
        user.setId_user(id_user);
    }

    public String getTgl_sewa() {
        return tgl_sewa;
    }

    public void setTgl_sewa(String tgl_sewa) {
        this.tgl_sewa = tgl_sewa;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return user.getUsername();
    }

    public void setUsername(String username) {
        user.setUsername(username);
    }

    public String getEmail(){return user.getEmail();}

    public void setEmail(String email)
    {
        user.setEmail(email);
    }

    public String getNoTelp(){return user.getNotelp();}

    public void setNoTelp(String noTelp)
    {
        user.setNotelp(noTelp);
    }

    public String getNomeja() {
        return meja.getNomeja();
    }

    public void setNomeja(String nomeja) {
        meja.setNomeja(nomeja);
    }

    public String getIdPembatalan()
    {
        return pembatalan.getId();
    }

    public void setIdPembatalan(String idpembatalan)
    {
        pembatalan.setId(idpembatalan);
    }

    public String getMejaImage()
    {
        return meja.getImage();
    }

    public void setMejaImage(String image)
    {
        meja.setImage(image);
    }

    public String getJenis(){
        return meja.getJenis();
    }

    public void setJenis(String jenis)
    {
        meja.setJenis(jenis);
    }
}
