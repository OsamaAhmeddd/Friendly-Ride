package com.fyp.baigktk.cuifr.models;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class DriverPreferences implements Serializable {
    private String passengerCount;
    private String gender;
    private boolean smoking;
    private boolean music;
    private boolean ac;

    public  DriverPreferences(){}
    public DriverPreferences(String passengerCount, String gender, boolean smoking, boolean music, boolean ac) {
        this.passengerCount = passengerCount;
        this.gender = gender;
        this.smoking = smoking;
        this.music = music;
        this.ac = ac;
    }

    public String getPassengerCount() {
        return passengerCount;
    }

    public void setPassengerCount(String passengerCount) {
        this.passengerCount = passengerCount;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isSmoking() {
        return smoking;
    }

    public void setSmoking(boolean smoking) {
        this.smoking = smoking;
    }

    public boolean isMusic() {
        return music;
    }

    public void setMusic(boolean music) {
        this.music = music;
    }

    public boolean isAc() {
        return ac;
    }

    public void setAc(boolean ac) {
        this.ac = ac;
    }





}

