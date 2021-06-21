package com.example.eindopdrachtmoviebrowser.API;

import com.example.eindopdrachtmoviebrowser.MovieSearchQuery;

public interface MovieSearchQueryListener {
    public void onResultsAvailable(MovieSearchQuery results);
    public void onResultsError(Error error);
}
