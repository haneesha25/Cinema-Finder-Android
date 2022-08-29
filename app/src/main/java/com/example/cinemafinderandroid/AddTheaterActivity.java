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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddTheaterActivity extends AppCompatActivity {

    EditText theatername,theateraddress;
    Button addtheaterbtn;
    ImageView selectimage;
    String userid;
    public Uri imageurl;
    public String theaterimageurl;
    private String theaterid;


    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore dbroot;
    ActivityResultLauncher<Intent> mgcontet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_theater);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        dbroot = FirebaseFirestore.getInstance();

        theatername = findViewById(R.id.theatername);
        theateraddress = findViewById(R.id.theateraddress);
        addtheaterbtn = findViewById(R.id.add_theater_btn);
        selectimage = findViewById(R.id.selectimage);

        FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
        userid = user.getUid();

        selectimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,1);
            }
        });

        addtheaterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addtheater();
            }
        });
    }
    private void addtheater() {

        String ettheatername = theatername.getText().toString();
        String ettheateraddress = theateraddress.getText().toString();
        //  String ettheaterimageurl = theaterimageurl;

        if(TextUtils.isEmpty(ettheatername)){
            theatername.setError("Email can not be empty");
            theatername.requestFocus();
        }
        else if (TextUtils.isEmpty(ettheateraddress)){
            theateraddress.setError("Password can not be empty");
            theateraddress.requestFocus();
        }else {

            Map<String, String> items = new HashMap<>();
            items.put("name", ettheatername);
            items.put("address", ettheateraddress);
            //items.put("imageUrl", theaterimageurl);
            //items.put("adminid", userid);

            dbroot.collection("theater").add(items).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    task.getResult().update("theaterid",task.getResult().getId());
                }
            });
            // Toast.makeText(AddTheaterActivity.this, "Url" + theaterimageurl, Toast.LENGTH_SHORT).show();
            Toast.makeText(AddTheaterActivity.this, "Add theater SuccessFully", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //  if(requestCode==1 && requestCode==RESULT_OK && data!=null && data.getData()!=null){
        imageurl = data.getData();
        selectimage.setImageURI(imageurl);
        final String randomkey = UUID.randomUUID().toString();
        final StorageReference sr = storageReference.child("theater/"+randomkey);
        sr.putFile(imageurl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                sr.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Toast.makeText(AddTheaterActivity.this, "uri: "+uri, Toast.LENGTH_SHORT).show();
                        theaterimageurl = uri.toString();
                    }
                });
            }

        });
    }
}