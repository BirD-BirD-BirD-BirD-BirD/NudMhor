package com.example.nudmhor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class Adapter extends FirestoreRecyclerAdapter<Appointment,Adapter.ViewHolder> {

    public Adapter(@NonNull FirestoreRecyclerOptions<Appointment> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Appointment model) {
        holder.clinic_name.setText(model.getClinic_treatment());
        holder.time.setText(model.getDate_Queue());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_entry,parent,false);
        return new ViewHolder(v);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView clinic_name,time;

        public ViewHolder(View view){
            super(view);
            clinic_name = view.findViewById(R.id.clinic_name);
            time = view.findViewById(R.id.time);
        }
    }
}
