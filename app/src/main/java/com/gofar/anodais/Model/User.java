package com.gofar.anodais.Model;

public class User {

    private String id_user, username,email,notelp;

    public User(String id_user, String username,String email, String notelp) {
        this.id_user = id_user;
        this.username = username;
        this.email=email;
        this.notelp=notelp;
    }

    public User() {

    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNotelp() {
        return notelp;
    }

    public void setNotelp(String notelp) {
        this.notelp = notelp;
    }
}
