package com.dafitius.simplemoviebrowser.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.dafitius.simplemoviebrowser.Models.ContentType;
import com.dafitius.simplemoviebrowser.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.dafitius.simplemoviebrowser.Models.ContentType.game;
import static com.dafitius.simplemoviebrowser.Models.ContentType.movie;
import static com.dafitius.simplemoviebrowser.Models.ContentType.series;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        Button gotoSeriesButton = findViewById(R.id.main_button_series);
        gotoSeriesButton.setOnClickListener(v -> gotoSearchActivity(series));

        Button gotoMoviesButton = findViewById(R.id.main_button_movies);
        gotoMoviesButton.setOnClickListener(v -> gotoSearchActivity(movie));

        Button gotoGamesButton = findViewById(R.id.main_button_games);
        gotoGamesButton.setOnClickListener(v -> gotoSearchActivity(game));

        FloatingActionButton favoritesButton = findViewById(R.id.main_button_favo);
        favoritesButton.setOnClickListener(v -> gotoFavoritesActivity());

    }

    private void gotoFavoritesActivity() {
        Intent intent = new Intent(this, FavoritesActivity.class);
        startActivity(intent);

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


    private void gotoSearchActivity(ContentType contentType){
        Intent intent = new Intent(this, SearchActivity.class);
        Log.i(this.getClass().getSimpleName(), "redirecting to search menu to search for: " + contentType.name() + ".");
        intent.putExtra("CONTENT", contentType);
        startActivity(intent);
    }
}