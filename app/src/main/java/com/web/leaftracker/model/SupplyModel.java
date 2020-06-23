package com.web.leaftracker.model;

public class SupplyModel {
    int id;
    String date;
    String weight;
    String offer_price;
    String exp_fine_leaf_count;
    String exp_moisture;
    int confirm_status;

    public SupplyModel(int id, String date, String weight, int confirm_status) {
        this.id = id;
        this.date = date;
        this.weight = weight;
        this.confirm_status = confirm_status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public int getConfirm_status() {
        return confirm_status;
    }

    public void setConfirm_status(int confirm_status) {
        this.confirm_status = confirm_status;
    }
}
