package com.fyp.baigktk.cuifr.models;

public class UserModel {
    public ratingModel getRating() {
        return rating;
    }

    public void setRating(ratingModel rating) {
        this.rating = rating;
    }

    ratingModel rating;
    private String email;
    public String username;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String defaultOrganizationId;
    private String imageURL;

    private latlng pickupPoint;
    private double fareAmount;
    private String riderKey;
    private int carMilage;
    private String gender;

    private String source;
    private String destination;


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

    public int getCarMilage() {
        return carMilage;
    }

    public void setCarMilage(int carMilage) {
        this.carMilage = carMilage;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


    public String getRiderKey() {
        return riderKey;
    }

    public void setRiderKey(String riderKey) {
        this.riderKey = riderKey;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDefaultOrganizationId() {
        return defaultOrganizationId;
    }

    public void setDefaultOrganizationId(String defaultOrganizationId) {
        this.defaultOrganizationId = defaultOrganizationId;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public latlng getPickupPoint() {
        return pickupPoint;
    }

    public void setPickupPoint(latlng
                                       pickupPoint) {
        this.pickupPoint = pickupPoint;
    }

    public double getFareAmount() {
        return fareAmount;
    }

    public void setFareAmount(double fareAmount) {
        this.fareAmount = fareAmount;
    }
}
