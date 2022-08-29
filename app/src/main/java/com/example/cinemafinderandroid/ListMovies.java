package com.example.cinemafinderandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


import java.io.IOException;
import java.io.IOException;
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

    SearchView searchView;
    DatabaseReference ref;

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

        searchView = findViewById(R.id.searchmovie);
        ref = FirebaseDatabase.getInstance().getReference().child("movie");

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

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<ViewMovieValues> mylist = new ArrayList<>();
                for (ViewMovieValues vm : viewMovieValues){
                    if(vm.getName().toLowerCase().contains(s.toLowerCase())){
                        //Toast.makeText(ListMovies.this, vm.getName(), Toast.LENGTH_SHORT).show();
                        mylist.add(vm);
                    }
                    ListMovieAdapter listMovieAdapter = new ListMovieAdapter(mylist,getApplicationContext());
                    showmovielist.setAdapter(listMovieAdapter);
                }
                return true;
            }
        });

       // Toast.makeText(this, "Theater ID"+ getIntent().getStringExtra("theaterID"), Toast.LENGTH_SHORT).show();
    }


    /* @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.searchmenu,menu);
        MenuItem item = menu.getItem(R.id.search);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                processsearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                processsearch(s);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void processsearch(String s) {
    }*/
}