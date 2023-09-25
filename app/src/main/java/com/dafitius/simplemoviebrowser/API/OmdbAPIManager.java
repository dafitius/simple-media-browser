package com.dafitius.simplemoviebrowser.API;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dafitius.simplemoviebrowser.BuildConfig;
import com.dafitius.simplemoviebrowser.Models.ContentType;
import com.dafitius.simplemoviebrowser.Models.Movie;
import com.dafitius.simplemoviebrowser.Models.MovieSearchQuery;
import com.google.gson.Gson;
import org.json.JSONException;

public class OmdbAPIManager {
    private static final String LOGTAG = OmdbAPIManager.class.getName();

    private final Context appContext;
    private final RequestQueue queue;
    private OmdbAPIListener movieListener;
    private MovieSearchQueryListener searchListener;

    public OmdbAPIManager(Context context, OmdbAPIListener movieListener) {
        this.appContext = context;
        this.movieListener = movieListener;
        this.queue = Volley.newRequestQueue(this.appContext);
    }

    public OmdbAPIManager(Context context, MovieSearchQueryListener searchListener) {
        this.appContext = context;
        this.searchListener = searchListener;
        this.queue = Volley.newRequestQueue(this.appContext);
    }

    public void getMovie(String movieName, boolean isID) {
        String url;
        String omdbKey = BuildConfig.OMDB_KEY;
        if(isID)  url = "https://www.omdbapi.com/?i=" + movieName + "&apikey=" + omdbKey;
        else url = "https://www.omdbapi.com/?t=" + movieName.replaceAll(" ", "+") + "&apikey=" + omdbKey;

        Log.d(this.getClass().getSimpleName(),"searching: " + url);

        final JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    Log.d(LOGTAG, "Volley response: " + response.toString());

                    String responseNotice;
                    try {
                        responseNotice = response.getString("Response");

                        if (!responseNotice.equals("False")) {

                            Gson gson = new Gson();
                            Movie movie = gson.fromJson(response.toString(), Movie.class);
                            movieListener.onMovieAvailable(movie);

                        } else{
                            movieListener.onMovieError(new Error("movie not found"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e(LOGTAG, error.getLocalizedMessage());
                    movieListener.onMovieError(new Error(error.getLocalizedMessage()));
                }
        );
        this.queue.add(request);
    }



    public void searchMovie(String search, ContentType contentType, int page) {
        final String url =
                "https://www.omdbapi.com/?s=" + search.replaceAll(" ", "+") + "&type=" + contentType.name() +  "&page=" + page + "&apikey=48d5b4ea";


        final JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    Log.d(LOGTAG, "Volley response: " + response.toString());
                    String responseNotice;
                    try {
                        responseNotice = response.getString("Response");

                        if (!responseNotice.equals("False")) {

                            Gson gson = new Gson();
                            MovieSearchQuery results = gson.fromJson(response.toString(), MovieSearchQuery.class);
                            searchListener.onResultsAvailable(results);
                        }else{
                            searchListener.onResultsError(new Error("movie not found"));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                },
                error -> {
                    Log.e(LOGTAG, error.getLocalizedMessage());
                    searchListener.onResultsError(new Error(error.getLocalizedMessage()));
                }
        );
        this.queue.add(request);
    }
}
