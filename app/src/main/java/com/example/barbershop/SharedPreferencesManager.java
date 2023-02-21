package com.example.barbershop;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.barbershop.model.User;
import com.example.barbershop.model.UserType;


public class SharedPreferencesManager {

    private static final String DB_FILE = "DB_FILE";
    private static final String SP_FIREBASE_UID = "SP_FIREBASE_UID";
    private static final String SP_FULLNAME = "SP_FULLNAME";
    private static final String SP_USERTYPE = "SP_USERTYPE";
    private static final String SP_ACTIVE_APPOINTMENT = "SP_ACTIVE_APPOINTMENT";


    private static void removeValue(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(DB_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.apply();
    }

    private static void putString(Context context, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences(DB_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private static String getString(Context context, String key, String def) {
        SharedPreferences preferences = context.getSharedPreferences(DB_FILE, Context.MODE_PRIVATE);
        return preferences.getString(key, def);
    }

    private static void putInt(Context context, String key, int value) {
        SharedPreferences preferences = context.getSharedPreferences(DB_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    private static int getInt(Context context, String key, int def) {
        SharedPreferences preferences = context.getSharedPreferences(DB_FILE, Context.MODE_PRIVATE);
        return preferences.getInt(key, def);
    }

    private static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences preferences = context.getSharedPreferences(DB_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private static boolean getBoolean(Context context, String key, boolean def) {
        SharedPreferences preferences = context.getSharedPreferences(DB_FILE, Context.MODE_PRIVATE);
        return preferences.getBoolean(key, def);
    }

    // Used when user is logging out
    public static void clearUserData(Context context) {
        removeValue(context, SP_FIREBASE_UID);
        removeValue(context, SP_FULLNAME);
        removeValue(context, SP_USERTYPE);
        removeValue(context, SP_ACTIVE_APPOINTMENT);
    }

    public static void saveLoggedUser(Context context, User user) {
        putString(context, SP_FIREBASE_UID, user.getId());
        putString(context, SP_FULLNAME, user.getName());
        putInt(context, SP_USERTYPE, user.getType().ordinal());
    }

    public static void saveActiveAppointmentState(Context context, boolean hasActiveAppointment) {
        putBoolean(context, SP_ACTIVE_APPOINTMENT, hasActiveAppointment);
    }

    public static User getUser(Context context) {
        String id = getString(context, SP_FIREBASE_UID, "");
        String name = getString(context, SP_FULLNAME, "");
        UserType type = UserType.fromInteger(getInt(context, SP_USERTYPE, UserType.CLIENT.ordinal()));
        boolean hasActiveAppointment = getBoolean(context, SP_ACTIVE_APPOINTMENT, false);

        if (id.isEmpty() && type != UserType.ADMIN) return null;

        return new User(id, name, type, hasActiveAppointment);
    }

}
