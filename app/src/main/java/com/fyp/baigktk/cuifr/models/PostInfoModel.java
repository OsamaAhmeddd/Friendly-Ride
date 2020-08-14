package com.fyp.baigktk.cuifr.models;

import java.io.Serializable;

public class PostInfoModel implements Serializable {
    private long arrivalTime;
//


    String email;
    Boolean accept;
    private String driverName;
    private String UserKey;
    private long departureTime;
    private long tripDate;
    private String destination;
    private String source;
    private String author;
    private DriverPreferences driverPreferences;
    private long passengerCount;
    private String destination_tripDate;
    private long carMilage;

    public long getCarMilage() {
        return carMilage;
    }

    public void setCarMilage(long carMilage) {
        this.carMilage = carMilage;
    }

    public String getUserKey() {
        return UserKey;
    }

    public void setUserKey(String userKey) {
        UserKey = userKey;
    }

    public Boolean getAccept() {
        return accept;
    }

    public void setAccept(Boolean accept) {
        this.accept = accept;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getDestination_tripDate() {
        return destination_tripDate;
    }

    public void setDestination_tripDate(String destination_tripDate) {
        this.destination_tripDate = destination_tripDate;
    }

    public long getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(long arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public long getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(long departureTime) {
        this.departureTime = departureTime;
    }

    public long getTripDate() {
        return tripDate;
    }

    public void setTripDate(long tripDate) {
        this.tripDate = tripDate;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public DriverPreferences getDriverPreferences() {
        return driverPreferences;
    }

    public void setDriverPreferences(DriverPreferences driverPreferences) {
        this.driverPreferences = driverPreferences;
    }

    public long getPassengerCount() {
        return passengerCount;
    }

    public void setPassengerCount(long passengerCount) {
        this.passengerCount = passengerCount;
    }
}
