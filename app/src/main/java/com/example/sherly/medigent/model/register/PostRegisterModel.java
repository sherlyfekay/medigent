package com.example.sherly.medigent.model.register;

public class PostRegisterModel {
    String nama_lengkap, email, password, telepon;

    public PostRegisterModel(String nama_lengkap, String email, String telepon, String password) {
        this.nama_lengkap = nama_lengkap;
        this.email = email;
        this.password = password;
        this.telepon = telepon;
    }
}
