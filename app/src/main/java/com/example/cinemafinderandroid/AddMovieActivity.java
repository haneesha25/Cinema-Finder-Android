package com.example.cinemafinderandroid;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddMovieActivity extends AppCompatActivity {

    EditText moviename,moviecast,moviedirector;
    Button addmoviebtn;
    ImageView selectmovieimage;

    public Uri imageurl;
    public String movieimageurl,userid;

    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    FirebaseFirestore dbroot;
    FirebaseAuth firebaseAuth;
    ActivityResultLauncher<Intent> mgcontet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        dbroot = FirebaseFirestore.getInstance();

        moviename = findViewById(R.id.moviename);
        moviecast = findViewById(R.id.moviecast);
        moviedirector = findViewById(R.id.moviedirector);
        addmoviebtn = findViewById(R.id.add_movie_btn);
        selectmovieimage = findViewById(R.id.selectmovieimage);

        FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
        userid = user.getUid();


        selectmovieimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,1);
            }
        });

        addmoviebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addmovie();
            }
        });
    }

}