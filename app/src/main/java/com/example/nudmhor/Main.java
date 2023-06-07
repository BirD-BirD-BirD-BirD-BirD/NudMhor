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
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main extends AppCompatActivity {

    TextView display_name,account_edit;
    Button next,appointment_history,account;
    Spinner clinic;
    EditText symptom;

    FirebaseFirestore db;

    String chosen_clinic;
    String clinic_treatment;
    String symptom_description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Intent intent = getIntent();
        String uID = intent.getStringExtra("uID");

        db = FirebaseFirestore.getInstance();

        display_name = findViewById(R.id.textview_name);
        next = findViewById(R.id.button_next);
        appointment_history = findViewById(R.id.button_appointment_history);
        account = findViewById(R.id.button_account);
        clinic = findViewById(R.id.spinner_clinic);
        symptom = findViewById(R.id.edittext_symptom);
        account_edit = findViewById(R.id.account_edit);

        db.collection("Users").document(uID).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot document = task.getResult();
                if(document.exists()){
                    String name = document.getString("name");
                    display_name.setText(name);
                }
            }else{
                String name = "Dafault Name";
                display_name.setText(name);
                Log.d("Main","Error can't get data from user");
            }
        });

        HashMap<String,String> clinic_id = new HashMap<String,String>();
        db.collection("Clinic").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                List<String> clinic_list = new ArrayList<>();

                String option = "";

                for(QueryDocumentSnapshot document : task.getResult()){
                    option = option.concat(document.getString("name"))
                            .concat("(")
                            .concat(document.getString("treatment"))
                            .concat(")");
                    clinic_list.add(option);
                    clinic_treatment = option;
                    clinic_id.put(option,document.getId());
                }
                for(String i : clinic_list){
                    Log.d("Main",i);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Main.this, android.R.layout.simple_spinner_dropdown_item,clinic_list);
                clinic.setAdapter(adapter);
            }else{
                Log.d("Main","Error can't get data from Clinic");
            }
        });



        clinic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                chosen_clinic = clinic_id.get(adapterView.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Handle case when nothing is selected
                chosen_clinic = null; // Set to null or provide a default value
            }
        });

        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                symptom_description = symptom.getText().toString();
                Intent to_Date = new Intent(Main.this,Date.class);
                to_Date.putExtra("uID",uID);
                to_Date.putExtra("chosen_clinic",chosen_clinic);
                to_Date.putExtra("clinic_treatment",clinic_treatment);
                to_Date.putExtra("symptom_description",symptom_description);
                Log.d("Main",chosen_clinic);
                Log.d("Main",symptom_description);
                startActivity(to_Date);
            }
        });

        account_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent to_Account = new Intent(Main.this,Account.class);
                to_Account.putExtra("uID",uID);
                startActivity(to_Account);
            }
        });

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent to_Account = new Intent(Main.this,Account.class);
                to_Account.putExtra("uID",uID);
                startActivity(to_Account);
            }
        });

        appointment_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent to_appointment_history = new Intent(Main.this,Appointment_History.class);
                to_appointment_history.putExtra("uID",uID);
                startActivity(to_appointment_history);
            }
        });


    }
}