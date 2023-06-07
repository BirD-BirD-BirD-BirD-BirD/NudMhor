package com.example.nudmhor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class UserInfo extends AppCompatActivity {
    EditText age,weight,height;
    Button next;
    String uID;
    String chosen_clinic;
    String clinic_treatment;
    String symptom_description;
    String chosen_doctor;
    String chosen_date;
    String chosen_queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info);

        age = findViewById(R.id.edittext_age);
        weight = findViewById(R.id.edittext_weight);
        height = findViewById(R.id.edittext_height);
        next = findViewById(R.id.button_next);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        uID = intent.getStringExtra("uID");
        chosen_clinic = intent.getStringExtra("chosen_clinic");
        clinic_treatment = intent.getStringExtra("clinic_treatment");
        symptom_description = intent.getStringExtra("symptom_description");
        chosen_doctor = intent.getStringExtra("chosen_doctor");
        chosen_date = intent.getStringExtra("chosen_date");
        chosen_queue = intent.getStringExtra("chosen_queue");


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String,String> data = new HashMap<>();
                data.put("user_id",uID);
                data.put("clinic_id",chosen_clinic);
                data.put("clinic_treatment",clinic_treatment);
                data.put("symptom_description",symptom_description);
                data.put("doctor_id",chosen_doctor);
                data.put("Date",chosen_date);
                data.put("Queue",chosen_queue);
                data.put("Age",age.getText().toString());
                data.put("Weight",weight.getText().toString());
                data.put("Height",height.getText().toString());
                db.collection("Appointment").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("UserInfo","appointment created!");

                        Intent to_success = new Intent(UserInfo.this,Success.class);
                        to_success.putExtra("uID",uID);
                        startActivity(to_success);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("UserInfo","appointment failed!");
                    }
                });
            }
        });

    }
}