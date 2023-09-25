package com.dafitius.simplemoviebrowser.Models;

import java.io.Serializable;

public class Movie implements Serializable {
    private String Title;
    private String Year;
    private String Rated;
    private String Released;
    private String Runtime;
    private String Genre;
    private String Director;
    private String Writer;
    private String Actors;
    private String Plot;
    private String Language;
    private String Country;
    private String Awards;
    private String Poster;
    private Rating[] Ratings;
    private String Metascore;
    private String imdbRating;
    private String imdbVotes;
    private String imdbID;
    private String Type;
    private String DVD;
    private String BoxOffice;
    private String Production;
    private String Website;
    private String Response;


    public String getTitle() {
        return Title;
    }

    public String getYear() {
        return Year;
    }

    public String getRated() {
        return Rated;
    }

    public String getReleased() {
        return Released;
    }

    public String getRuntime() {
        return Runtime;
    }

    public String[] getGenre() {
        if(Genre != null) return Genre.split(", ");
        else return new String[0];
    }

    public String[] getDirector() {
        if(Director != null) return Director.split(", ");
        else return new String[0];
    }

    public String[] getWriter(boolean useTags) {
        if(Writer != null) {
            String[] writers = Writer.split(", ");

            if (!useTags) {
                for (int i = 0; i < writers.length; i++) {
                    if (writers[i].contains(" (")) {
                        writers[i] = writers[i].split("\\(")[0];
                    }
                }
            }
            return writers;
        } else return new String[0];
    }

    public String[] getActors() {
        if(Actors != null) return Actors.split(", ");
        else return new String[0];
    }

    public String getPlot() {
        return Plot;
    }

    public String[] getLanguage() {
        if(Language != null) return Language.split(", ");
        else return new String[0];
    }

    public String[] getCountry() {
        if(Country != null) return Country.split(", ");
        else return new String[0];
    }

    public String getAwards() {
        return Awards;
    }

    public String getPoster() {
        return Poster;
    }

    public Rating[] getRatings() {
        if(Ratings != null) return Ratings;
        else return new Rating[0];
    }

    public String getMetascore() {
        return Metascore;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public String getImdbVotes() {
        return imdbVotes;
    }

    public String getImdbID() {
        return imdbID;
    }

    public String getType() {
        return Type;
    }

    public String getDVD() {
        return DVD;
    }

    public String getBoxOffice() {
        return BoxOffice;
    }

    public String getProduction() {
        return Production;
    }

    public String getWebsite() {
        return Website;
    }

    public String getResponse() {
        return Response;
    }

    public Movie(String title) {
        Title = title;
    }
}

