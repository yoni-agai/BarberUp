package com.example.barbershop;

import android.text.TextUtils;

import com.example.barbershop.model.Day;

import java.util.Calendar;

public class Utils {

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public final static boolean isValidPassword(CharSequence target) {
        return target.length() >= 8;
    }

    public final static Day getDayOfWeek(int dayOfMonth, int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        return Day.fromInteger(calendar.get(Calendar.DAY_OF_WEEK));
    }

    public static String getStartAppointmentStartHourFormat(String hours) {
        return hours.split("-")[0].trim().substring(0, 2).replace(":", "");
    }
}
