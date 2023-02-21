package com.example.barbershop.model;

public class NextAppointment {

    private String date;
    private String hour;
    private String status;

    public NextAppointment() {
    }

    public NextAppointment(String date, String hour, String status) {
        this.date = date;
        this.hour = hour;
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public String getHour() {
        return hour;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
