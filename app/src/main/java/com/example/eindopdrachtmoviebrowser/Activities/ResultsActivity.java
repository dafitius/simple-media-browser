package com.example.eindopdrachtmoviebrowser.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.eindopdrachtmoviebrowser.Adapters.MoviePosterAdapter;
import com.example.eindopdrachtmoviebrowser.ContentType;
import com.example.eindopdrachtmoviebrowser.MovieSearchQuery;
import com.example.eindopdrachtmoviebrowser.API.MovieSearchQueryListener;
import com.example.eindopdrachtmoviebrowser.API.OmdbAPIManager;
import com.example.eindopdrachtmoviebrowser.R;
import com.example.eindopdrachtmoviebrowser.SearchResult;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ResultsActivity extends AppCompatActivity {

    FloatingActionButton backButton;
    RecyclerView RecView;
    MovieSearchQuery results;
    TextView counter;
    String search;
    int page;
    ContentType contentType;

    FloatingActionButton nextList;
    FloatingActionButton previousList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        backButton = findViewById(R.id.results_button_back);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        results = (MovieSearchQuery) this.getIntent().getSerializableExtra("RESULTS");
        search = this.getIntent().getStringExtra("SEARCH");
        page = this.getIntent().getIntExtra("PAGEINDEX", 0);
        contentType = (ContentType) this.getIntent().getSerializableExtra("CONTENT");


        counter = findViewById(R.id.results_text_counter);
        counter.setText(page + "/" + computeAmountOfPages(results.getTotalResults()));

        SearchResult[] searchResults = results.getSearch();

        String[] values = new String[searchResults.length];
        String[] images = new String[searchResults.length];
        String[] ids = new String[searchResults.length];
        for (int i = 0; i < searchResults.length; i++) {
            values[i] = searchResults[i].getTitle();
            images[i] = searchResults[i].getPoster();
            ids[i] = searchResults[i].getImdbID();
        }
        RecView = (RecyclerView) findViewById(R.id.gridview);
        MoviePosterAdapter moviePosterAdapter = new MoviePosterAdapter(this, values, images, ids);
        RecView.setLayoutManager(new LinearLayoutManager(this));
        RecView.setAdapter(moviePosterAdapter);

        nextList = findViewById(R.id.favorites_button_back);
        previousList = findViewById(R.id.results_button_previous);

        nextList.setOnClickListener(v-> gotoNextPage());
        previousList.setOnClickListener(v->gotoPreviousPage());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    private void gotoNextPage() {

        Intent intent = new Intent(this, ResultsActivity.class);
        OmdbAPIManager omdbAPIManager = new OmdbAPIManager(this, new MovieSearchQueryListener() {
            @Override
            public void onResultsAvailable(MovieSearchQuery results) {

                Log.i(this.getClass().getSimpleName(), "opening next page (" + page + ")");
                intent.putExtra("RESULTS", results);
                intent.putExtra("SEARCH", search);
                intent.putExtra("PAGEINDEX", page + 1);
                intent.putExtra("CONTENT", contentType);
                startActivity(intent);
                finish();
            }

            @Override
            public void onResultsError(Error error) {}
        });


        omdbAPIManager.searchMovie(search, contentType, page + 1);





    }

    private void gotoPreviousPage() {

        Intent intent = new Intent(this, ResultsActivity.class);
        OmdbAPIManager omdbAPIManager = new OmdbAPIManager(this, new MovieSearchQueryListener() {
            @Override
            public void onResultsAvailable(MovieSearchQuery results) {

                Log.i(this.getClass().getSimpleName(), "Opening previous page. (" + page + ")");
                intent.putExtra("RESULTS", results);
                intent.putExtra("SEARCH", search);
                if(page - 1 > 0) intent.putExtra("PAGEINDEX", page - 1);
                else intent.putExtra("PAGEINDEX", page);
                intent.putExtra("CONTENT", contentType);
                startActivity(intent);
                finish();

            }

            @Override
            public void onResultsError(Error error) {}
        });

        if(page - 1 >= 1) {
            omdbAPIManager.searchMovie(search, contentType, page - 1);
        }


    }

    private int computeAmountOfPages(int amountOfPosters){
        int amount = amountOfPosters / 10;

        if(amountOfPosters % 10 > 0) amount = amount + 1;
        return amount;

    }
}