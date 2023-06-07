package com.example.nudmhor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Account extends AppCompatActivity {

    TextView display_name, display_birthday;
    EditText editText_name,editText_sex,editText_id_number,editText_phone_number,editText_address;
    Button button_change_user_info,main,appointment_history;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account);

        Intent intent = getIntent();
        String uID = intent.getStringExtra("uID");

        db = FirebaseFirestore.getInstance();
        display_name = findViewById(R.id.textview_name);
        display_birthday = findViewById(R.id.textview_birthday);
        editText_name = findViewById(R.id.edittext_name);
        editText_sex = findViewById(R.id.edittext_sex);
        editText_id_number = findViewById(R.id.edittext_id_number);
        editText_phone_number = findViewById(R.id.edittext_phone_number);
        editText_address = findViewById(R.id.edittext_address);
        button_change_user_info = findViewById(R.id.button_change_account_info);
        main = findViewById(R.id.button_main);
        appointment_history = findViewById(R.id.button_appointment_history);

        db.collection("Users").document(uID).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot document = task.getResult();
                if(document.exists()){
                    String name = document.getString("name");
                    display_name.setText(name);

                    String birthday = document.getString("birthday");
                    display_birthday.setText(birthday);

                    editText_name.setText(name,TextView.BufferType.EDITABLE);
                    editText_sex.setText(document.getString("sex"),TextView.BufferType.EDITABLE);
                    editText_id_number.setText(document.getString("id_number"),TextView.BufferType.EDITABLE);
                    editText_phone_number.setText(document.getString("phone_number"),TextView.BufferType.EDITABLE);
                    editText_address.setText(document.getString("address"),TextView.BufferType.EDITABLE);
                    editText_sex.setText(document.getString("sex"),TextView.BufferType.EDITABLE);
                }else{
                    String name = "Default name";
                    display_name.setText(name);

                    String birthday = "Default birthday";
                    display_birthday.setText(birthday);
                    Log.d("Account","Failed to retrieve account data");
                }
            }
        });

        button_change_user_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("Users").document(uID).update("name",editText_name.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Account","name is updated!");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Account","name update is failed!");
                    }
                });

                db.collection("Users").document(uID).update("sex",editText_sex.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Account","sex is updated!");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Account","sex update is failed!");
                    }
                });

                db.collection("Users").document(uID).update("id_number",editText_id_number.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Account","id_number is updated!");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Account","id_number update is failed!");
                    }
                });

                db.collection("Users").document(uID).update("phone_number",editText_phone_number.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Account","phone_number is updated!");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Account","phone_number update is failed!");
                    }
                });

                db.collection("Users").document(uID).update("address",editText_address.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Account","address is updated!");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Account","address update is failed!");
                    }
                });

                finish();
                startActivity(getIntent());
            }
        });

        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent to_main = new Intent(Account.this,Main.class);
                to_main.putExtra("uID",uID);
                startActivity(to_main);
            }
        });

        appointment_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent to_appointment_history = new Intent(Account.this,Appointment_History.class);
                to_appointment_history.putExtra("uID",uID);
                startActivity(to_appointment_history);
            }
        });


    }
}