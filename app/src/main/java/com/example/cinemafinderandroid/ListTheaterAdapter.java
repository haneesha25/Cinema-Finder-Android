package com.example.cinemafinderandroid;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListTheaterAdapter extends RecyclerView.Adapter<ListTheaterAdapter.ListTheaterViewHolder>{

    ArrayList<ViewTheaterValues> viewTheaterValues;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    Context context;

    public ListTheaterAdapter(ArrayList<ViewTheaterValues> viewTheaterValues, Context context) {
        this.viewTheaterValues = viewTheaterValues;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        this.context = context;
    }

    @NonNull
    @Override
    public ListTheaterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listtheateritem,parent,false);
        return  new ListTheaterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListTheaterViewHolder holder, int position) {

        final ViewTheaterValues temp = viewTheaterValues.get(position);

        holder.recyclemoviename.setText(viewTheaterValues.get(position).getName());
        holder.recyclemoviecast.setText(viewTheaterValues.get(position).getAddress());
       // String theaterimageurl = null;
     //   theaterimageurl = viewTheaterValues.get(position).getImageUrl();
      //  Picasso.get().load(theaterimageurl).into(holder.recyclemimage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context,SelectSeats.class);
                i.putExtra("theaterID",temp.getTheaterId());
                i.putExtra("movieID",temp.getMovieId());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });

       /* holder.recyclemimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context,SelectSeats.class);
                i.putExtra("theaterID",temp.getTheaterId());
                i.putExtra("movieID",temp.getMovieId());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return viewTheaterValues.size();
    }



    class ListTheaterViewHolder extends RecyclerView.ViewHolder  {

        TextView recyclemoviename,recyclemoviecast;
        ImageView recyclemimage;

        public ListTheaterViewHolder(@NonNull View itemView) {

            super(itemView);
            recyclemoviename = (TextView) itemView.findViewById(R.id.listtheatername);
            recyclemoviecast = (TextView) itemView.findViewById(R.id.listtheateraddress);
            /*recyclemimage = (ImageView) itemView.findViewById(R.id.listtheaterimage);*/

        }
    }

}
