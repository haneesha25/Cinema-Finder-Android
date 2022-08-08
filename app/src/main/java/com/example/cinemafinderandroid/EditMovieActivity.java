package com.example.cinemafinderandroid;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.UUID;

public class EditMovieActivity extends AppCompatActivity {

    String movieId;
    EditText updatemoviename,updatemoviecast,updatemoviedirector;
    ImageView updatemovieimage;
    Button editmovie;

    public Uri updimageurl;
    public String movieimageurl;

    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    FirebaseFirestore dbroot;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_movie);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        dbroot = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        movieId = getIntent().getStringExtra("movieID");

        updatemovieimage = findViewById(R.id.updatemovieimage);
        updatemoviename = findViewById(R.id.updatemoviename);
        updatemoviecast = findViewById(R.id.updatemoviecast);
        updatemoviedirector = findViewById(R.id.updatemoviedirector);
        editmovie = findViewById(R.id.update_movie_btn);



        dbroot.collection("movie").document(movieId).addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                updatemoviecast.setText(documentSnapshot.getString("cast"));
                updatemoviedirector.setText(documentSnapshot.getString("director"));
                updatemoviename.setText(documentSnapshot.getString("moviename"));
                Picasso.get().load(documentSnapshot.getString("imageUrl")).into(updatemovieimage);
            }
        });

//        updatemovieimage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(EditMovieActivity.this, "Image clicked", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("image/*");
//                startActivityForResult(intent,1);
//            }
//        });
//
//        editmovie.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(EditMovieActivity.this, "Edit clicked", Toast.LENGTH_SHORT).show();
//                HashMap<String, Object> editmoviedata = new HashMap<>();
//                editmoviedata.put("cast", updatemoviecast.getText().toString());
//                editmoviedata.put("director", updatemoviedirector.getText().toString());
//                editmoviedata.put("moviename", updatemoviename.getText().toString());
//                editmoviedata.put("imageUrl", movieimageurl);
//
//                dbroot.collection("movie").document(movieId).set(editmoviedata).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        Toast.makeText(EditMovieActivity.this, "Movie Updated Successfully", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(EditMovieActivity.this, ViewMovieActivity.class);
//                        startActivity(intent);
//                    }
//                });
//            }
//        });
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        //  if(requestCode==1 && requestCode==RESULT_OK && data!=null && data.getData()!=null){
//        updimageurl = data.getData();
//        updatemovieimage.setImageURI(updimageurl);
//        final String randomkey = UUID.randomUUID().toString();
//        final StorageReference sr = storageReference.child("movie/"+randomkey);
//        sr.putFile(updimageurl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                Toast.makeText(EditMovieActivity.this, "uri: "+sr.getDownloadUrl(), Toast.LENGTH_SHORT).show();
//                sr.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        movieimageurl = uri.toString();
//                    }
//                });
//            }
//        });
//    }
//}