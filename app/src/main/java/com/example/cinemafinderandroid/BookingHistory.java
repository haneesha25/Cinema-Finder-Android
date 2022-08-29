package com.example.cinemafinderandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class BookingHistory extends AppCompatActivity {

    RecyclerView bookinghistory;
    LinearLayout listmovielayout;
    String userid;

    ImageView profile;
    TextView title;

    FirebaseFirestore db;
    FirebaseAuth auth;
    private DatabaseReference mDatabase;

    BookingAdapter bookingAdapter;
    ArrayList<BookingDetails> bookingDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);

        bookinghistory = findViewById(R.id.bookinghistorydetails);
        bookinghistory.setLayoutManager(new LinearLayoutManager(this));
        bookingDetails = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userid = auth.getCurrentUser().getUid();
        bookingAdapter = new BookingAdapter(bookingDetails,db,getApplicationContext());
        bookinghistory.setAdapter(bookingAdapter);
        mDatabase = FirebaseDatabase.getInstance().getReference();


        listmovielayout = findViewById(R.id.bookinglinearlayout);

        title = findViewById(R.id.title);
        profile = (ImageView) findViewById(R.id.profile);


        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookingHistory.this, ListMovies.class);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookingHistory.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });


        db.collection("booking").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot d:list){
                    BookingDetails obj = d.toObject(BookingDetails.class);
                    if (obj.getUid().equals(userid)){
                        obj.setOrderId(d.getId());
                        bookingDetails.add(obj);
                    }
                }
                bookingAdapter.notifyDataSetChanged();
            }
        });
    }
}