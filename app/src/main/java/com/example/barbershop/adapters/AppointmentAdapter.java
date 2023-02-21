package com.example.barbershop.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.barbershop.R;
import com.example.barbershop.interfaces.IOnScheduleItemClickedListener;
import com.example.barbershop.model.Appointment;
import com.example.barbershop.model.AppointmentStatus;
import com.example.barbershop.screens.main.ScheduleFragment;

import java.util.ArrayList;

public class AppointmentAdapter extends RecyclerView.Adapter {

    private ArrayList<Appointment> mDataSet;
    private boolean mIsAdmin;
    private IOnScheduleItemClickedListener mListener;

    public AppointmentAdapter(ArrayList<Appointment> dataset, boolean isAdmin, IOnScheduleItemClickedListener listener) {
        mDataSet = dataset;
        mIsAdmin = isAdmin;
        mListener = listener;
    }

    public void setData(ArrayList<Appointment> dataset) {
        mDataSet = dataset;
        notifyDataSetChanged();
    }

    public void notifyItemChangedAtPosition(int position) {
        notifyItemChanged(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.li_appointment, parent, false);
        AppointmentViewHolder holder = new AppointmentViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                if (mListener != null) {
                    mListener.onItemClicked(mDataSet.get(position), position);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((AppointmentViewHolder) holder).bind(mDataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public class AppointmentViewHolder extends RecyclerView.ViewHolder {
        private ImageView mCancelIv;
        private TextView mHour, mFullName;

        public AppointmentViewHolder(final View itemView) {
            super(itemView);
            mHour = (TextView) itemView.findViewById(R.id.liaHour);
            mFullName = (TextView) itemView.findViewById(R.id.liaFullName);
            mCancelIv = itemView.findViewById(R.id.liaCancel);
        }

        public void bind(Appointment appointment) {
            mHour.setText(appointment.getHour());

            if (appointment.getStatus().equals(AppointmentStatus.EMPTY.name())) {
                mFullName.setText("Available Appointment");
            } else {
                mFullName.setText(appointment.getCustomerName());
            }

            if (mIsAdmin && appointment.getStatus().equals(AppointmentStatus.BOOKED.name())) {
                mCancelIv.setVisibility(View.VISIBLE);
            } else if (mCancelIv.getVisibility() != View.GONE) {
                mCancelIv.setVisibility(View.GONE);
            }
        }
    }
}
