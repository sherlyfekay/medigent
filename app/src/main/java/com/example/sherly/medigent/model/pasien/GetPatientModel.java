package com.example.sherly.medigent.model.pasien;

import java.util.ArrayList;

public class GetPatientModel {
    int count;
    String status;
    ArrayList<PostPatientModel> patients;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<PostPatientModel> getPatients() {
        return patients;
    }

    public void setPatients(ArrayList<PostPatientModel> patients) {
        this.patients = patients;
    }
}
