package com.example.sherly.medigent.model.pemesanan;

public class PostOrderModel {
    String id_user, id_patient, id_address, jk_agen, tgl_mulai, jns_layanan, created_at;
    Integer jml_shift, status, jenis;

    public PostOrderModel(Integer jenis, String id_user, String id_patient, String id_address, String jk_agen, String tgl_mulai, String jns_layanan, Integer jml_shift, Integer status, String created_at) {
        this.jenis = jenis;
        this.id_user = id_user;
        this.id_patient = id_patient;
        this.id_address = id_address;
        this.jk_agen = jk_agen;
        this.tgl_mulai = tgl_mulai;
        this.jns_layanan = jns_layanan;
        this.jml_shift = jml_shift;
        this.status = status;
        this.created_at = created_at;
    }
}
