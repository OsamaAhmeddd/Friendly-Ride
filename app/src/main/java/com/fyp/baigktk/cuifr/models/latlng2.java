package com.fyp.baigktk.cuifr.models;

import java.io.Serializable;

public class latlng2 implements Serializable {
    private long latitude;
    private long longitude;

    public long getLat() {
        return latitude;
    }

    public void setLat(long latitude) {
        this.latitude = latitude;
    }

    public long getLng() {
        return longitude;
    }

    public void setLng(long longitude) {
        this.longitude = longitude;
    }
}
