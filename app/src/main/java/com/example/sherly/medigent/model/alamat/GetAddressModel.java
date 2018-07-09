package com.example.sherly.medigent.model.alamat;

import java.util.ArrayList;

public class GetAddressModel {
    int count;
    String status;
    ArrayList<AddressModel> addresses;

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

    public ArrayList<AddressModel> getAddresses() {
        return addresses;
    }

    public void setAddresses(ArrayList<AddressModel> addresses) {
        this.addresses = addresses;
    }
}
