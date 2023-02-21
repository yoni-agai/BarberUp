package com.example.barbershop.interfaces;

import com.example.barbershop.model.Appointment;

import java.util.ArrayList;
import java.util.List;

public interface IGETSchedule {
    void onSuccess(ArrayList<Appointment> list);
    void onError(String error);
}
