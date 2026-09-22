package com.movieticket.model;

import java.math.BigDecimal;

public class Seat {

    private int seatId;
    private int screenId;
    private String seatNumber;
    private String seatType;
    private BigDecimal price;

    public Seat() {
    }

    public Seat(int seatId, int screenId,
                String seatNumber, String seatType,
                BigDecimal price) {

        this.seatId = seatId;
        this.screenId = screenId;
        this.seatNumber = seatNumber;
        this.seatType = seatType;
        this.price = price;
    }

    public Seat(int screenId, String seatNumber,
                String seatType, BigDecimal price) {

        this.screenId = screenId;
        this.seatNumber = seatNumber;
        this.seatType = seatType;
        this.price = price;
    }

    public int getSeatId() {
        return seatId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    public int getScreenId() {
        return screenId;
    }

    public void setScreenId(int screenId) {
        this.screenId = screenId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "seatId=" + seatId +
                ", screenId=" + screenId +
                ", seatNumber='" + seatNumber + '\'' +
                ", seatType='" + seatType + '\'' +
                ", price=" + price +
                '}';
    }
}
