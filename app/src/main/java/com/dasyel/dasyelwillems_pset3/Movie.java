package com.dasyel.dasyelwillems_pset3;

public class Movie implements Comparable<Movie>{
    final String title, year, imdbID, type, poster, actors, plot;

    public Movie(String title, String year, String imdbID, String type, String poster){
        this.title = title;
        this.year = year;
        this.imdbID = imdbID;
        this.type = type;
        this.poster = poster;
        this.actors = null;
        this.plot = null;
    }

    public Movie(String title, String year, String imdbID, String type, String poster,
                 String actors, String plot){
        this.title = title;
        this.year = year;
        this.imdbID = imdbID;
        this.type = type;
        this.poster = poster;
        this.actors = actors;
        this.plot = plot;
    }

    @Override
    public int compareTo(Movie o) {
        return this.title.compareTo(o.title);
    }
}
