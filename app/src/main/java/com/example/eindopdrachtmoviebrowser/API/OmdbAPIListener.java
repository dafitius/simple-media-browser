package com.example.eindopdrachtmoviebrowser.API;

import com.example.eindopdrachtmoviebrowser.Movie;

public interface OmdbAPIListener {
    public void onMovieAvailable(Movie movie);
    public void onMovieError(Error error);
}
