package com.gofar.anodais.Model;

public class Pembatalan {
   private String id,dari,ke;

    public Pembatalan(String id, String dari, String ke) {
        this.id = id;
        this.dari = dari;
        this.ke = ke;
    }


    public Pembatalan()
    {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDari() {
        return dari;
    }

    public void setDari(String dari) {
        this.dari = dari;
    }

    public String getKe() {
        return ke;
    }

    public void setKe(String ke) {
        this.ke = ke;
    }
}
