package com.example.sherly.medigent.model.shift;

import java.util.ArrayList;

public class ShiftModel {
    Integer count;
    String status;
    ArrayList<DetailShiftModel> shifts;

    public Integer getCount() {
        return count;
    }

    public String getStatus() {
        return status;
    }

    public ArrayList<DetailShiftModel> getShifts() {
        return shifts;
    }
}
