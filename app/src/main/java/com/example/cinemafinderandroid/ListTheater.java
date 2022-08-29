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
import android.widget.SearchView;
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
    SearchView searchView;

    ArrayList<ViewTheaterValues> viewTheaterValues;
    FirebaseFirestore db;
    ListTheaterAdapter theaterAdapter;
    String movieID;

    ImageView profile,back;
    TextView title;

    LinearLayout listtheaterlayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_theater);

        movieID = getIntent().getStringExtra("movieID");

        searchView = findViewById(R.id.searchtheater);
        recyclerView = (RecyclerView) findViewById(R.id.showalltheater);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        viewTheaterValues = new ArrayList<>();
        theaterAdapter = new ListTheaterAdapter(viewTheaterValues,getApplicationContext());
        recyclerView.setAdapter(theaterAdapter);

        listtheaterlayout = findViewById(R.id.listtheaterlayout);

        title = findViewById(R.id.title);
        profile = (ImageView) findViewById(R.id.profile);
        back = (ImageView) findViewById(R.id.back) ;




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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListTheater.this, ListMovies.class);
                startActivity(intent);
            }
        });



        db = FirebaseFirestore.getInstance();
        db.collection("theater").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot d:list){
                    String theaterId = d.getId();
                    ViewTheaterValues obj = d.toObject(ViewTheaterValues.class);
                    obj.setMovieId(movieID);
                    obj.setTheaterId(theaterId);
                    viewTheaterValues.add(obj);
                }
                theaterAdapter.notifyDataSetChanged();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                ArrayList<ViewTheaterValues> mylist = new ArrayList<>();
                for (ViewTheaterValues vm : viewTheaterValues){
                    if(vm.getName().toLowerCase().contains(s.toLowerCase())){
                        mylist.add(vm);
                    }
                    ListTheaterAdapter listTheaterAdapter = new ListTheaterAdapter(mylist,getApplicationContext());
                    recyclerView.setAdapter(listTheaterAdapter);
                }
                return true;
            }
        });

    }
}