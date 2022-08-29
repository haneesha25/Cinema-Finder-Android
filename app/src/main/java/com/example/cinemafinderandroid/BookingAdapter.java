package com.example.cinemafinderandroid;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BookingAdapter extends  RecyclerView.Adapter<BookingAdapter.BookingAdapterViewHolder> {

    ArrayList<BookingDetails> bookingDetails;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    Context context;

    public BookingAdapter(ArrayList<BookingDetails> bookingDetails, FirebaseFirestore firebaseFirestore, Context context) {
        this.bookingDetails = bookingDetails;
        this.firebaseFirestore = firebaseFirestore;
        this.context = context;
    }

    @NonNull
    @Override
    public BookingAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookingdata,parent,false);
        return  new BookingAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingAdapterViewHolder holder, int position) {
        final BookingDetails temp = bookingDetails.get(position);

        holder.bookingorderid.setText("ORDER ID "+bookingDetails.get(position).getOrderId());


        holder.bookingdate.setText(bookingDetails.get(position).getMovieDate()+" AT ");
        holder.booktimetime.setText(bookingDetails.get(position).getMovieTime());
        holder.bookingtotalseats.setText(bookingDetails.get(position).getNumberOfSeats()+" seats you have booked");
        holder.bookingtotalprice.setText(bookingDetails.get(position).getTotalPayment());
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("movie").whereEqualTo("movieid",bookingDetails.get(position).getMovieid())
            .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    List<DocumentSnapshot> list1 = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d1:list1) {
                        ViewMovieValues obj1 = d1.toObject(ViewMovieValues.class);
                        holder.bookmoviename.setText("Movie "+obj1.getName());
                    }
                }
            });
        firebaseFirestore.collection("theater").whereEqualTo("theaterid",bookingDetails.get(position).getTheaterid())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list2 = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot d2:list2) {
                            ViewMovieValues obj2 = d2.toObject(ViewMovieValues.class);
                            holder.booktheatername.setText("Theater "+obj2.getName());
                        }
                    }
                });

    }

    @Override
    public int getItemCount() {
        return bookingDetails.size();
    }

    class BookingAdapterViewHolder extends RecyclerView.ViewHolder  {

        TextView bookingorderid,booktheatername,bookmoviename,bookingdate,bookingtimebtn1,booktimetime,bookingtotalseats,bookingtotalprice;

        public BookingAdapterViewHolder(@NonNull View itemView) {

            super(itemView);
            bookingorderid = itemView.findViewById(R.id.bookingorderid);
            booktheatername = itemView.findViewById(R.id.booktheatername);
            bookmoviename = itemView.findViewById(R.id.bookmoviename);
            bookingdate = itemView.findViewById(R.id.bookingdate);
            booktimetime =itemView.findViewById(R.id.booktimetime);
            bookingtotalseats = itemView.findViewById(R.id.bookingtotalseats);
            bookingtotalprice = itemView.findViewById(R.id.bookingtotalprice);
        }
    }

}
