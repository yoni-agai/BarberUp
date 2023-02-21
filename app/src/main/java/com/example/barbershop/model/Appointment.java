package com.example.barbershop.model;

import java.util.HashMap;

public class Appointment {

    private String customerName;
    private String userId;
    private String status;
    private String note;
    private String hour;

    public Appointment() {
    }

    public Appointment(String status, String hour) {
        this.customerName = "";
        this.userId = "";
        this.status = status;
        this.note = "";
        this.hour = hour;
    }

    public Appointment(String customerName, String userId, String status, String note, String hour) {
        this.customerName = customerName;
        this.userId = userId;
        this.status = status;
        this.note = note;
        this.hour = hour;
    }

    public Appointment(HashMap<String, String> map) {
        this.customerName = map.get("customerName");
        this.userId = map.get("userId");
        this.status = map.get("status");
        this.note = map.get("note");
        this.hour = map.get("hour");
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getUserId() {
        return userId;
    }

    public String getStatus() {
        return status;
    }

    public String getNote() {
        return note;
    }

    public String getHour() {
        return hour;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }
}
