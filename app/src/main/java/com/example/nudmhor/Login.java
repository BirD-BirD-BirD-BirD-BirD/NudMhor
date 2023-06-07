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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    Button login,register;
    EditText email,password;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        email = findViewById(R.id.edittext_email);
        password = findViewById(R.id.edittext_password);
        login = findViewById(R.id.button_login);
        register = findViewById(R.id.button_register);

        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("Login", "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if(user != null){
                                        String uID = user.getUid();
                                        Log.d("Login",uID);
                                        Intent to_main = new Intent(Login.this, Main.class);
                                        to_main.putExtra("uID",uID);
                                        startActivity(to_main);
                                    }
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.d("Login", "createUserWithEmail:failure");


                                }
                            }
                        });
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent to_register = new Intent(Login.this,Register.class);
                startActivity(to_register);
            }
        });

    }
}