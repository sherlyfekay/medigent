package com.example.sherly.medigent.model.pasien;

public class PostPatientModel {
    String _id, nama_lengkap, jk, tgl_lahir, berat, tinggi, hubungan, alat, diagnosa, kondisi, id_user;

    public PostPatientModel(String nama_lengkap, String jk, String tgl_lahir, String berat, String tinggi, String hubungan, String alat, String diagnosa, String kondisi, String id_user) {
        this.nama_lengkap = nama_lengkap;
        this.jk = jk;
        this.tgl_lahir = tgl_lahir;
        this.berat = berat;
        this.tinggi = tinggi;
        this.hubungan = hubungan;
        this.alat = alat;
        this.diagnosa = diagnosa;
        this.kondisi = kondisi;
        this.id_user = id_user;
    }

    public String get_id() {
        return _id;
    }

    public String getNama_lengkap() {
        return nama_lengkap;
    }

    public String getJk() {
        return jk;
    }

    public String getTgl_lahir() {
        return tgl_lahir;
    }

    public String getBerat() {
        return berat;
    }

    public String getTinggi() {
        return tinggi;
    }

    public String getHubungan() {
        return hubungan;
    }

    public String getAlat() {
        return alat;
    }

    public String getDiagnosa() {
        return diagnosa;
    }

    public String getKondisi() {
        return kondisi;
    }

    public String getId_user() {
        return id_user;
    }
}
