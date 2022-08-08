package com.example.cinemafinderandroid;

public class ViewMovieValues {

    String name,cast,director;
    String movieId,theaterID;;
    String imageUrl;


    public ViewMovieValues() {
    }

    public ViewMovieValues(String name, String cast, String director,String imageUrl) {
        this.name = name;
        this.cast = cast;
        this.director = director;
        this.imageUrl = imageUrl;
    }

    public String getTheaterID() {
        return theaterID;
    }

    public void setTheaterID(String theaterID) {
        this.theaterID = theaterID;
    }

    public String getimageUrl() {
        return imageUrl;
    }

    public void setimageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }
}
