package com.example.sherly.medigent.model.mainmenu;

import java.util.ArrayList;

public class RoleModel {
    private int count;
    private int status;
    private ArrayList<DataRole> roles;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<DataRole> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<DataRole> roles) {
        this.roles = roles;
    }
}
