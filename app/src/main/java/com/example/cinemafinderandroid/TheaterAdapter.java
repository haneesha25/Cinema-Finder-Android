package com.example.cinemafinderandroid;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TheaterAdapter extends RecyclerView.Adapter<TheaterAdapter.TheaterViewHolder>{


    ArrayList<ViewTheaterValues> viewTheaterValues;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    Context context;

    public TheaterAdapter(ArrayList<ViewTheaterValues> viewTheaterValues, Context context) {

        this.viewTheaterValues = viewTheaterValues;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        this.context = context;
    }

    @NonNull
    @Override
    public TheaterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.theateritems,parent,false);
        return  new TheaterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TheaterViewHolder holder, int position) {
        holder.recyclemoviename.setText(viewTheaterValues.get(position).getName());
        holder.recyclemoviecast.setText(viewTheaterValues.get(position).getAddress());

        String theaterimageurl = null;
        theaterimageurl = viewTheaterValues.get(position).getImageUrl();
        if(!TextUtils.isEmpty(theaterimageurl)){
            //Picasso.get().load(theaterimageurl).into(holder.recyclemimage);
            Glide.with(context).load(theaterimageurl).into(holder.recyclemimage);
        }

       // holder.recyclemoviedirector.setText(viewTheaterValues.get(position).getRecyclermdirector());
        //holder.recyclemimage.setImageResource(Integer.parseInt(viewTheaterValues.get(position).getImageUrl()));

        holder.deletetheater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseFirestore.collection("theater").document(viewTheaterValues.get(position).getTheaterId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            viewTheaterValues.remove(viewTheaterValues.get(position));
                            notifyDataSetChanged();
                            Toast.makeText(view.getContext(), "Theater Deleted", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(view.getContext(), "Something went wrong please try again", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

       
        }
    }
}
