package com.example.eindopdrachtmoviebrowser.API;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.eindopdrachtmoviebrowser.ContentType;
import com.example.eindopdrachtmoviebrowser.Movie;
import com.example.eindopdrachtmoviebrowser.MovieSearchQuery;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class OmdbAPIManager {
    private static final String LOGTAG = OmdbAPIManager.class.getName();

    private Context appContext;
    private RequestQueue queue;
    private OmdbAPIListener movieListener;
    private MovieSearchQueryListener searchListener;

    public OmdbAPIManager(Context context, OmdbAPIListener movieListener, MovieSearchQueryListener searchListener) {
        this.appContext = context;
        this.movieListener = movieListener;
        this.searchListener = searchListener;
        // Create the RequestQueue for Volley requests
        this.queue = Volley.newRequestQueue(this.appContext);
    }

    public OmdbAPIManager(Context context, OmdbAPIListener movieListener) {
        this.appContext = context;
        this.movieListener = movieListener;
        // Create the RequestQueue for Volley requests
        this.queue = Volley.newRequestQueue(this.appContext);
    }

    public OmdbAPIManager(Context context, MovieSearchQueryListener searchListener) {
        this.appContext = context;
        this.searchListener = searchListener;
        // Create the RequestQueue for Volley requests
        this.queue = Volley.newRequestQueue(this.appContext);
    }

    public void getMovie(String movieName, boolean isID) {
        String url;
        if(isID)  url = "https://www.omdbapi.com/?i=" + movieName + "&apikey=48d5b4ea";
        else url = "https://www.omdbapi.com/?t=" + movieName.replaceAll(" ", "+") + "&apikey=48d5b4ea";

        Log.d(this.getClass().getSimpleName(),"searching: " + url);

        // Define a request with its attributes and two callback methods, one for success, the other for failure
        final JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, // Use HTTP GET to retrieve the data from the NASA API
                url, // This is the actual URL used to retrieve the data
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    // The callback for handling the response
                    public void onResponse(JSONObject response) {
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
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    // The callback for handling a transmission error
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error
                        Log.e(LOGTAG, error.getLocalizedMessage());
                        movieListener.onMovieError(new Error(error.getLocalizedMessage()));
                    }
                }
        );
        // Add the request that was just created to the request queue, this starts off the actual netwerk transmission
        this.queue.add(request);
    }



    public void searchMovie(String search, ContentType contentType, int page) {
        final String url =
                "https://www.omdbapi.com/?s=" + search.replaceAll(" ", "+") + "&type=" + contentType.name() +  "&page=" + page + "&apikey=48d5b4ea";


        // Define a request with its attributes and two callback methods, one for success, the other for failure
        final JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, // Use HTTP GET to retrieve the data from the NASA API
                url, // This is the actual URL used to retrieve the data
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    // The callback for handling the response
                    public void onResponse(JSONObject response) {
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

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    // The callback for handling a transmission error
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error
                        Log.e(LOGTAG, error.getLocalizedMessage());
                        searchListener.onResultsError(new Error(error.getLocalizedMessage()));
                    }
                }
        );
        // Add the request that was just created to the request queue, this starts off the actual netwerk transmission
        this.queue.add(request);
    }
}
