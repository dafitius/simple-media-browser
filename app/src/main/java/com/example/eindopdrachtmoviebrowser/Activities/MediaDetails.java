package com.example.eindopdrachtmoviebrowser.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eindopdrachtmoviebrowser.Adapters.CastAdapter;
import com.example.eindopdrachtmoviebrowser.Adapters.MoviePosterAdapter;
import com.example.eindopdrachtmoviebrowser.Movie;
import com.example.eindopdrachtmoviebrowser.R;

import com.example.eindopdrachtmoviebrowser.Rating;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.HashSet;
import java.util.Set;

public class MediaDetails extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs";

    FloatingActionButton backButton;
    FloatingActionButton favoriteButton;
    FloatingActionButton shareButton;
    boolean isFavorite;
    Movie movie;

    ImageView poster;
    TextView title;
    TextView year;
    TextView description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_details);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        //add back button
        backButton = findViewById(R.id.details_button_back);
        backButton.setOnClickListener(v -> finish());




        movie = (Movie) this.getIntent().getSerializableExtra("MOVIE");

        shareButton = findViewById(R.id.details_button_share);
        shareButton.setOnClickListener(v -> shareMovie(movie, this));

        //add favorite button
        isFavorite = checkIfFavorite();
        favoriteButton = findViewById(R.id.details_button_favorite);
        if(isFavorite) favoriteButton.setImageResource(android.R.drawable.star_on);
        else favoriteButton.setImageResource(android.R.drawable.star_off);


        favoriteButton.setOnClickListener(v -> {
                    if (isFavorite) {
                        removeFavorite(movie.getImdbID());
                        favoriteButton.setImageResource(android.R.drawable.star_off);
                        isFavorite = false;
                    }
                    else {
                        addFavorite(movie.getImdbID());
                        favoriteButton.setImageResource(android.R.drawable.star_on);

                        isFavorite = true;
                    }
                }
        );




        //define elements
        poster = findViewById(R.id.details_image_poster);
        title = findViewById(R.id.details_text_title);
        year = findViewById(R.id.details_text_year);

        //set elements
        Picasso.get().load(movie.getPoster()).into(poster);
        title.setText(movie.getTitle());
        year.setText("(" + movie.getYear() + ")");

        //add genre recyclerview
        LinearLayoutManager genreLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView genreRecyclerView = (RecyclerView) findViewById(R.id.details_recycler_genres);
        genreRecyclerView.setLayoutManager(genreLayoutManager);
        genreRecyclerView.setAdapter(getGenreAdapter());

        //add rating recyclerview
        LinearLayoutManager ratingLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView ratingRecyclerView = (RecyclerView) findViewById(R.id.details_recycler_ratings);
        ratingRecyclerView.setLayoutManager(ratingLayoutManager);
        ratingRecyclerView.setAdapter(getRatingAdapter());

        DividerItemDecoration ratingDecoration = new DividerItemDecoration(ratingRecyclerView.getContext(), DividerItemDecoration.HORIZONTAL);
        Drawable divider = getResources().getDrawable(R.drawable.divider);
        ratingDecoration.setDrawable(divider);
        ratingRecyclerView.addItemDecoration(ratingDecoration);

        //add cast recyclerview
        LinearLayoutManager castLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView castRecyclerView = (RecyclerView) findViewById(R.id.details_recycler_cast);
        castRecyclerView.setLayoutManager(castLayoutManager);
        castRecyclerView.setAdapter(new CastAdapter(this, movie.getDirector(), movie.getWriter(false), movie.getActors()));

        DividerItemDecoration castDecoration = new DividerItemDecoration(castRecyclerView.getContext(), DividerItemDecoration.HORIZONTAL);
        castRecyclerView.addItemDecoration(castDecoration);

        //define and set description
        description = findViewById(R.id.details_text_description);
        description.setText(movie.getPlot());

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

    public void shareMovie(Movie movie, Context context){
        ImageView siv = (ImageView) new ImageView(this);
        Picasso.get().load(movie.getPoster()).into(siv);

        Drawable mDrawable = siv.getDrawable();
        Bitmap mBitmap = ((BitmapDrawable)mDrawable).getBitmap();

        String path = MediaStore.Images.Media.insertImage(getContentResolver(),
                mBitmap, movie.getTitle().replaceAll(" ", "-"), "image downloaded from David's media browser");

        Uri uri = Uri.parse(path);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.putExtra(Intent.EXTRA_TEXT,  context.getText(R.string.sharing_description) + " " + movie.getTitle() + "!");
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(shareIntent,  "this do be kinda tricky tho"));
    }


    public RecyclerView.Adapter getGenreAdapter() {
        return new RecyclerView.Adapter() {

            String[] genres = movie.getGenre();


            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                TextView textView = new TextView(parent.getContext());
                textView.setPadding(4, 0, 4, 0);

                View view = textView;

                return new MoviePosterAdapter.ViewHolder(view, parent.getContext());
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                TextView textView = (TextView) holder.itemView;
                textView.setText(" " + genres[position] + " ");
                Drawable d = getResources().getDrawable(R.drawable.border);
                textView.setBackground(d);
            }

            @Override
            public int getItemCount() {
                return genres.length;
            }
        };
    }

    public RecyclerView.Adapter getRatingAdapter() {
        return new RecyclerView.Adapter() {

            Rating[] ratings = movie.getRatings();


            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                TextView textView = new TextView(parent.getContext());

                View view = textView;

                return new MoviePosterAdapter.ViewHolder(view, parent.getContext());
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

                TextView textView = (TextView) holder.itemView;
                textView.setText(" " + ratings[position].toString() + " ");
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                textView.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                Drawable d = getResources().getDrawable(R.drawable.border);
                textView.setBackground(d);

            }

            @Override
            public int getItemCount() {
                return ratings.length;
            }
        };
    }

    private boolean checkIfFavorite() {
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Set<String> set = sharedpreferences.getStringSet("favo", null);
        if(set != null) {
            if (set.contains(movie.getImdbID())) {
                return true;
            } else return false;
        } else{
            createFavorite();
            return false;
        }
    }

    private void createFavorite() {
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedpreferences.edit();

        Set<String> set = new HashSet<>();

        editor.putStringSet("favo", set);
        editor.clear();
        editor.commit();
        editor.apply();
    }

    private void addFavorite(String movieId) {

        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedpreferences.edit();

        //Retrieve the values
        Set<String> set = sharedpreferences.getStringSet("favo", null);

        //Set the values
        if (set != null) {
            set.add(movieId);

        } else{
            set = new HashSet<>();
            set.add(movieId);
        }
        Log.i(this.getClass().getSimpleName(), "added " + movieId + " to the favorite movies");
        editor.putStringSet("favo", set);
        editor.clear();
        editor.commit();
        editor.apply();
    }

    private void removeFavorite(String title) {

        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedpreferences.edit();

        //Retrieve the values
        Set<String> set = sharedpreferences.getStringSet("favo", null);

        //Set the values
        if (set != null && set.contains(title)) {
            set.remove(title);
            Log.i(this.getClass().getSimpleName(), "Removed " + title + " from the favorites");
            editor.putStringSet("favo", set);
        }
        editor.clear();
        editor.commit();
        editor.apply();

    }
}