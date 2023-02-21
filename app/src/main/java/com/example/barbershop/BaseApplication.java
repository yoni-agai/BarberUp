package com.example.barbershop;

import android.app.Application;
import android.content.Context;

import com.example.barbershop.repositories.AppointmentRepo;
import com.example.barbershop.repositories.BarbershopInfoRepo;
import com.example.barbershop.repositories.UserRepo;
import com.example.barbershop.repositories.UsersUpcomingAppointmentRepo;

public class BaseApplication extends Application {
    private static BaseApplication singleton;

    private static UserRepo mUserRepo;
    private static BarbershopInfoRepo mBarbershopInfoRepo;
    private static UsersUpcomingAppointmentRepo mUsersUpcomingAppointmentRepo;
    private static AppointmentRepo mAppointmentRepo;
    private static DatabaseManager mDatabaseManager;

    public static BaseApplication getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;

        initDatabaseManager(this);

        initUserRepo(this);
        initUsersUpcomingAppointmentRepo(this, getUserRepo());
        initBarbershopInfoRepo();
        initAppointmentRepo(getBarbershopInfoRepo(), getUserRepo());
    }

    private void initUsersUpcomingAppointmentRepo(Context context, UserRepo userRepo) {
        mUsersUpcomingAppointmentRepo = new UsersUpcomingAppointmentRepo(context, userRepo);
    }

    private void initUserRepo(Context context) {
        mUserRepo = new UserRepo(context);
    }

    private void initBarbershopInfoRepo() {
        mBarbershopInfoRepo = new BarbershopInfoRepo();
    }

    private void initAppointmentRepo(BarbershopInfoRepo barbershopInfoRepo, UserRepo userRepo) {
        mAppointmentRepo = new AppointmentRepo(barbershopInfoRepo, userRepo);
    }

    public static UserRepo getUserRepo() {
        return mUserRepo;
    }

    public static UsersUpcomingAppointmentRepo getUsersUpcomingAppointmentRepo() {
        return mUsersUpcomingAppointmentRepo;
    }

    public static BarbershopInfoRepo getBarbershopInfoRepo() {
        return mBarbershopInfoRepo;
    }

    public static AppointmentRepo getAppointmentRepo() {
        return mAppointmentRepo;
    }

    private void initDatabaseManager(Context context) {
        //TODO - init this code
        //mUserRepo = new UserRepo(context);
    }

    public static DatabaseManager getDatabaseManager() {
        return mDatabaseManager;
    }

}
