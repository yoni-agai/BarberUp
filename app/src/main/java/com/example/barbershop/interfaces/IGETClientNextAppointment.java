package com.example.barbershop.interfaces;

import com.example.barbershop.model.NextAppointment;

public interface IGETClientNextAppointment {

    void onSuccess(NextAppointment nextAppointment);
    void onError(String message);
}
