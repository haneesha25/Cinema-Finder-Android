package com.example.cinemafinderandroid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class UserProfileActivity extends AppCompatActivity {
    private FirebaseUser user;
    FirebaseFirestore dbroot;
    FirebaseAuth firebaseAuth;
    private String userID;
    TextView usernameTv,emailTv,mobilenumberTv,changepassword;
    Button signOut,updateprofile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        user = FirebaseAuth.getInstance().getCurrentUser();
        dbroot = FirebaseFirestore.getInstance();
        userID = user.getUid();
        firebaseAuth=FirebaseAuth.getInstance();

        usernameTv = (TextView) findViewById(R.id.username);
        emailTv = (TextView) findViewById(R.id.useremail);
        mobilenumberTv = (TextView) findViewById(R.id.usermobilenumber);
        signOut = (Button) findViewById(R.id.signoutbtn);
        changepassword = (TextView) findViewById(R.id.changepassword);
        updateprofile = findViewById(R.id.updateprofile);

        changepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.sendPasswordResetEmail(user.getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(UserProfileActivity.this, "Change password link has been sent to your mail id", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(UserProfileActivity.this,LoginActivity.class));
            }
        });

        dbroot.collection("users").document(userID).addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                usernameTv.setText(documentSnapshot.getString("fullName"));
                emailTv.setText(user.getEmail());
                mobilenumberTv.setText(documentSnapshot.getString("contactNumber"));
            }
        });

        updateprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserProfileActivity.this,UpdateProfileActivity.class));
            }
        });
    }
}