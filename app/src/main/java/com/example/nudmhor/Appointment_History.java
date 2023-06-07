package com.example.nudmhor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Appointment_History extends AppCompatActivity {

    String uID;
    Adapter adapter;
    Button main,account;
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.appointment_history);

        Intent intent = getIntent();
        uID = intent.getStringExtra("uID");

        //Log.d("Appointment_History",uID);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Query query = db.collection("Appointment").whereEqualTo("user_id",uID);

        FirestoreRecyclerOptions<Appointment> options = new FirestoreRecyclerOptions.Builder<Appointment>()
                .setQuery(query, Appointment.class)
                .build();

        adapter = new Adapter(options);

        RecyclerView recyclerView = findViewById(R.id.appointment_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        main = findViewById(R.id.button_main);
        account = findViewById(R.id.button_account);

        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent to_Account = new Intent(Appointment_History.this,Main.class);
                to_Account.putExtra("uID",uID);
                startActivity(to_Account);
            }
        });

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent to_Account = new Intent(Appointment_History.this,Account.class);
                to_Account.putExtra("uID",uID);
                startActivity(to_Account);
            }
        });
    }

    private void setUpRecyclerView(String uID){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Query query = db.collection("Appointment").whereEqualTo("user_id",uID);

        FirestoreRecyclerOptions<Appointment> options = new FirestoreRecyclerOptions.Builder<Appointment>()
                .setQuery(query, Appointment.class)
                .build();

        adapter = new Adapter(options);

        RecyclerView recyclerView = findViewById(R.id.appointment_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}