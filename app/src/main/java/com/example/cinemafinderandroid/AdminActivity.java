package com.example.cinemafinderandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminActivity extends AppCompatActivity {

    Button addmovie,addtheater,editmovie,edittheater,yourprofile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        addmovie = findViewById(R.id.adminaddmovie);
        addtheater = findViewById(R.id.adminaddtheater);
        editmovie = findViewById(R.id.admineditmovie);
        edittheater = findViewById(R.id.adminedittheater);
        yourprofile = findViewById(R.id.adminprofile);

        addmovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //finish();
                Intent intent = new Intent(AdminActivity.this, AddMovieActivity.class);
                startActivity(intent);
            }
        });
        addtheater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //finish();
                Intent intent = new Intent(AdminActivity.this, AddTheaterActivity.class);
                startActivity(intent);
            }
        });
        editmovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //finish();
                Intent intent = new Intent(AdminActivity.this, ViewMovieActivity.class);
                startActivity(intent);
            }
        });
        edittheater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //finish();
                Intent intent = new Intent(AdminActivity.this, ViewTheatersActivity.class);
                startActivity(intent);
            }
        });
        yourprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //finish();
                Intent intent = new Intent(AdminActivity.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}