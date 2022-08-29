package com.example.cinemafinderandroid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;

public class UpdateProfileActivity extends AppCompatActivity {

    private FirebaseUser user;
    FirebaseFirestore dbroot;
    FirebaseAuth firebaseAuth;
    private String userID;
    EditText usernameTv,emailTv,mobilenumberTv,changepassword;
    Button updated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        user = FirebaseAuth.getInstance().getCurrentUser();
        dbroot = FirebaseFirestore.getInstance();
        userID = user.getUid();
        firebaseAuth=FirebaseAuth.getInstance();

        usernameTv = (EditText) findViewById(R.id.updateusername);
        emailTv = (EditText) findViewById(R.id.updateuseremail);
        mobilenumberTv = (EditText) findViewById(R.id.updateusermobilenumber);
        updated = findViewById(R.id.updated);


        dbroot.collection("users").document(userID).addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                usernameTv.setText(documentSnapshot.getString("fullName"));
                emailTv.setText(user.getEmail());
                mobilenumberTv.setText(documentSnapshot.getString("contactNumber"));
            }
        });

        updated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateUser();
            }
        });
    }
    private void UpdateUser() {
        FirebaseAuth.getInstance();
        user.updateEmail(emailTv.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                HashMap<String, Object> UserdataMap = new HashMap<>();
                UserdataMap.put("email", emailTv.getText().toString());
                UserdataMap.put("fullName", usernameTv.getText().toString());
                UserdataMap.put("contactNumber", mobilenumberTv.getText().toString());

                dbroot.collection("users").document(firebaseAuth.getCurrentUser().getUid()).set(UserdataMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(UpdateProfileActivity.this, "User details Updated Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UpdateProfileActivity.this, ListMovies.class);
                        startActivity(intent);
                    }
                });

            }
        });
    }
}