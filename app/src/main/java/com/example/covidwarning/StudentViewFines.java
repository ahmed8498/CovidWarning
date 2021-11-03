package com.example.covidwarning;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.covidwarning.Callbacks.GetFinesCallback;
import com.example.covidwarning.Models.Adapters.ViewFineRecyclerViewAdapter;
import com.example.covidwarning.Models.Fine;
import com.example.covidwarning.Network.FirebaseNetworking;

import java.util.ArrayList;

public class StudentViewFines extends AppCompatActivity implements GetFinesCallback {

    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_view_fines);
        recyclerView = findViewById(R.id.studentViewFinesRecyclerView);
        FirebaseNetworking firebaseNetworking = new FirebaseNetworking();

        SharedPreferences prefs = getSharedPreferences("Login",MODE_PRIVATE);
        String email = prefs.getString("LoggedInUser", "");

        firebaseNetworking.getFinesOfStudent(email.substring(4,9),this);

    }

    @Override
    public void onCallback(Boolean isSuccessful, ArrayList<Fine> fines) {
        ViewFineRecyclerViewAdapter adapter = new ViewFineRecyclerViewAdapter(fines);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}
