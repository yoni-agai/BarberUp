package com.example.barbershop.repositories;

import static com.example.barbershop.model.Constants.COLLECTION_USERS_UPCOMING_APPOINTMENT;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.barbershop.SharedPreferencesManager;
import com.example.barbershop.interfaces.IGETClientNextAppointment;
import com.example.barbershop.interfaces.IUPDATENextAppointment;
import com.example.barbershop.model.AppointmentStatus;
import com.example.barbershop.model.NextAppointment;
import com.example.barbershop.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UsersUpcomingAppointmentRepo {
    private FirebaseFirestore mFirebaseFirestore;
    private Context mContext;
    private UserRepo mUserRepo;

    public UsersUpcomingAppointmentRepo(Context context, UserRepo userRepo) {
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mContext = context;
        mUserRepo = userRepo;
    }

    public void setNextAppointment(NextAppointment nextAppointment, String userId, IUPDATENextAppointment listener) {

        if (userId == null) {
            userId = mUserRepo.getUser().getId();
        }

        mFirebaseFirestore.collection(COLLECTION_USERS_UPCOMING_APPOINTMENT).document(userId).set(nextAppointment).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    boolean hasActiveNextAppointment = getActiveAppointmentStatus(nextAppointment.getStatus());
                    SharedPreferencesManager.saveActiveAppointmentState(mContext, hasActiveNextAppointment);
                    listener.onSuccess();
                    return;
                }

                listener.onError(task.getException().getMessage());
            }
        });
    }

    private boolean getActiveAppointmentStatus(String status) {
        if (status.equals(AppointmentStatus.BOOKED.name())) {
            // appointment is set and booked.
            return true;
        }

        return false;
    }

    public void getClientNextAppointment(IGETClientNextAppointment listener) {

        User user = SharedPreferencesManager.getUser(mContext);

        if (user.isAdmin()) {
            return;
        }

        String userId = user.getId();
        DocumentReference docRef = mFirebaseFirestore.collection(COLLECTION_USERS_UPCOMING_APPOINTMENT).document(userId);

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    NextAppointment nextAppointment =
                            document.toObject(NextAppointment.class);
                            listener.onSuccess(nextAppointment);
                } else {
                    listener.onSuccess(null);
                }
                return;
            }
            listener.onError(task.getException().getMessage());
        });
    }
}
