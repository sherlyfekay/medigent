package com.example.sherly.medigent.model.alamat;

public class AddressModel {
    String _id, judul, alamat_lengkap, tambahan, id_user;
    Double lat, lng;

    public AddressModel(String judul, String alamat_lengkap, String tambahan, Double lat, Double lng, String id_user) {
        this.judul = judul;
        this.alamat_lengkap = alamat_lengkap;
        this.tambahan = tambahan;
        this.lat = lat;
        this.lng = lng;
        this.id_user = id_user;
    }

    public AddressModel(String _id, String judul, String alamat_lengkap, String tambahan, Double lat, Double lng, String id_user) {
        this._id = _id;
        this.judul = judul;
        this.alamat_lengkap = alamat_lengkap;
        this.tambahan = tambahan;
        this.id_user = id_user;
        this.lat = lat;
        this.lng = lng;
    }

    public AddressModel(String tambahan) {
        this.tambahan = tambahan;
    }

    public String get_id() {
        return _id;
    }

    public String getJudul() {
        return judul;
    }

    public String getAlamat_lengkap() {
        return alamat_lengkap;
    }

    public String getTambahan() {
        return tambahan;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    public String getId_user() {
        return id_user;
    }
}
