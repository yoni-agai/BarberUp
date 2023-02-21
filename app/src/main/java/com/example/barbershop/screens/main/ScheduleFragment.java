package com.example.barbershop.screens.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barbershop.BaseApplication;
import com.example.barbershop.R;
import com.example.barbershop.Utils;
import com.example.barbershop.adapters.AppointmentAdapter;
import com.example.barbershop.interfaces.IGETSchedule;
import com.example.barbershop.interfaces.IOnScheduleItemClickedListener;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ScheduleFragment extends Fragment implements IGETSchedule, IOnScheduleItemClickedListener {

    private UserRepo mUserRepo = BaseApplication.getUserRepo();
    private AppointmentRepo mAppRepo = BaseApplication.getAppointmentRepo();
    private UsersUpcomingAppointmentRepo mUsersUpcomingAppRepo = BaseApplication.getUsersUpcomingAppointmentRepo();

    private ArrayList<Appointment> mAppointments;

    private CalendarView mCalendarView;
    private FrameLayout mChosenDateLayout;
    private TextView mChosenDateTv;

    private RecyclerView mRecyclerView;
    private AppointmentAdapter mAdapter;

    private Calendar mCurrentlySelectedCalendar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mChosenDateTv.setText("Select a date");
    }

    private void initViews(View view) {
        mChosenDateLayout = view.findViewById(R.id.fsChosenDateLayout);
        mChosenDateTv = view.findViewById(R.id.fsChosenDateTv);
        mCalendarView = view.findViewById(R.id.fsCalendarView);
        mRecyclerView = view.findViewById(R.id.fsRecyclerView);
        initRecyclerView();

        mChosenDateLayout.setOnClickListener(view12 -> {
            mRecyclerView.setVisibility(View.GONE);
            mChosenDateLayout.setVisibility(View.GONE);
            mCalendarView.setVisibility(View.VISIBLE);
        });

        mCalendarView.setMinDate(new Date().getTime());
        mCalendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {

            mCurrentlySelectedCalendar = Calendar.getInstance();
            mCurrentlySelectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            mCurrentlySelectedCalendar.set(Calendar.MONTH, month);
            mCurrentlySelectedCalendar.set(Calendar.YEAR, year);

            Day dayOfWeek = Utils.getDayOfWeek(dayOfMonth, month, year);
            if (dayOfWeek == Day.CLOSED) {
                Toast.makeText(requireContext(), "Barbershop is closed on selected day.", Toast.LENGTH_LONG).show();
                return;
            }

            mCalendarView.setVisibility(View.GONE);
            mChosenDateLayout.setVisibility(View.VISIBLE);
            mChosenDateTv.setText(dayOfMonth+"-"+(month+1)+"-"+year);
            mAppRepo.getAppointmentsByDate(dayOfWeek, dayOfMonth, month, year, this);
        });
    }

    private void initRecyclerView() {
        mAdapter = new AppointmentAdapter(new ArrayList<Appointment>(), mUserRepo.getUser().isAdmin(),  this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onSuccess(ArrayList<Appointment> list) {
        mAppointments = list;
        mAdapter.setData(mAppointments);
        mRecyclerView.setVisibility(View.VISIBLE);
    }
    @Override
    public void onError(String error) {

    }

    @Override
    public void onItemClicked(Appointment appointment, int position) {

        if (mUserRepo.getUser().isAdmin() && appointment.getStatus().equals(AppointmentStatus.BOOKED.name())) {
            String date = mChosenDateTv.getText().toString();
            String hour = appointment.getHour();
            cancelAppointmentForUser(appointment.getUserId(), date, hour);
            return;
        }

        if (appointment.getStatus().equals(AppointmentStatus.BOOKED.name())) {
            Toast.makeText(requireContext(), "This appointment is booked already, try another time please.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mUserRepo.getUser().hasActiveAppointment()) {
            Toast.makeText(requireContext(), "You already have an appointment booked, you cannot book another one.", Toast.LENGTH_SHORT).show();
            return;
        }

        showDialog(appointment, position);
    }

    private void cancelAppointmentForUser(String userId, String date, String hour) {
        mAppRepo.removeAppointment(date, hour, new IUPDATEAppointment() {
            @Override
            public void onSuccess() {
                //Also remove next appointment after successfully deleting the appointment itself.
                NextAppointment nextAppointment = new NextAppointment(date, hour, AppointmentStatus.CANCELLED.name());
                mUsersUpcomingAppRepo.setNextAppointment(nextAppointment, userId, new IUPDATENextAppointment() {
                    @Override
                    public void onSuccess() {
                        // reload data after deletion.
                        int dayOfMonth = mCurrentlySelectedCalendar.get(Calendar.DAY_OF_MONTH);
                        int month = mCurrentlySelectedCalendar.get(Calendar.MONTH);
                        int year = mCurrentlySelectedCalendar.get(Calendar.YEAR);
                        Day dayOfWeek = Utils.getDayOfWeek(dayOfMonth, month, year);
                        mAppRepo.getAppointmentsByDate(dayOfWeek, dayOfMonth, month, year, ScheduleFragment.this);
                    }
                    @Override
                    public void onError(String error) {}
                });
            }
            @Override
            public void onError(String error) {}
        });
    }

    private void showDialog(Appointment appointment, int position) {
        String date = mChosenDateTv.getText().toString();
        String hour = appointment.getHour();
        String message = String.format(getString(R.string.schedule_new_app_dialog_msg), date, hour);
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.schedule_new_app_dialog_title))
                .setMessage(message)
                .setPositiveButton(getString(R.string.schedule_new_app_dialog_positive), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        bookAppointment(date, hour, position);
                    }
                })
                .setNegativeButton(getString(R.string.schedule_new_app_dialog_negative), null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(android.R.color.holo_red_light));
            }
        });

        dialog.show();
    }

    private void bookAppointment(String date, String hour, int position) {
        NextAppointment nextAppointment = new NextAppointment(date, hour, AppointmentStatus.BOOKED.name());
        mUsersUpcomingAppRepo.setNextAppointment(nextAppointment, null, new IUPDATENextAppointment() {
            @Override
            public void onSuccess() {
                mAppRepo.setAppointment(date, hour, AppointmentStatus.BOOKED.name(), new IUPDATEAppointment() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(requireContext(), "Appointment Booked Successfully!", Toast.LENGTH_LONG).show();

                        User user = mUserRepo.getUser();
                        Appointment appointment = mAppointments.get(position);
                        appointment.setHour(hour);
                        appointment.setCustomerName(user.getName());
                        appointment.setStatus(AppointmentStatus.BOOKED.name());
                        appointment.setUserId(user.getId());
                        mAdapter.notifyItemChangedAtPosition(position);
                    }
                    @Override
                    public void onError(String error) {
                        Toast.makeText(requireContext(), "Booking appointment failed. please try again later.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onError(String error) {
                Toast.makeText(requireContext(), "Booking appointment failed. please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
