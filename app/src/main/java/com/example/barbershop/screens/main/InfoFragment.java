package com.example.barbershop.screens.main;


import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.barbershop.BaseApplication;
import com.example.barbershop.R;
import com.example.barbershop.SharedPreferencesManager;
import com.example.barbershop.interfaces.IGETBarbershopInfo;
import com.example.barbershop.interfaces.IUPDATEBarbershopInfo;
import com.example.barbershop.model.BarbershopInfo;
import com.example.barbershop.model.Day;
import com.example.barbershop.model.Schedule;
import com.example.barbershop.model.Workday;
import com.example.barbershop.repositories.BarbershopInfoRepo;
import com.google.android.material.slider.RangeSlider;

public class InfoFragment extends Fragment implements IGETBarbershopInfo {

    private BarbershopInfoRepo mBarberInfoRepo = BaseApplication.getBarbershopInfoRepo();
    private boolean isAdminUser;

    private Schedule mSchedule;
    private Schedule mUpdatedSchedule;
    private int mScheduleChanges = 0;

    private TextView mPhoneNumber, mAddress, mSunday, mMonday,
            mTuesday, mWednesday, mThursday, mEditScheduleNote;

    private Button mUpdateButton;

    private RangeSlider mSundaySlider, mMondaySlider, mTuesdaySlider, mWednesdaySlider, mThursdaySlider;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle saveInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        initViews(view);

        isAdminUser = SharedPreferencesManager.getUser(requireContext()).isAdmin();

        if (isAdminUser) {
            mEditScheduleNote.setVisibility(View.VISIBLE);
            setButtonsListeners();
        } else {
            mUpdateButton.setVisibility(View.GONE);
            mSundaySlider.setEnabled(false);
            mMondaySlider.setEnabled(false);
            mTuesdaySlider.setEnabled(false);
            mWednesdaySlider.setEnabled(false);
            mThursdaySlider.setEnabled(false);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getBarbershopInfo();
    }

    private void initViews(View view) {
        mPhoneNumber = view.findViewById(R.id.fiPhoneNumber);
        mAddress = view.findViewById(R.id.fiAddress);
        mSunday = view.findViewById(R.id.fiSunday);
        mMonday = view.findViewById(R.id.fiMonday);
        mTuesday = view.findViewById(R.id.fiTuesday);
        mWednesday = view.findViewById(R.id.fiWednesday);
        mThursday = view.findViewById(R.id.fiThursday);

        mSundaySlider = view.findViewById(R.id.fiSundaySlider);
        mMondaySlider = view.findViewById(R.id.fiMondaySlider);
        mTuesdaySlider = view.findViewById(R.id.fiTuesdaySlider);
        mWednesdaySlider = view.findViewById(R.id.fiWednesdaySlider);
        mThursdaySlider = view.findViewById(R.id.fiThursdaySlider);

        mEditScheduleNote = view.findViewById(R.id.fiEditScheduleNote);
        mUpdateButton = view.findViewById(R.id.fiUpdateButton);
    }


    /** Screen button listeners*/
    private void setButtonsListeners() {

        mUpdateButton.setVisibility(View.VISIBLE);
        mSundaySlider.setEnabled(true);
        mMondaySlider.setEnabled(true);
        mTuesdaySlider.setEnabled(true);
        mWednesdaySlider.setEnabled(true);
        mThursdaySlider.setEnabled(true);


        mPhoneNumber.setOnClickListener(view -> {
            editTextView(mPhoneNumber);
        });

        mAddress.setOnClickListener(view -> {
            editTextView(mAddress);
        });

        mUpdateButton.setOnClickListener(view -> {
            mBarberInfoRepo.updateBarbershopInfo(mPhoneNumber.getText().toString(), mAddress.getText().toString(), mUpdatedSchedule, new IUPDATEBarbershopInfo() {
                @Override
                public void onSuccess() {

                }
                @Override
                public void onError(String message) {

                }
            });
        });

        mSundaySlider.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull RangeSlider rangeSlider) {}
            @Override
            public void onStopTrackingTouch(@NonNull RangeSlider rangeSlider) {
                newRangeSliderInput(Day.SUNDAY, rangeSlider);
            }
        });
        mMondaySlider.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull RangeSlider rangeSlider) {}
            @Override
            public void onStopTrackingTouch(@NonNull RangeSlider rangeSlider) {
                newRangeSliderInput(Day.MONDAY, rangeSlider);
            }
        });
        mTuesdaySlider.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull RangeSlider rangeSlider) {}
            @Override
            public void onStopTrackingTouch(@NonNull RangeSlider rangeSlider) {
                newRangeSliderInput(Day.TUESDAY, rangeSlider);
            }
        });
        mWednesdaySlider.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull RangeSlider rangeSlider) {}
            @Override
            public void onStopTrackingTouch(@NonNull RangeSlider rangeSlider) {
                newRangeSliderInput(Day.WEDNESDAY, rangeSlider);
            }
        });
        mThursdaySlider.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull RangeSlider rangeSlider) {}
            @Override
            public void onStopTrackingTouch(@NonNull RangeSlider rangeSlider) {
                newRangeSliderInput(Day.THURSDAY, rangeSlider);
            }
        });
        }

    private void editTextView (TextView editText) {
        editText.setCursorVisible(true);
        editText.setFocusableInTouchMode(true);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.requestFocus();

    }

    private void newRangeSliderInput(Day day, RangeSlider rangeSlider) {
        String startTime = rangeSlider.getValues().get(0).intValue() + ":00";
        String endTime = rangeSlider.getValues().get(1).intValue() + ":00";
        updateNewDayInput(day, startTime, endTime);
    }

    private void updateNewDayInput(Day day, String startTime, String endTime) {
        Workday workday = mUpdatedSchedule.getWorkday(day);
        workday.setStartTime(startTime);
        workday.setEndTime(endTime);
    }

    private void initBarbershopInfoData(BarbershopInfo barbershopInfo) {
        String phoneText = String.format(requireContext().getString(R.string.barbershop_info_phone_with_prefix), barbershopInfo.getPhoneNumber());
        mPhoneNumber.setText(phoneText);

        String addressText = String.format(requireContext().getString(R.string.barbershop_info_address_with_prefix), barbershopInfo.getAddress());
        mAddress.setText(addressText);

        String sundayText = String.format(requireContext().getString(R.string.barbershop_info_sunday_with_prefix), barbershopInfo.getSunday());
        mSunday.setText(sundayText);
        Workday sunday = new Workday(barbershopInfo.getSunday());
        mSundaySlider.setValues(getSliderValue(sunday.getStartTime()), getSliderValue(sunday.getEndTime()));

        String mondayText = String.format(requireContext().getString(R.string.barbershop_info_monday_with_prefix), barbershopInfo.getMonday());
        mMonday.setText(mondayText);
        Workday monday = new Workday(barbershopInfo.getMonday());
        mMondaySlider.setValues(getSliderValue(monday.getStartTime()), getSliderValue(monday.getEndTime()));

        String tuesdayText = String.format(requireContext().getString(R.string.barbershop_info_tuesday_with_prefix), barbershopInfo.getTuesday());
        mTuesday.setText(tuesdayText);
        Workday tuesday = new Workday(barbershopInfo.getTuesday());
        mTuesdaySlider.setValues(getSliderValue(tuesday.getStartTime()), getSliderValue(tuesday.getEndTime()));

        String wednesdayText = String.format(requireContext().getString(R.string.barbershop_info_wednesday_with_prefix), barbershopInfo.getWednesday());
        mWednesday.setText(wednesdayText);
        Workday wednesday = new Workday(barbershopInfo.getWednesday());
        mWednesdaySlider.setValues(getSliderValue(wednesday.getStartTime()), getSliderValue(wednesday.getEndTime()));

        String thursdayText = String.format(requireContext().getString(R.string.barbershop_info_thursday_with_prefix), barbershopInfo.getThursday());
        mThursday.setText(thursdayText);
        Workday thursday = new Workday(barbershopInfo.getThursday());
        mThursdaySlider.setValues(getSliderValue(thursday.getStartTime()), getSliderValue(thursday.getEndTime()));

        mSchedule = new Schedule(sunday, monday, tuesday, wednesday, thursday);
        mUpdatedSchedule = new Schedule(sunday, monday, tuesday, wednesday, thursday);
    }

    private float getSliderValue(String time) {
        return Float.parseFloat(time.replace(":00", ""));
    }

    private void getBarbershopInfo() {
        mBarberInfoRepo.getBarbershopInfo(this);
    }

    /**
     * BarbershopInfo server call callbacks:
     * onSuccess/onEmptyResult/onError
     */
    @Override
    public void onSuccess(BarbershopInfo barbershopInfo) {
        initBarbershopInfoData(barbershopInfo);
    }

    @Override
    public void onEmptyResult() {
        Toast.makeText(requireContext(), "Our search has returned empty handed.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

}
