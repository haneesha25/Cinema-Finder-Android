package com.example.cinemafinderandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class ViewMovieActivity extends AppCompatActivity {

    RecyclerView showallmovie;
    ArrayList<ViewMovieValues> movielist;
    FirebaseFirestore db;
    MovieAdapter movieAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_movie);

        showallmovie = findViewById(R.id.showallmovie);
        showallmovie.setLayoutManager(new LinearLayoutManager(this));
        movielist = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        movieAdapter = new MovieAdapter(movielist,getApplicationContext());
        showallmovie.setAdapter(movieAdapter);

        db.collection("movie").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot d:list){
                    String movieID = d.getId();
                    ViewMovieValues obj = d.toObject(ViewMovieValues.class);
                    obj.setMovieId(movieID);
                    movielist.add(obj);
                }
                movieAdapter.notifyDataSetChanged();

            }
        });


    }
}