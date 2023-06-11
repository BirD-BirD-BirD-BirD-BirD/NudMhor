package com.example.nudmhor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Time extends AppCompatActivity {
    TextView textView_name,account_edit;
    FirebaseFirestore db;
    String uID;
    String chosen_clinic;
    String clinic_treatment;
    String symptom_description;
    String chosen_doctor;
    String chosen_date;
    String chosen_queue;

    Spinner queue;
    Button next,appointment_history,account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time);

        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        uID = intent.getStringExtra("uID");
        chosen_clinic = intent.getStringExtra("chosen_clinic");
        clinic_treatment = intent.getStringExtra("clinic_treatment");
        symptom_description = intent.getStringExtra("symptom_description");
        chosen_doctor = intent.getStringExtra("doctorID");
        chosen_date = intent.getStringExtra("chosen_date");

        Log.d("Time",chosen_doctor);
        queue = findViewById(R.id.spinner_time);
        next = findViewById(R.id.button_next);
        appointment_history = findViewById(R.id.button_appointment_history);
        account = findViewById(R.id.button_account);
        account_edit = findViewById(R.id.account_edit);
        textView_name = findViewById(R.id.textview_name);

        db.collection("Users").document(uID).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot document = task.getResult();
                if(document.exists()){
                    String name = document.getString("name");
                    textView_name.setText(name);
                }
            }else{
                String name = "Dafault Name";
                textView_name.setText(name);
                Log.d("Main","Error can't get data from user");
            }
        });

        db.collection("Doctor").document(chosen_doctor).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot document = task.getResult();

                List<String> doctor_queue = (List<String>) document.get("Queue");
                List<String> availableQueue = available_queue(doctor_queue,chosen_date);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(Time.this, android.R.layout.simple_spinner_dropdown_item, availableQueue);
                queue.setAdapter(adapter);
            }else {
                Log.d("Time", "Error can't get data from doctor");
            }
        });

        queue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                chosen_queue = adapterView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                chosen_queue = null;
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent to_user_info = new Intent(Time.this,UserInfo.class);
                to_user_info.putExtra("uID",uID);
                to_user_info.putExtra("chosen_clinic",chosen_clinic);
                to_user_info.putExtra("clinic_treatment",clinic_treatment);
                to_user_info.putExtra("symptom_description",symptom_description);
                to_user_info.putExtra("chosen_doctor",chosen_doctor);
                to_user_info.putExtra("chosen_date",chosen_date);
                to_user_info.putExtra("chosen_queue",chosen_queue);

                startActivity(to_user_info);
            }
        });

        appointment_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent to_appointment_history = new Intent(Time.this,Appointment_History.class);
                to_appointment_history.putExtra("uID",uID);
                startActivity(to_appointment_history);
            }
        });

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent to_Account = new Intent(Time.this,Account.class);
                to_Account.putExtra("uID",uID);
                startActivity(to_Account);
            }
        });

        account_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent to_Account = new Intent(Time.this,Account.class);
                to_Account.putExtra("uID",uID);
                startActivity(to_Account);
            }
        });
    }

    private List<String> available_queue(List<String> doctor_queue, String chosen_date){
        List<String> new_queue = doctor_queue;
        db.collection("Appointment").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                for(QueryDocumentSnapshot document : task.getResult()){
                    if(document.getString("Date").equals(chosen_date) && doctor_queue.contains(document.getString("Queue"))){
                        new_queue.remove(document.getString("Queue"));
                    }
                }
            }
        });
        return new_queue;
    }
}