package com.dafitius.simplemoviebrowser.Models;

import java.io.Serializable;

public class MovieSearchQuery implements Serializable {

    private SearchResult[] Search;
    private String totalResults;
    private String Response;

    public SearchResult[] getSearch() {
        return Search;
    }

    public int getTotalResults() {
        return Integer.parseInt(totalResults);
    }

    public String getResponse() {
        return Response;
    }
}

