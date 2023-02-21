package com.example.barbershop.model;

public class User {
    private String id;
    private String name;
    private UserType type;

    private boolean hasActiveAppointment;

    public User(String id, String name, UserType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public User(String id, String name, UserType type, boolean hasActiveAppointment) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.hasActiveAppointment = hasActiveAppointment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public boolean isAdmin() {
        return type == UserType.ADMIN;
    }

    public boolean hasActiveAppointment() {
        return hasActiveAppointment;
    }

    public void setHasActiveAppointment(boolean hasActiveAppointment) {
        this.hasActiveAppointment = hasActiveAppointment;
    }
}
