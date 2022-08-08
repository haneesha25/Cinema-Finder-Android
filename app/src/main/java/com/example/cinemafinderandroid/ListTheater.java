package com.example.cinemafinderandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListTheater extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<ViewTheaterValues> viewTheaterValues;
    ListTheaterAdapter theaterAdapter;
    String movieID;

    ImageView profile;
    TextView title;

    LinearLayout listtheaterlayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_theater);

        movieID = getIntent().getStringExtra("movieID");

        recyclerView = (RecyclerView) findViewById(R.id.showalltheater);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewTheaterValues = new ArrayList<>();
        theaterAdapter = new ListTheaterAdapter(viewTheaterValues,getApplicationContext());
        recyclerView.setAdapter(theaterAdapter);

        listtheaterlayout = findViewById(R.id.listtheaterlayout);

        title = findViewById(R.id.title);
        profile = (ImageView) findViewById(R.id.profile);


        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListTheater.this, ListMovies.class);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListTheater.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });


        
    }
}