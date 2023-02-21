package com.example.barbershop.model;

import java.util.HashMap;

public class Schedule {

    private HashMap<Day, Workday> schedule;

    public Schedule(Workday sunday,Workday monday, Workday tuesday,
                    Workday wednesday, Workday thursday) {
        schedule = new HashMap<>();
        initSchedule(sunday, monday, tuesday, wednesday, thursday);
    }

    private void initSchedule(Workday sunday,Workday monday, Workday tuesday,
                              Workday wednesday, Workday thursday) {
        schedule.put(Day.SUNDAY, sunday);
        schedule.put(Day.MONDAY, monday);
        schedule.put(Day.TUESDAY, tuesday);
        schedule.put(Day.WEDNESDAY, wednesday);
        schedule.put(Day.THURSDAY, thursday);
    }

    public Workday getWorkday(Day day) {
        return schedule.get(day);
    }

    public void setWorkday(Workday workday) {
        schedule.put(workday.getDay(), workday);
    }
}
