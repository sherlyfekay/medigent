package com.example.sherly.medigent.model.home;

public class DaftarArtikelModel {
    private int foto;
    private String judul;
    private String sumber;

    public DaftarArtikelModel() {
    }

    public DaftarArtikelModel(int foto, String judul, String sumber) {
        this.foto = foto;
        this.judul = judul;
        this.sumber = sumber;
    }

    public int getFoto() {
        return foto;
    }

    public void setFoto(int foto) {
        this.foto = foto;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getSumber() {
        return sumber;
    }

    public void setSumber(String sumber) {
        this.sumber = sumber;
    }
}
