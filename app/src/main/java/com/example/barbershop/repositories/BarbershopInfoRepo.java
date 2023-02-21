package com.example.barbershop.repositories;

import static com.example.barbershop.model.Constants.COLLECTION_BARBERSHOP_INFO;
import static com.example.barbershop.model.Constants.DOCUMENT_BARBERSHOP_INFO;

import androidx.annotation.NonNull;

import com.example.barbershop.interfaces.IGETBarbershopInfo;
import com.example.barbershop.interfaces.IUPDATEBarbershopInfo;
import com.example.barbershop.model.BarbershopInfo;
import com.example.barbershop.model.Schedule;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class BarbershopInfoRepo {

    private FirebaseFirestore mFirebaseFirestore;
    public BarbershopInfoRepo() {
        mFirebaseFirestore = FirebaseFirestore.getInstance();
    }

    public void getBarbershopInfo(IGETBarbershopInfo listener) {
        DocumentReference docRef = mFirebaseFirestore.collection(COLLECTION_BARBERSHOP_INFO).document(DOCUMENT_BARBERSHOP_INFO);

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    BarbershopInfo barbershopInfo = document.toObject(BarbershopInfo.class);
                    listener.onSuccess(barbershopInfo);
                } else {
                    listener.onEmptyResult();
                }

            } else {
                listener.onError(task.getException().getMessage());
            }
        });
    }

    public void updateBarbershopInfo(String phoneNumber, String address, Schedule schedule, IUPDATEBarbershopInfo listener) {
        phoneNumber = phoneNumber.replace("Phone:", "").trim();
        address = address.replace("Address:", "").trim();
        BarbershopInfo barbershopInfo = generateBarbershopInfo(phoneNumber, address, schedule);

        mFirebaseFirestore.collection(COLLECTION_BARBERSHOP_INFO).document(DOCUMENT_BARBERSHOP_INFO).set(barbershopInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    listener.onSuccess();
                    return;
                }

                listener.onError(task.getException().getMessage());
            }
        });
    }

    private BarbershopInfo generateBarbershopInfo(String phoneNumber, String address, Schedule schedule) {
        return new BarbershopInfo(phoneNumber, address, schedule);
    }
}
