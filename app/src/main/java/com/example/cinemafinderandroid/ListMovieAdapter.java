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


    }
}
