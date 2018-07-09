package com.example.sherly.medigent.model.penawaran;

public class PostOfferModel {
    String jk_agen, tgl_mulai, biaya, info, id_user, id_address, id_patient, created_at;
    Integer jml_shift, jenis, status;

    public PostOfferModel(String jk_agen, String tgl_mulai, String biaya, String info, String id_user, String id_address, String id_patient, String created_at, Integer jml_shift, Integer jenis, Integer status) {
        this.jk_agen = jk_agen;
        this.tgl_mulai = tgl_mulai;
        this.biaya = biaya;
        this.info = info;
        this.id_user = id_user;
        this.id_address = id_address;
        this.id_patient = id_patient;
        this.created_at = created_at;
        this.jml_shift = jml_shift;
        this.jenis = jenis;
        this.status = status;
    }
}
