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

    }
}
