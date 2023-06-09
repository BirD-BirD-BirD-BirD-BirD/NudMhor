package com.example.nudmhor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Date extends AppCompatActivity {
    TextView textView_name,account_edit;
    EditText date;
    Spinner doctor;

    Button next,appointment_history,account;

    FirebaseFirestore db;
    List<String> doctor_id = new ArrayList<>();

    String appointment_date;
    String chosen_doctor;

    String uID;
    String chosen_clinic;
    String clinic_treatment;
    String symptom_description;
    String doctorID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date);

        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        uID = intent.getStringExtra("uID");
        chosen_clinic = intent.getStringExtra("chosen_clinic");
        clinic_treatment = intent.getStringExtra("clinic_treatment");
        symptom_description = intent.getStringExtra("symptom_description");

        Log.d("Date",uID);
        Log.d("Date",chosen_clinic);
        Log.d("Date",symptom_description);

        textView_name = findViewById(R.id.textview_name);
        account_edit = findViewById(R.id.account_edit);
        doctor = findViewById(R.id.spinner_doctor);
        date = findViewById(R.id.edittext_date);
        next = findViewById(R.id.button_next);
        appointment_history = findViewById(R.id.button_appointment_history);
        account = findViewById(R.id.button_account);


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

        db.collection("Clinic").document(chosen_clinic).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    doctor_id = (ArrayList<String>) document.get("doctor");
                    Log.d("Date", Arrays.toString(doctor_id.toArray()));

                    List<String> doctor_name = new ArrayList<String>();

                    for (String s : doctor_id) {
                        db.collection("Doctor").document(s).get().addOnCompleteListener(doctorTask -> {
                            if (doctorTask.isSuccessful()) {
                                DocumentSnapshot doctorDocument = doctorTask.getResult();
                                doctor_name.add(doctorDocument.getString("name"));

                                ArrayAdapter<String> adapter = new ArrayAdapter<>(Date.this, android.R.layout.simple_spinner_dropdown_item, doctor_name);
                                doctor.setAdapter(adapter);
                            } else {
                                Log.d("Date", "Error can't get data from doctor");
                            }
                        });
                    }
                } else {
                    Log.d("Date", "Failed to get doctor info.");
                }
            }
        });

        doctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                chosen_doctor = adapterView.getItemAtPosition(position).toString();
                Log.d("Date", chosen_doctor);

                db.collection("Doctor")
                        .whereEqualTo("name", chosen_doctor)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    doctorID = document.getId();
                                    Log.d("Date", "Doctor ID: " + doctorID);

                                    // Perform further actions with the doctorID if needed
                                }
                            } else {
                                Log.d("Date", "Error getting doctor document: ", task.getException());
                            }
                        });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                chosen_doctor = null; // Set to null or provide a default value
            }
        });



        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        Date.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                appointment_date=dayOfMonth + "." + (monthOfYear + 1) + "." + year;
                                date.setText(appointment_date);
                            }
                        },
                        year,
                        month,
                        day
                );

                datePickerDialog.show();


            }


        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent to_Time = new Intent(Date.this,Time.class);
                to_Time.putExtra("uID",uID);
                to_Time.putExtra("chosen_clinic",chosen_clinic);
                to_Time.putExtra("clinic_treatment",clinic_treatment);
                to_Time.putExtra("symptom_description",symptom_description);
                to_Time.putExtra("chosen_date",appointment_date);
                to_Time.putExtra("doctorID",doctorID);

                Log.d("Date",uID);
                Log.d("Date",chosen_clinic);
                Log.d("Date",symptom_description);
                Log.d("Date",chosen_doctor);
                Log.d("Date",appointment_date);

                startActivity(to_Time);
            }
        });

        appointment_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent to_appointment_history = new Intent(Date.this,Appointment_History.class);
                to_appointment_history.putExtra("uID",uID);
                startActivity(to_appointment_history);
            }
        });

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent to_Account = new Intent(Date.this,Account.class);
                to_Account.putExtra("uID",uID);
                startActivity(to_Account);
            }
        });

        account_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent to_Account = new Intent(Date.this,Account.class);
                to_Account.putExtra("uID",uID);
                startActivity(to_Account);
            }
        });

    }
}