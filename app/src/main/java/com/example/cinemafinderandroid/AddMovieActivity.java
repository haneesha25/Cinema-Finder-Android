package com.example.cinemafinderandroid;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddMovieActivity extends AppCompatActivity {

    EditText moviename,moviecast,moviedirector;
    Button addmoviebtn;
    ImageView selectmovieimage;
    ArrayList<ViewMovieValues> movielist;

    public Uri imageurl;
    public String movieimageurl,userid;
    private String movieId;
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
        movieId = dbroot.collection("movie").getId();
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
    private void addmovie() {

        String ettheatername = moviename.getText().toString();
        String etmoviecast = moviecast.getText().toString();
        String etmoviedirector = moviedirector.getText().toString();

        if(TextUtils.isEmpty(ettheatername)){
            moviename.setError("Movie name can not be empty");
            moviename.requestFocus();
        }
        else if (TextUtils.isEmpty(etmoviecast)){
            moviecast.setError("Movie cast can not be empty");
            moviecast.requestFocus();
        }
        else if (TextUtils.isEmpty(etmoviedirector)){
            moviedirector.setError("Movie Director can not be empty");
            moviedirector.requestFocus();
        }else{
            Map<String, String> items = new HashMap<>();
            items.put("name",ettheatername);
            items.put("imageUrl", movieimageurl);
            items.put("cast",etmoviecast );
            items.put("director",etmoviedirector );
            items.put("adminid",userid );
            items.put("movieid","");


            dbroot.collection("movie").add(items).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    task.getResult().update("movieid", task.getResult().getId());
                }
            });
            Toast.makeText(AddMovieActivity.this, "Add Movie SuccessFully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AddMovieActivity.this, ViewMovieActivity.class);
            startActivity(intent);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //  if(requestCode==1 && requestCode==RESULT_OK && data!=null && data.getData()!=null){
        imageurl = data.getData();
        selectmovieimage.setImageURI(imageurl);
        final String randomkey = UUID.randomUUID().toString();
        final StorageReference sr = storageReference.child("movie/"+randomkey);
        sr.putFile(imageurl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                sr.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Toast.makeText(AddMovieActivity.this, "uri: "+uri, Toast.LENGTH_SHORT).show();
                        movieimageurl = uri.toString();
                    }
                });
            }

        });
    }
}