package com.fyp.baigktk.cuifr.models;

import com.google.android.gms.maps.model.LatLng;

public class RideRequestModel {
    private String source;
    private String destination;
    private LatLng pickUpPoint;
    private long date;

    public long getDepTIme() {
        return depTIme;
    }

    public void setDepTIme(long depTIme) {
        this.depTIme = depTIme;
    }

    long depTIme;



    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public RideRequestModel() {
    }

    public RideRequestModel(String source, String destination, LatLng pickUpPoint) {
        this.source = source;
        this.destination = destination;
        this.pickUpPoint = pickUpPoint;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LatLng getPickUpPoint() {
        return pickUpPoint;
    }

    public void setPickUpPoint(LatLng pickUpPoint) {
        this.pickUpPoint = pickUpPoint;
    }
}
