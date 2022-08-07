package com.example.cinemafinderandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class ViewTheatersActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    ArrayList<ViewTheaterValues> viewTheaterValues;
    TheaterAdapter theaterAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_theaters);

        //reference = FirebaseDatabase.getInstance().getReference().child("theater");
        recyclerView = (RecyclerView) findViewById(R.id.showalltheater);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewTheaterValues = new ArrayList<>();
        theaterAdapter = new TheaterAdapter(viewTheaterValues);
        recyclerView.setAdapter(theaterAdapter);

        
    }

}