package com.dafitius.simplemoviebrowser.API;

import com.dafitius.simplemoviebrowser.Models.Movie;

public interface OmdbAPIListener {
    void onMovieAvailable(Movie movie);
    void onMovieError(Error error);
}
