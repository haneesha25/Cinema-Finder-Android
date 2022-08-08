package com.example.cinemafinderandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class ListMovies extends AppCompatActivity {


    RecyclerView showmovielist;
    ImageView profile;
    TextView title;

    LinearLayout listmovielayout;

    //String gettheaterid;
    FirebaseFirestore db;
    ListMovieAdapter listMovieAdapter;
    ArrayList<ViewMovieValues> viewMovieValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_movies);

       // gettheaterid = getIntent().getStringExtra("theaterID"); 

        showmovielist = findViewById(R.id.showallmovielist);
        showmovielist.setLayoutManager(new LinearLayoutManager(this));
        viewMovieValues = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        listMovieAdapter = new ListMovieAdapter(viewMovieValues,getApplicationContext());
        showmovielist.setAdapter(listMovieAdapter);

        listmovielayout = findViewById(R.id.listmovielayout);

        title = findViewById(R.id.title);
        profile = (ImageView) findViewById(R.id.profile);


        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListMovies.this, ListMovies.class);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListMovies.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });

        db.collection("movie").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot d:list){
                    String movieID = d.getId();
                    ViewMovieValues obj = d.toObject(ViewMovieValues.class);
                    obj.setMovieId(movieID);
                  //  obj.setTheaterID(gettheaterid);
                    viewMovieValues.add(obj);
                }
                listMovieAdapter.notifyDataSetChanged();
            }
        });


       // Toast.makeText(this, "Theater ID"+ getIntent().getStringExtra("theaterID"), Toast.LENGTH_SHORT).show();
    }
}