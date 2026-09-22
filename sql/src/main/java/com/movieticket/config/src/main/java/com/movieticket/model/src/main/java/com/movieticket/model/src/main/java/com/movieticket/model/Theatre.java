package com.movieticket.model;

public class Theatre {

    private int theatreId;
    private String theatreName;
    private String city;
    private String address;

    public Theatre() {
    }

    public Theatre(int theatreId, String theatreName,
                   String city, String address) {

        this.theatreId = theatreId;
        this.theatreName = theatreName;
        this.city = city;
        this.address = address;
    }

    public Theatre(String theatreName, String city, String address) {
        this.theatreName = theatreName;
        this.city = city;
        this.address = address;
    }

    public int getTheatreId() {
        return theatreId;
    }

    public void setTheatreId(int theatreId) {
        this.theatreId = theatreId;
    }

    public String getTheatreName() {
        return theatreName;
    }

    public void setTheatreName(String theatreName) {
        this.theatreName = theatreName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Theatre{" +
                "theatreId=" + theatreId +
                ", theatreName='" + theatreName + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
