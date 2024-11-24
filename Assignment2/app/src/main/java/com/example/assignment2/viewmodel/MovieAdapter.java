package com.example.assignment2.viewmodel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment2.R;
import com.example.assignment2.model.Movie;

import com.squareup.picasso.Picasso; // For loading images from URLs

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movieList;
    private MovieClickListener movieClickListener;

    // Constructor
    public MovieAdapter(List<Movie> movieList, MovieClickListener movieClickListener) {
        this.movieList = movieList;
        this.movieClickListener = movieClickListener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        // Get the movie at the current position
        Movie movie = movieList.get(position);

        // Set the movie title, year, and poster image
        holder.titleTextView.setText(movie.getTitle());
        holder.yearTextView.setText(movie.getYear());

        // Use Picasso to load the poster image
        Picasso.get()
                .load(movie.getPoster()) // Load the poster URL
                .placeholder(R.drawable.placeholder) // Placeholder image while loading
                .into(holder.posterImageView);

        // Set an OnClickListener to notify the activity when a movie item is clicked
        holder.itemView.setOnClickListener(v -> movieClickListener.onMovieClick(movie));
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    // ViewHolder class to represent each movie item
    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        TextView yearTextView;
        ImageView posterImageView;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            // Find views from the item layout
            titleTextView = itemView.findViewById(R.id.title);
            yearTextView = itemView.findViewById(R.id.year);
            posterImageView = itemView.findViewById(R.id.poster);
        }
    }

    // Interface for handling movie clicks
    public interface MovieClickListener {
        void onMovieClick(Movie movie);
    }
}
