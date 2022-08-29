package com.example.cinemafinderandroid;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SelectSeats extends AppCompatActivity implements PaymentResultListener {

    String gettheaterid,getmovieid;
    TextView displaytheatername,displaymoviename,displaytheateraddress,noofseates,noofprice,fullname;
    TextView setdates;
    Button signplus,signminus,timebtn1,timebtn2,timebtn3,payment;
    int count=1,totalprice=15;
    String timezone,userID;
    boolean isselected = true;

    FirebaseFirestore dbroot;
    FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    ImageView profile,back;
    TextView title;

    LinearLayout selectseatslayout;
    public String userdata = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_seats);


        displaytheatername = findViewById(R.id.displaytheatername);
        displaymoviename = findViewById(R.id.displaymoviename);
        displaytheateraddress = findViewById(R.id.displaytheateraddress);
        fullname = findViewById(R.id.fullname);
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
        userID = user.getUid();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        gettheaterid = getIntent().getStringExtra("theaterID");
        getmovieid = getIntent().getStringExtra("movieID");

        noofprice.setText("Total Price: $"+totalprice);

        selectseatslayout = findViewById(R.id.selectseatslayout);

        title = findViewById(R.id.title);
        profile = (ImageView) findViewById(R.id.profile);
        back = (ImageView) findViewById(R.id.back);


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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectSeats.this, ListMovies.class);
                startActivity(intent);
            }
        });

        DocumentReference documenttheater = dbroot.collection("theater").document(gettheaterid);
        DocumentReference documentmovie = dbroot.collection("movie").document(getmovieid);

        MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);


        documenttheater.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                displaytheatername.setText("Theater : "+documentSnapshot.getString("name"));
                displaytheateraddress.setText("Theater Address: "+documentSnapshot.getString("address"));
            }
        });

        documentmovie.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                displaymoviename.setText("Movie : "+documentSnapshot.getString("name"));
            }
        });
        dbroot.collection("users").document(userID).addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                        fullname.setText(documentSnapshot.getString("fullName"));
                    }
                });

        setdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog dialog = new DatePickerDialog(SelectSeats.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        int month1 = month+1;
                        String date = day+"/"+month1+"/"+year;
                        setdates.setText(date);
                    }
                },year,month,day);

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_MONTH, -7);
                long now = System.currentTimeMillis() ;
                dialog.getDatePicker().setMinDate(now);
                dialog.getDatePicker().setMaxDate(now+(1000*60*60*24*7));
                dialog.show();

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
                if(isselected) {
                    timezone = "";
                    timezone = timebtn1.getText().toString();
                    timebtn1.setBackgroundColor(Color.WHITE);
                    timebtn1.setTextColor(Color.BLACK);
                    isselected = false;
                }else {
                    timezone = "";
                    timebtn1.setBackgroundColor(Color.RED);
                    timebtn1.setTextColor(Color.WHITE);
                    isselected= true;
                    timezone = null;
                }
            }
        });
        timebtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isselected) {
                    timezone = "";
                    timezone = timebtn2.getText().toString();
                    timebtn2.setBackgroundColor(Color.WHITE);
                    timebtn2.setTextColor(Color.BLACK);
                    isselected = false;
                }else {
                    timebtn2.setBackgroundColor(Color.RED);
                    timebtn2.setTextColor(Color.WHITE);
                    isselected= true;
                    timezone = null;
                }
            }
        });
        timebtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isselected) {
                    timezone = "";
                    timezone = timebtn3.getText().toString();
                    timebtn3.setBackgroundColor(Color.WHITE);
                    timebtn3.setTextColor(Color.BLACK);
                    isselected = false;
                }else {
                    timebtn3.setBackgroundColor(Color.RED);
                    timebtn3.setTextColor(Color.WHITE);
                    isselected= true;
                    timezone = null;
                }
            }
        });

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(setdates.getText().toString())){
                    Toast.makeText(SelectSeats.this, "Date is not Selected", Toast.LENGTH_SHORT).show();
                }
                else if(timezone == null){
                    Toast.makeText(SelectSeats.this,"Time Not selected",Toast.LENGTH_SHORT).show();

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
        items.put("numberOfSeats", noofseates.getText().toString());
        items.put("theaterid", gettheaterid);
        items.put("movieid", getmovieid);
        items.put("totalPayment", "$" + totalprice);
        items.put("uid", userID);
        items.put("movieTime", timezone);
        items.put("movieDate", setdates.getText().toString());
        //items.put("paymentId", s);

        dbroot.collection("booking").add(items) .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

                int price = totalprice * count;
                FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
                String url ="https://api.sendgrid.com/v3/mail/send";
                RequestQueue queue = Volley.newRequestQueue(SelectSeats.this);
                String message= "Your Booking has been Confiemd!!!";
                String username = "";
                JSONObject jsonBody = new JSONObject();
                try {
                    JSONArray personalizationsJarray = new JSONArray();

                    JSONArray toArray = new JSONArray();
                    JSONObject toParentObject= new JSONObject();
                    JSONObject toObject= new JSONObject();
                    toObject.put("email",firebaseAuth.getInstance().getCurrentUser().getEmail());
                    toObject.put("name","Cinema Finder User");
                    toArray.put(toObject);

                    JSONArray fromJarray1 = new JSONArray();
                    JSONObject fromObject1= new JSONObject();
                    fromObject1.put("email",user.getEmail());
                    fromObject1.put("name",user.getDisplayName());
                    fromObject1.put("movie",displaymoviename.getText().toString());
                    fromObject1.put("seats", noofseates.getText().toString());
                    fromObject1.put("price",totalprice);
                    fromObject1.put("time",timezone);
                    fromObject1.put("date",setdates.getText().toString());
                    fromObject1.put("theatername",displaytheatername.getText().toString());
                    fromObject1.put("address",displaytheateraddress.getText().toString());
                    fromJarray1.put(fromObject1);


                    toParentObject.put("subject"," Ticket Confirmed");
                    toParentObject.put("to",toArray);
                    toParentObject.put("to",toArray);
                    toParentObject.put("dynamic_template_data",fromObject1);

                    personalizationsJarray.put(toParentObject);

                    //commented contet object in parent object
                    JSONArray contentJarray = new JSONArray();
                    JSONObject contentObject= new JSONObject();
                    contentObject.put("type", "text");
                    contentObject.put("value",message);
                    contentJarray.put(contentObject);

                    JSONArray fromJarray = new JSONArray();
                    JSONObject fromObject= new JSONObject();
                    fromObject.put("email","udaydheerajreddy@gmail.com");
                    fromObject.put("name","Cinema Finder");
                    fromJarray.put(fromObject);

                    jsonBody.put("personalizations",personalizationsJarray);
                    // jsonBody.put("content",contentJarray);
                    jsonBody.put("from",fromObject);
                    //jsonBody.put("template_id","XXXXXXXXXXXXXX");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(1,
                        url, jsonBody, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(SelectSeats.this, "Payment Success and Movie Tickets Booked successfully", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Intent i = new Intent(getApplicationContext(),BookDetails.class);
                        i.putExtra("theaterid",gettheaterid);
                        i.putExtra("movieID",getmovieid);
                        i.putExtra("numberOfSeats", noofseates.getText().toString());
                        i.putExtra("totalPayment", "$" + totalprice);
                        i.putExtra("uid", userID);
                        i.putExtra("movieTime", timezone);
                        i.putExtra("movieDate", setdates.getText().toString());
                        //i.putExtra("paymentId", s);
                        i.putExtra("orderid",documentReference.getId());
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        Toast.makeText(SelectSeats.this, "Payment Success and Movie Tickets Booked successfully", Toast.LENGTH_SHORT).show();                    }
                }
                ){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                       
                        params.put("Content-Type","application/json");
                        return params;
                    }
                };


                queue.add(jsonObjectRequest);

            }
        });
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment Error"+s, Toast.LENGTH_SHORT).show();
    }
}
