package com.example.assignment2.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.assignment2.Api;
import com.example.assignment2.databinding.ActivityMainBinding;
import com.example.assignment2.model.Movie;
import com.example.assignment2.viewmodel.MovieAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MovieAdapter movieAdapter;
    private ArrayList<Movie> movieList = new ArrayList<>();
    private static final String API_KEY = "ea7ebca0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize RecyclerView with adapter
        movieAdapter = new MovieAdapter(movieList, movie -> {
            // Log the selected movie details for debugging
            Log.d("MainActivity", "Selected Movie: " + movie.getTitle() + ", imdbID: " + movie.getImdbID());

            // Start MovieDetailsActivity with the selected movie's imdbID
            Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
            intent.putExtra("imdbID", movie.getImdbID()); // Pass imdbID to details screen
            startActivity(intent);
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(movieAdapter);

        // Set up search button click listener
        binding.searchButton.setOnClickListener(view -> {
            String query = binding.searchField.getText().toString().trim();
            if (!query.isEmpty()) {
                fetchMovies(query);
            } else {
                Log.e("MainActivity", "Search query is empty!");
                binding.searchButton.setText("Please enter a movie name.");
            }
        });
    }

    // Fetch movies from the API
    private void fetchMovies(String query) {
        String url = "https://www.omdbapi.com/?apikey=" + API_KEY + "&s=" + query;

        Api.get(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("MainActivity", "API Request Failed", e);
                runOnUiThread(() -> binding.searchButton.setText("Failed to fetch movies. Try again!"));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseData = response.body().string();
                        JSONObject jsonObject = new JSONObject(responseData);

                        if (jsonObject.has("Search")) {
                            JSONArray searchArray = jsonObject.getJSONArray("Search");
                            movieList.clear(); // Clear previous results
                            for (int i = 0; i < searchArray.length(); i++) {
                                JSONObject movieObject = searchArray.getJSONObject(i);
                                String title = movieObject.optString("Title", "N/A");
                                String year = movieObject.optString("Year", "N/A");
                                String poster = movieObject.optString("Poster", "N/A");
                                String imdbID = movieObject.optString("imdbID", "");

                                // Add movie to the list
                                movieList.add(new Movie(title, year, poster, imdbID));
                            }

                            // Notify the adapter on the main thread
                            runOnUiThread(() -> {
                                movieAdapter.notifyDataSetChanged();
                                binding.searchButton.setText("Search"); // Reset button text
                            });
                        } else {
                            Log.e("MainActivity", "No movies found in response");
                            runOnUiThread(() -> binding.searchButton.setText("No movies found!"));
                        }
                    } catch (Exception e) {
                        Log.e("MainActivity", "Parsing error", e);
                        runOnUiThread(() -> binding.searchButton.setText("Error parsing data!"));
                    }
                } else {
                    Log.e("MainActivity", "Invalid API response");
                    runOnUiThread(() -> binding.searchButton.setText("Failed to fetch movies. Try again!"));
                }
            }
        });
    }
}
