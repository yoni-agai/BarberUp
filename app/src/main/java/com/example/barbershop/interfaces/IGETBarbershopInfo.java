package com.example.barbershop.interfaces;

import com.example.barbershop.model.BarbershopInfo;

public interface IGETBarbershopInfo {
    void onSuccess(BarbershopInfo barbershopInfo);
    void onEmptyResult();
    void onError(String message);
}
