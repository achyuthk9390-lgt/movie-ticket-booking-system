package com.movieticket.model;

public class Screen {

    private int screenId;
    private int theatreId;
    private String screenName;
    private int totalSeats;

    public Screen() {
    }

    public Screen(int screenId, int theatreId,
                  String screenName, int totalSeats) {

        this.screenId = screenId;
        this.theatreId = theatreId;
        this.screenName = screenName;
        this.totalSeats = totalSeats;
    }

    public Screen(int theatreId, String screenName, int totalSeats) {
        this.theatreId = theatreId;
        this.screenName = screenName;
        this.totalSeats = totalSeats;
    }

    public int getScreenId() {
        return screenId;
    }

    public void setScreenId(int screenId) {
        this.screenId = screenId;
    }

    public int getTheatreId() {
        return theatreId;
    }

    public void setTheatreId(int theatreId) {
        this.theatreId = theatreId;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    @Override
    public String toString() {
        return "Screen{" +
                "screenId=" + screenId +
                ", theatreId=" + theatreId +
                ", screenName='" + screenName + '\'' +
                ", totalSeats=" + totalSeats +
                '}';
    }
}
