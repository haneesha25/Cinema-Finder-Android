package com.example.cinemafinderandroid;

public class BookingDetails{

    String movieDate,movieTime,movieid,theaterid;
    String orderId,numberOfSeats,totalPayment,uid,moviename,theatername;

    public BookingDetails() {
    }

    public BookingDetails(String movieDate, String movieTime, String movieid, String theaterid, String orderId, String numberOfSeats, String totalPayment, String uid) {
        this.movieDate = movieDate;
        this.movieTime = movieTime;
        this.movieid = movieid;
        this.theaterid = theaterid;
        this.orderId = orderId;
        this.numberOfSeats = numberOfSeats;
        this.totalPayment = totalPayment;
        this.uid = uid;
    }

    public String getMovieDate() {
        return movieDate;
    }

    public void setMovieDate(String movieDate) {
        this.movieDate = movieDate;
    }

    public String getMovieTime() {
        return movieTime;
    }

    public void setMovieTime(String movieTime) {
        this.movieTime = movieTime;
    }

    public String getMovieid() {
        return movieid;
    }

    public void setMovieid(String movieid) {
        this.movieid = movieid;
    }

    public String getTheaterid() {
        return theaterid;
    }

    public void setTheaterid(String theaterid) {
        this.theaterid = theaterid;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumbersOfSeats(String numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public String getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(String totalPayment) {
        this.totalPayment = totalPayment;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMoviename() {
        return moviename;
    }

    public void setMoviename(String moviename) {
        this.moviename = moviename;
    }

    public String getTheatername() {
        return theatername;
    }

    public void setTheatername(String theatername) {
        this.theatername = theatername;
    }
}
