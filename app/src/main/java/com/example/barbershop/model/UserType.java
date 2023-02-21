package com.example.barbershop.model;

public enum UserType {
    ADMIN,
    CLIENT;

    public static UserType fromInteger(int x) {
        switch(x) {
            case 0:
                return ADMIN;
            case 1:
                return CLIENT;
        }
        return null;
    }
}