package com.example.barbershop.model;

public class Workday {

    private String startTime;
    private String endTime;
    private String workdayHours;
    private Day day;

    public Workday(String dataBaseText) {
        String startTime = fetchStartTime(dataBaseText);
        String endTime = fetchEndTime(dataBaseText);
        this.startTime = startTime;
        this.endTime = endTime;
        setWorkdayHours();
    }

    private String fetchStartTime(String dataBaseText) {
        return dataBaseText.split("-")[0].trim().substring(0, 2).replace(":", "") + ":00";
    }

    private String fetchEndTime(String dataBaseText) {
        return dataBaseText.split("-")[1].trim().substring(0, 2).replace(":", "") + ":00";
    }

    public Workday(String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        setWorkdayHours();
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
        setWorkdayHours();
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
        setWorkdayHours();
    }

    public String getWorkdayHours() {
        return workdayHours;
    }

    private void setWorkdayHours() {
        this.workdayHours = startTime + " - " + endTime;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

}
