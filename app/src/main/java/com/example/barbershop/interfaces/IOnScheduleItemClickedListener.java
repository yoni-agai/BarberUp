package com.example.barbershop.interfaces;

import com.example.barbershop.model.Appointment;

public interface IOnScheduleItemClickedListener {
    void onItemClicked(Appointment appointment, int position);
}
