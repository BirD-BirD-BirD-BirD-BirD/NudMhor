package com.example.nudmhor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Success extends AppCompatActivity {
    String uID;

    Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success);

        Intent intent = getIntent();
        uID = intent.getStringExtra("uID");

        back = findViewById(R.id.button_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent to_main = new Intent(Success.this, Main.class);
                to_main.putExtra("uID",uID);
                startActivity(to_main);
            }
        });
    }
}