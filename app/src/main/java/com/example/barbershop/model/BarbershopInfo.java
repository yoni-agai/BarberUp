package com.example.barbershop.model;

public class BarbershopInfo {

    private String phoneNumber;
    private String address;
    private String sunday;
    private String monday;
    private String tuesday;
    private String wednesday;
    private String thursday;

    public BarbershopInfo() {
    }

    public BarbershopInfo(String phoneNumber, String address, Schedule schedule) {
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.sunday = schedule.getWorkday(Day.SUNDAY).getWorkdayHours();
        this.monday = schedule.getWorkday(Day.MONDAY).getWorkdayHours();
        this.tuesday = schedule.getWorkday(Day.TUESDAY).getWorkdayHours();
        this.wednesday = schedule.getWorkday(Day.WEDNESDAY).getWorkdayHours();
        this.thursday = schedule.getWorkday(Day.THURSDAY).getWorkdayHours();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getSunday() { return sunday; }

    public String getMonday() { return monday; }

    public String getTuesday() { return tuesday; }

    public String getWednesday() { return wednesday; }

    public String getThursday() { return thursday; }

}
