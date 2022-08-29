package com.example.cinemafinderandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class BookDetails extends AppCompatActivity {

    TextView booktheater,bookmovie,bookdate,booktime,booktotalseats,booktotalprice,orderid,bookinghistory;

    String gettheaterid,getmovieid;

    FirebaseFirestore dbroot;
    FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    ImageView profile;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        booktheater = findViewById(R.id.booktheater);
        bookmovie = findViewById(R.id.bookmovie);
        bookdate = findViewById(R.id.bookdate);
        booktime = findViewById(R.id.booktime);
        booktotalseats = findViewById(R.id.booktotalseats);
        booktotalprice = findViewById(R.id.booktotalprice);
        orderid = findViewById(R.id.orderid);
        bookinghistory = findViewById(R.id.bookinghistory);

        dbroot = FirebaseFirestore.getInstance();
        FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        gettheaterid = getIntent().getStringExtra("theaterid");
        getmovieid = getIntent().getStringExtra("movieID");

        DocumentReference documenttheater = dbroot.collection("theater").document(gettheaterid);
        DocumentReference documentmovie = dbroot.collection("movie").document(getmovieid);

        documenttheater.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                booktheater.setText("Theater : "+documentSnapshot.getString("name"));
//                displaytheateraddress.setText("Theater Address: "+documentSnapshot.getString("address"));
            }
        });

        documentmovie.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                bookmovie.setText("Movie : "+documentSnapshot.getString("name"));
            }
        });

        title = findViewById(R.id.title);
        profile = (ImageView) findViewById(R.id.profile);


        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookDetails.this, ListMovies.class);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookDetails.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });

        bookinghistory.setTextColor(Color.parseColor("#FEFEFF"));
        orderid.setText("Your Order ID "+getIntent().getStringExtra("orderid"));
        bookdate.setText("Date" + getIntent().getStringExtra("movieDate"));
        booktime.setText(getIntent().getStringExtra("movieTime"));
        booktotalseats.setText(getIntent().getStringExtra("numberOfSeats"));
        booktotalprice.setText("Total Price: "+getIntent().getStringExtra("totalPayment"));

    }
}