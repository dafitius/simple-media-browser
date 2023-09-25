package com.dafitius.simplemoviebrowser.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dafitius.simplemoviebrowser.Models.ContentType;
import com.dafitius.simplemoviebrowser.Models.MovieSearchQuery;
import com.dafitius.simplemoviebrowser.API.MovieSearchQueryListener;
import com.dafitius.simplemoviebrowser.API.OmdbAPIManager;
import com.dafitius.simplemoviebrowser.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SearchActivity extends AppCompatActivity {

    FloatingActionButton backButton;
    Button searchButton;
    EditText searchBar;
    ContentType contentType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        backButton = findViewById(R.id.search_button_back);
        backButton.setOnClickListener(v -> finish());

        searchButton = findViewById(R.id.search_button_search);
        searchButton.setOnClickListener(v -> onSearchButtonPressed());

        searchBar = findViewById(R.id.search_textField_searchbar);

        contentType = (ContentType) this.getIntent().getSerializableExtra("CONTENT");

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

    private void onSearchButtonPressed(){
        OmdbAPIManager omdbAPIManager = new OmdbAPIManager(this, new MovieSearchQueryListener() {
            @Override
            public void onResultsAvailable(MovieSearchQuery results) {
                gotoResultsActivity(results, searchBar.getText().toString());
            }

            @Override
            public void onResultsError(Error error) {
                Log.d(this.getClass().getSimpleName(), "could not get search results");
            }
        });


            omdbAPIManager.searchMovie(searchBar.getText().toString(), contentType, 1);


    }

    private void gotoResultsActivity(MovieSearchQuery results, String search){
        Intent intent = new Intent(this, ResultsActivity.class);
        intent.putExtra("RESULTS", results);
        intent.putExtra("SEARCH", search);
        intent.putExtra("PAGEINDEX", 1);
        intent.putExtra("CONTENT", contentType);
        startActivity(intent);
    }
}