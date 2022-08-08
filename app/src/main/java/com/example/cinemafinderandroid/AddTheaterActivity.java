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

public class AddTheaterActivity extends AppCompatActivity {

    EditText theatername,theateraddress;
    Button addtheaterbtn;
    ImageView selectimage;
    String userid;
    public Uri imageurl;
    public String theaterimageurl;


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
            items.put("imageUrl", theaterimageurl);
            items.put("adminid", userid);

            dbroot.collection("theater").add(items);
            // Toast.makeText(AddTheaterActivity.this, "Url" + theaterimageurl, Toast.LENGTH_SHORT).show();
            Toast.makeText(AddTheaterActivity.this, "Add theater SuccessFully", Toast.LENGTH_SHORT).show();
        }

    }
    

}