package com.example.barbershop.screens.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barbershop.BaseApplication;
import com.example.barbershop.R;
import com.example.barbershop.Utils;
import com.example.barbershop.adapters.AppointmentAdapter;
import com.example.barbershop.interfaces.IGETClientNextAppointment;
import com.example.barbershop.interfaces.IGETSchedule;
import com.example.barbershop.interfaces.IUPDATEAppointment;
import com.example.barbershop.interfaces.IUPDATENextAppointment;
import com.example.barbershop.model.Appointment;
import com.example.barbershop.model.AppointmentStatus;
import com.example.barbershop.model.Day;
import com.example.barbershop.model.NextAppointment;
import com.example.barbershop.model.User;
import com.example.barbershop.repositories.AppointmentRepo;
import com.example.barbershop.repositories.UserRepo;
import com.example.barbershop.repositories.UsersUpcomingAppointmentRepo;
import com.example.barbershop.screens.loginSignup.LoginActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class HomeFragment extends Fragment {

    private UserRepo mUserRepo = BaseApplication.getUserRepo();
    private UsersUpcomingAppointmentRepo mUpcomingAppointmentRepo = BaseApplication.getUsersUpcomingAppointmentRepo();
    private AppointmentRepo mAppointmentRepo = BaseApplication.getAppointmentRepo();

    private User mUser = mUserRepo.getUser();

    private NextAppointment mNextAppointment = null;

    private ImageView mLogout, mClientNextAppCancelBtn;
    private TextView mWelcomeTv, mClientNextAppHourTv, mClientNextAppNameTv, mClientSetAppBtn;
    private ViewGroup mClientNextAppLayout, mAdminDailyPlanLayout;

    private RecyclerView mRecyclerView;
    private AppointmentAdapter mAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        switch (mUser.getType()) {
            case CLIENT:
                mUpcomingAppointmentRepo.getClientNextAppointment(new IGETClientNextAppointment() {
                    @Override
                    public void onSuccess(NextAppointment nextAppointment) {
                        initClientData(nextAppointment);
                    }
                    @Override
                    public void onError(String message) {}
                });
                break;
            case ADMIN:
                Calendar calendar = Calendar.getInstance();
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                Day dayOfWeek = Utils.getDayOfWeek(dayOfMonth, month, year);
                mAppointmentRepo.getAppointmentsByDate(dayOfWeek, dayOfMonth, month, year, new IGETSchedule() {
                    @Override
                    public void onSuccess(ArrayList<Appointment> list) {
                        mAdapter.setData(list);
                    }
                    @Override
                    public void onError(String error) {}
                });
                break;
        }
    }

    private void initViews(View view) {

        mWelcomeTv = view.findViewById(R.id.fhWelcomeTv);
        mWelcomeTv.setText(generateWelcomeMessage());

        mLogout = (ImageView) view.findViewById(R.id.fhLogoutBtn);
        mLogout.setOnClickListener(v -> {
            mUserRepo.signOut();
            navigateToLogin();
        });

        switch (mUser.getType()) {
            case CLIENT:
                initClientViewState(view);
                break;
            case ADMIN:
                initAdminViewState(view);
                break;
        }
    }

    private String generateWelcomeMessage() {
        switch (mUser.getType()) {
            case ADMIN:
                return "Welcome Boss!";
            case CLIENT:
                return "Welcome " + mUser.getName();
            default:
                return "Unsupported User?!";
        }
    }

    private void initAdminViewState(View view) {
        mAdminDailyPlanLayout = view.findViewById(R.id.fhAdminDailyPlanLayout);
        mRecyclerView = view.findViewById(R.id.fhRecyclerView);

        mAdminDailyPlanLayout.setVisibility(View.VISIBLE);
        initRecyclerView();
    }

    private void initRecyclerView() {
        mAdapter = new AppointmentAdapter(new ArrayList<Appointment>(), mUser.isAdmin(), null);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initClientViewState(View view) {
        mClientNextAppLayout = view.findViewById(R.id.fhClientNextAppointmentLayout);
        mClientSetAppBtn = view.findViewById(R.id.fhClientSetAppointment);
        mClientNextAppHourTv = view.findViewById(R.id.liaHour);
        mClientNextAppNameTv = view.findViewById(R.id.liaFullName);
        mClientNextAppCancelBtn = view.findViewById(R.id.liaCancel);

        mClientSetAppBtn.setOnClickListener(view1 -> {
            MainActivity activity = ((MainActivity) getActivity());
            if (activity != null) {
                activity.navigateToScheduleTab();
            }
        });

        mClientNextAppCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNextAppointment.setStatus(AppointmentStatus.CANCELLED.name());
                mUpcomingAppointmentRepo.setNextAppointment(mNextAppointment, null, new IUPDATENextAppointment() {
                    @Override
                    public void onSuccess() {
                        initClientData(mNextAppointment);
                        mAppointmentRepo.removeAppointment(mNextAppointment.getDate(), mNextAppointment.getHour(), new IUPDATEAppointment() {
                            @Override
                            public void onSuccess() {
                                // should we do something here?!
                            }
                            @Override
                            public void onError(String error) {}
                        });
                    }
                    @Override
                    public void onError(String error) {}
                });
            }
        });
    }

    private void initClientData(NextAppointment appointment) {

        if (appointment == null || !appointment.getStatus().equals(AppointmentStatus.BOOKED.name())) {
            mClientSetAppBtn.setVisibility(View.VISIBLE);
            mClientNextAppLayout.setVisibility(View.GONE);
            return;
        }

        mNextAppointment = appointment;

        mClientSetAppBtn.setVisibility(View.GONE);
        mClientNextAppLayout.setVisibility(View.VISIBLE);
        mClientNextAppHourTv.setText(appointment.getHour());
        mClientNextAppNameTv.setText(mUser.getName());

        mClientNextAppCancelBtn.setVisibility(View.VISIBLE);
    }

    private void navigateToLogin() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }
}
