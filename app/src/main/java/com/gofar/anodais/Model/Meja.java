package com.gofar.anodais.Model;

public class Meja  {

    private String id;
    private String nomeja;
    private String image;
    private String jenis;

    public Meja()
    {

    }

    public Meja(String id,String nomeja, String image) {
        this.id=id;
        this.nomeja = nomeja;
        this.image = image;
    }

    public String getNomeja() {
        return nomeja;
    }

    public void setNomeja(String nomeja) {
        this.nomeja = nomeja;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image=image;
    }

    public String getJenis(){
        return jenis;
    }

    public void setJenis(String jenis)
    {
        this.jenis=jenis;
    }

}
