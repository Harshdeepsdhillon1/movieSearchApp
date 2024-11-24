package com.example.assignment2.view;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.example.assignment2.Api;
import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment2.R;
import com.example.assignment2.databinding.ActivityMovieDetailsBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class MovieDetailsActivity extends AppCompatActivity {

    private ActivityMovieDetailsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // View Binding
        binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get the imdbID from Intent
        String imdbID = getIntent().getStringExtra("imdbID");

        // Log the imdbID
        Log.d("MovieDetailsActivity", "Received imdbID: " + imdbID);

        // Validate imdbID
        if (imdbID == null || imdbID.isEmpty()) {
            Toast.makeText(this, "Movie details are missing!", Toast.LENGTH_SHORT).show();
            finish(); // Exit if no imdbID is available
            return;
        }

        // Fetch movie details using imdbID
        fetchMovieDetails(imdbID);
    }


    // MovieDetailsActivity.java
    private void fetchMovieDetails(String imdbID) {
        String url = "https://www.omdbapi.com/?apikey=a7a37496&i=" + imdbID;

        Api.get(url, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("MovieDetailsActivity", "Network Request Failed", e);
                runOnUiThread(() -> Toast.makeText(MovieDetailsActivity.this, "Failed to load details", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseData = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseData);

                        String title = jsonObject.optString("Title", "N/A");
                        String year = jsonObject.optString("Year", "N/A");
                        String plot = jsonObject.optString("Plot", "No description available.");
                        String poster = jsonObject.optString("Poster", "N/A");
                        String rating = jsonObject.optString("imdbRating", "N/A");

                        runOnUiThread(() -> {
                            binding.title.setText(title);
                            binding.year.setText(year);
                            binding.description.setText(plot);
                            binding.rating.setText("IMDb Rating: " + rating);

                            if (!poster.equalsIgnoreCase("N/A")) {
                                Picasso.get().load(poster).error(R.drawable.placeholder).into(binding.poster);
                            } else {
                                binding.poster.setImageResource(R.drawable.placeholder);
                            }
                        });

                    } catch (Exception e) {
                        Log.e("MovieDetailsActivity", "Parsing Error", e);
                        runOnUiThread(() -> Toast.makeText(MovieDetailsActivity.this, "Error loading details", Toast.LENGTH_SHORT).show());
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(MovieDetailsActivity.this, "Invalid movie data", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

}


