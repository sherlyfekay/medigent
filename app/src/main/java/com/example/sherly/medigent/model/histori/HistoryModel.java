package com.example.sherly.medigent.model.histori;

import java.util.ArrayList;

public class HistoryModel {
    Integer count;
    String status;
    ArrayList<DetailHistoryModel> histories;

    public Integer getCount() {
        return count;
    }

    public String getStatus() {
        return status;
    }

    public ArrayList<DetailHistoryModel> getDetailHistoryModels() {
        return histories;
    }
}
