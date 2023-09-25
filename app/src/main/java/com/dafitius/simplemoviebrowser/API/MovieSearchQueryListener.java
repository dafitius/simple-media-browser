package com.dafitius.simplemoviebrowser.API;

import com.dafitius.simplemoviebrowser.Models.MovieSearchQuery;

public interface MovieSearchQueryListener {
     void onResultsAvailable(MovieSearchQuery results);
     void onResultsError(Error error);
}
