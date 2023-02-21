package com.example.barbershop.model;

public enum Day {
    SUNDAY,
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,

    CLOSED;

    public static Day fromInteger(int x) {
        switch(x) {
            case 1:
                return SUNDAY;
            case 2:
                return MONDAY;
            case 3:
                return TUESDAY;
            case 4:
                return WEDNESDAY;
            case 5:
                return THURSDAY;
        }
        return CLOSED;
    }
}