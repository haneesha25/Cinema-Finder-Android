package com.example.cinemafinderandroid;

import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {



    ArrayList<ViewMovieValues> movielist;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    public MovieAdapter(ArrayList<ViewMovieValues> movielist) {

        this.movielist = movielist;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movieitems,parent,false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {

        holder.moviename.setText(movielist.get(position).getName());
        holder.moviecast.setText(movielist.get(position).getCast());
        holder.moviedirector.setText(movielist.get(position).getDirector());
        String movieimageurl = null;
        movieimageurl = movielist.get(position).getimageUrl();
        Picasso.get().load(movieimageurl).into(holder.imageView);

        holder.deletemovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseFirestore.collection("movie").document(movielist.get(position).getMovieId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            movielist.remove(movielist.get(position));
                            notifyDataSetChanged();
                            Toast.makeText(view.getContext(), "Movie Deleted", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(view.getContext(), "Something went wrong please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        holder.editmovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(),EditMovieActivity.class);
                i.putExtra("movieID",movielist.get(position).getMovieId());
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return movielist.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder
    {
        TextView moviename,moviecast,moviedirector;
        Button deletemovie,editmovie;
        ImageView imageView;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            moviename = itemView.findViewById(R.id.recyclemoviename);
            moviecast = itemView.findViewById(R.id.recyclemoviecast);
            moviedirector = itemView.findViewById(R.id.recyclemoviedirector);
            deletemovie = itemView.findViewById(R.id.deletemovie);
            imageView = itemView.findViewById(R.id.movieimage);
            editmovie = itemView.findViewById(R.id.editmovie);


        }
    }
}
