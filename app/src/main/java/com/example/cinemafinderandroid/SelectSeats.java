package com.example.cinemafinderandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SelectSeats extends AppCompatActivity implements PaymentResultListener {

    String gettheaterid,getmovieid;
    TextView displaytheatername,displaymoviename,displaytheateraddress,noofseates,noofprice;
    TextView setdates;
    Button signplus,signminus,timebtn1,timebtn2,timebtn3,payment;
    int count=1,totalprice=15;
    String timezone,userid;

    FirebaseFirestore dbroot;
    FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    ImageView profile;
    TextView title;

    LinearLayout selectseatslayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_seats);


        displaytheatername = findViewById(R.id.displaytheatername);
        displaymoviename = findViewById(R.id.displaymoviename);

        setdates = findViewById(R.id.setdate);
        signminus = findViewById(R.id.signminus);
        signplus = findViewById(R.id.signplus);
        noofseates = findViewById(R.id.noofseates);
        noofprice = findViewById(R.id.noofprice);
        timebtn1 = findViewById(R.id.timebtn1);
        timebtn2 = findViewById(R.id.timebtn2);
        timebtn3 = findViewById(R.id.timebtn3);
        payment = findViewById(R.id.payment);

        dbroot = FirebaseFirestore.getInstance();
        FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
        userid = user.getUid();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        gettheaterid = getIntent().getStringExtra("theaterID");
        getmovieid = getIntent().getStringExtra("movieID");

        noofprice.setText("Total Price: $"+totalprice);

        selectseatslayout = findViewById(R.id.selectseatslayout);

        title = findViewById(R.id.title);
        profile = (ImageView) findViewById(R.id.profile);


        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectSeats.this, ListMovies.class);
                startActivity(intent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectSeats.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });

        DocumentReference documenttheater = dbroot.collection("theater").document(gettheaterid);
        DocumentReference documentmovie = dbroot.collection("movie").document(getmovieid);

        MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        documenttheater.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                displaytheatername.setText("Theater : "+documentSnapshot.getString("name"));
//                displaytheateraddress.setText("Theater Address: "+documentSnapshot.getString("address"));
            }
        });

        documentmovie.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                displaymoviename.setText("Movie : "+documentSnapshot.getString("name"));
            }
        });

        setdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.show(getSupportFragmentManager(), "Material_Date_Picker");
                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        setdates.setText(datePicker.getHeaderText());
                    }
                });
            }
        });

        signplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
                String currentDateandTime = sdf.format(new Date());

                if(count > 10 && currentDateandTime.equals(setdates.getText().toString())){
                    Toast.makeText(SelectSeats.this, "You can select only maximum 10 seats", Toast.LENGTH_SHORT).show();
                }
                else{

                    noofseates.setText(""+count);
                    totalprice = 15;
                    totalprice = totalprice*count;
                    noofprice.setText("Total Price: $"+totalprice);
                }


            }
        });

        signminus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count<=1) count=1;
                else
                    count--;
                noofseates.setText(""+count);
                totalprice = 15;
                totalprice = totalprice*count;
                noofprice.setText("Total Price: $"+totalprice);
            }
        });

        timebtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timezone = "";
                timezone = timebtn1.getText().toString();
                timebtn1.setBackgroundColor(Color.WHITE);
                timebtn1.setTextColor(Color.BLACK);
            }
        });
        timebtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timezone = "";
                timezone = timebtn2.getText().toString();
                timebtn2.setBackgroundColor(Color.WHITE);
                timebtn2.setTextColor(Color.BLACK);
            }
        });
        timebtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timezone = "";
                timezone = timebtn3.getText().toString();
                timebtn3.setBackgroundColor(Color.WHITE);
                timebtn3.setTextColor(Color.BLACK);
            }
        });

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(setdates.getText().toString())){
                    Toast.makeText(SelectSeats.this, "Date is not Selected", Toast.LENGTH_SHORT).show();
                }
                else {

                    String totalprice1 = String.valueOf(totalprice);
                    int amount = Math.round(Float.parseFloat(totalprice1) *100);

                    Checkout checkout = new Checkout();
                    checkout.setKeyID("rzp_test_HCVYCp9beI7gNu");
                    checkout.setImage(R.drawable.splashscreen);

                    JSONObject object = new JSONObject();
                    try {

                        object.put("name", "Cinema Finder");
                        object.put("description", "Test payment");
                        object.put("theme.color", "");
                        object.put("currency", "CAD");
                        object.put("amount", amount);
                        object.put("prefill.contact", dbroot.collection("theater").document(gettheaterid));
                        object.put("prefill.email", "bhoopathi575@gmail.com");

                        // open razorpay to checkout activity
                        checkout.open(SelectSeats.this, object);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    @Override
    public void onPaymentSuccess(String s) {
        Map<String, Object> items = new HashMap<>();
        items.put("numbersOfSeats", noofseates.getText().toString());
        items.put("theaterid", gettheaterid);
        items.put("movieid", getmovieid);
        items.put("totalPayment", "$" + totalprice);
        items.put("uid", userid);
        items.put("movieTime", timezone);
        items.put("movieDate", setdates.getText().toString());
        items.put("paymentId", s);

        dbroot.collection("booking").add(items) .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(SelectSeats.this, "Payment Success and Movie Tickets Reserved successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment Error"+s, Toast.LENGTH_SHORT).show();
    }
}