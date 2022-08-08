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

public class EditTheaterActivity extends AppCompatActivity {

    String theaterId;
    EditText updatetheatername,updatetheateraddress;
    ImageView updatetheaterimage;
    Button edittheater;

    public Uri updimageurl;
    public String theaterimageuri;

    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    FirebaseFirestore dbroot;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_theater);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        dbroot = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        theaterId = getIntent().getStringExtra("theaterID");

        updatetheaterimage = findViewById(R.id.updatetheaterimage);
        updatetheatername = findViewById(R.id.updatetheatername);
        updatetheateraddress = findViewById(R.id.updatetheateraddress);
        edittheater = findViewById(R.id.update_theater_btn);

        dbroot.collection("theater").document(theaterId).addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                updatetheatername.setText(documentSnapshot.getString("name"));
                updatetheateraddress.setText(documentSnapshot.getString("address"));
                Picasso.get().load(documentSnapshot.getString("imageUrl")).into(updatetheaterimage);
            }
        });

        updatetheaterimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EditTheaterActivity.this, "Image clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,1);
            }
        });

        edittheater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EditTheaterActivity.this, "Edit clicked", Toast.LENGTH_SHORT).show();
                HashMap<String, Object> edittheaterdata = new HashMap<>();
                edittheaterdata.put("name", updatetheatername.getText().toString());
                edittheaterdata.put("address", updatetheateraddress.getText().toString());
                edittheaterdata.put("imageUrl", theaterimageuri);

                dbroot.collection("theater").document(theaterId).set(edittheaterdata).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(EditTheaterActivity.this, "Theater Updated Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditTheaterActivity.this, ViewTheatersActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //  if(requestCode==1 && requestCode==RESULT_OK && data!=null && data.getData()!=null){
        updimageurl = data.getData();
        updatetheaterimage.setImageURI(updimageurl);
        final String randomkey = UUID.randomUUID().toString();
        final StorageReference sr = storageReference.child("theater/"+randomkey);
        sr.putFile(updimageurl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(EditTheaterActivity.this, "uri: "+sr.getDownloadUrl(), Toast.LENGTH_SHORT).show();
                sr.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        theaterimageuri = uri.toString();
                    }
                });
            }
        });
    }
}