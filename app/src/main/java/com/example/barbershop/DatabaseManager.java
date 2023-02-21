package com.example.barbershop;

import com.example.barbershop.model.User;
import com.google.firebase.firestore.FirebaseFirestore;

public class DatabaseManager {

    public DatabaseManager() {
        FirebaseFirestore mFirebaseFirestore = FirebaseFirestore.getInstance();
    }

}
