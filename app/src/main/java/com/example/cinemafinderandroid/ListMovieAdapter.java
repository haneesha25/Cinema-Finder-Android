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

public class ListMovieAdapter extends RecyclerView.Adapter<ListMovieAdapter.ListMovieHolder>{

    ArrayList<ViewMovieValues> showmovielist;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    Context context;

    public ListMovieAdapter(ArrayList<ViewMovieValues> showmovielist, Context context) {
        this.showmovielist = showmovielist;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        this.context = context;
    }

    @NonNull
    @Override
    public ListMovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listmovieiteam,parent,false);
        return new ListMovieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListMovieHolder holder, int position) {

        final ViewMovieValues temp = showmovielist.get(position);

        holder.moviename.setText(showmovielist.get(position).getName());
        holder.moviecast.setText(showmovielist.get(position).getCast());
        holder.moviedirector.setText(showmovielist.get(position).getDirector());
        String movieimageurl = null;
        movieimageurl = showmovielist.get(position).getimageUrl();
        Picasso.get().load(movieimageurl).into(holder.movieimage);

        holder.movieimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context,ListTheater.class);
               // i.putExtra("theaterID",temp.getTheaterID());
                i.putExtra("movieID",showmovielist.get(position).getMovieId());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return showmovielist.size();
    }

    class ListMovieHolder extends RecyclerView.ViewHolder{

        TextView moviename,moviecast,moviedirector;
        ImageView movieimage;

        public ListMovieHolder(@NonNull View itemView) {
            super(itemView);

            moviename = itemView.findViewById(R.id.listmoviename);
            moviecast = itemView.findViewById(R.id.listmoviecast);
            moviedirector = itemView.findViewById(R.id.listmoviedirector);
            movieimage = itemView.findViewById(R.id.listmovieimage);
        }
    }
}
