package com.example.barbershop.repositories;

import static com.example.barbershop.model.Constants.COLLECTION_APPOINTMENTS;
import static com.example.barbershop.model.Constants.COLLECTION_USERS_UPCOMING_APPOINTMENT;

import static java.lang.Integer.parseInt;

import androidx.annotation.NonNull;

import com.example.barbershop.Utils;
import com.example.barbershop.interfaces.IGETBarbershopInfo;
import com.example.barbershop.interfaces.IGETSchedule;
import com.example.barbershop.interfaces.IUPDATEAppointment;
import com.example.barbershop.model.Appointment;
import com.example.barbershop.model.AppointmentStatus;
import com.example.barbershop.model.BarbershopInfo;
import com.example.barbershop.model.Day;
import com.example.barbershop.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AppointmentRepo {

    private FirebaseFirestore mFirebaseFirestore;
    private BarbershopInfoRepo mBarbershopInfoRepo;

    private UserRepo mUserRepo;
    private BarbershopInfo mBarbershopInfo;

    public AppointmentRepo(BarbershopInfoRepo barbershopInfoRepo, UserRepo userRepo) {
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mBarbershopInfoRepo = barbershopInfoRepo;
        mUserRepo = userRepo;
    }

    public void setAppointment(String date, String hour, String status, IUPDATEAppointment listener) {
        User user = mUserRepo.getUser();

        Appointment appointment = new Appointment(user.getName(), user.getId(), status, "", hour);

        Map<String, Object> data = new HashMap<>();
        data.put(Utils.getStartAppointmentStartHourFormat(hour), appointment);

        mFirebaseFirestore.collection(COLLECTION_APPOINTMENTS).document(date).set(data, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    public void getAppointmentsByDate(Day dayOfWeek, int dayOfMonth, int month, int year, IGETSchedule listener) {

        if (mBarbershopInfo != null) {
            fetchAppointmentsByDate(dayOfWeek, dayOfMonth, month, year, listener);
            return;
        }

        mBarbershopInfoRepo.getBarbershopInfo(new IGETBarbershopInfo() {
            @Override
            public void onSuccess(BarbershopInfo barbershopInfo) {
                mBarbershopInfo = barbershopInfo;
                getAppointmentsByDate(dayOfWeek, dayOfMonth, month, year, listener);
            }
            @Override
            public void onEmptyResult() {}
            @Override
            public void onError(String message) {}
        });

    }

    public void removeAppointment(String date, String hour, IUPDATEAppointment listener) {
        Map<String, Object> updates = new HashMap<>();
        updates.put(Utils.getStartAppointmentStartHourFormat(hour), FieldValue.delete());

        mFirebaseFirestore.collection(COLLECTION_APPOINTMENTS).document(date).update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    private void fetchAppointmentsByDate(Day dayOfWeek, int dayOfMonth, int month, int year, IGETSchedule listener) {
        String docId = dayOfMonth + "-" + (month+1) + "-" + year;
        DocumentReference docRef = mFirebaseFirestore.collection(COLLECTION_APPOINTMENTS).document(docId);

        String openHours = getBarbershopAvailableHoursByDay(dayOfWeek);

        String firstAppointmentHour = openHours.split("-")[0].trim().substring(0, 2).replace(":", "");
        String lastAppointmentHour = openHours.split("-")[1].trim().substring(0, 2).replace(":", "");

        int start = parseInt(firstAppointmentHour);
        int end = parseInt(lastAppointmentHour) - 1;

        HashMap<String, Appointment> appointmentMap = new HashMap<>();
        for (int i = start; i <= end; i++) {
            String appointmentHour = i + ":00" + "-" + (i+1) + ":00";
            Appointment emptyAppointment = new Appointment(AppointmentStatus.EMPTY.name(), appointmentHour);
            appointmentMap.put(String.valueOf(i), emptyAppointment);
        }

        docRef.get().addOnCompleteListener(task -> {
            ArrayList<Appointment> appointments = new ArrayList<>();

            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Map<String, Object> map = document.getData();
                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        Appointment appointment = new Appointment((HashMap<String, String>) entry.getValue());
                        String appointmentStartTime = appointment.getHour().split("-")[0].trim().substring(0, 2).replace(":", "");
                        appointmentMap.put(appointmentStartTime, appointment);
                    }
                }
            }

            for (Map.Entry<String, Appointment> item : appointmentMap.entrySet()) {
                appointments.add(item.getValue());
            }

            listener.onSuccess(appointments);
        });
    }

    private String getBarbershopAvailableHoursByDay(Day dayOfWeek) {
        switch (dayOfWeek) {
            case SUNDAY:
                return mBarbershopInfo.getSunday();
            case MONDAY:
                return mBarbershopInfo.getMonday();
            case TUESDAY:
                return mBarbershopInfo.getTuesday();
            case WEDNESDAY:
                return mBarbershopInfo.getWednesday();
            case THURSDAY:
                return mBarbershopInfo.getThursday();
        }
        return null;
    }
}
