package com.example.nudmhor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    Button register;
    EditText email, password, name, sex, birthday, id_number, phone_number, address;
    FirebaseFirestore firestore;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        email = findViewById(R.id.edittext_email);
        password = findViewById(R.id.edittext_password);
        name = findViewById(R.id.edittext_name);
        sex = findViewById(R.id.edittext_sex);
        birthday = findViewById(R.id.edittext_birthday);
        id_number = findViewById(R.id.edittext_id_number);
        phone_number = findViewById(R.id.edittext_phone_number);
        address = findViewById(R.id.edittext_address);
        register = findViewById(R.id.button_confirm_register);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(email.getText().toString()) ||
                        TextUtils.isEmpty(password.getText().toString()) ||
                        TextUtils.isEmpty(name.getText().toString()) ||
                        TextUtils.isEmpty(sex.getText().toString()) ||
                        TextUtils.isEmpty(birthday.getText().toString()) ||
                        TextUtils.isEmpty(id_number.getText().toString()) ||
                        TextUtils.isEmpty(phone_number.getText().toString()) ||
                        TextUtils.isEmpty(address.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Please fill all info.", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("Register", "createUserWithEmail:success");
                                        String userId = mAuth.getCurrentUser().getUid();

                                        Map<String,Object> user_info = new HashMap<>();
                                        user_info.put("name",name.getText().toString());
                                        user_info.put("sex",sex.getText().toString());
                                        user_info.put("birthday",birthday.getText().toString());
                                        user_info.put("id_number",id_number.getText().toString());
                                        user_info.put("phone_number",phone_number.getText().toString());
                                        user_info.put("address",address.getText().toString());

                                        firestore.collection("Users")
                                                .document(userId)
                                                .set(user_info)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        Log.d("Register", "DocumentSnapshot successfully written!");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.d("Register","Error writing document");
                                                    }
                                                });

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.d("Register", "createUserWithEmail:failure");

                                    }
                                }
                            });

                    Intent to_login = new Intent(Register.this,Login.class);
                    startActivity(to_login);
                }
            }
        });
    }
}