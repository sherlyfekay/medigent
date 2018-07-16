package com.example.sherly.medigent.model.histori;

public class DetailHistoryModel {
    Integer jml_shift, status;
    String _id, created_at, jenis, nama_pasien, diagnosa, nama_agent, role, alamat_lengkap;
    Double lat, lng;

    public Integer getJml_shift() {
        return jml_shift;
    }

    public String get_id() {
        return _id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getJenis() {
        return jenis;
    }

    public String getNama_pasien() {
        return nama_pasien;
    }

    public String getDiagnosa() {
        return diagnosa;
    }

    public String getNama_agent() {
        return nama_agent;
    }

    public String getRole() {
        return role;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    public Integer getStatus() {
        return status;
    }

    public String getAlamat_lengkap() {
        return alamat_lengkap;
    }
}
