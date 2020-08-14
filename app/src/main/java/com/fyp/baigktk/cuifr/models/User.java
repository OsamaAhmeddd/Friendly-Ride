package com.fyp.baigktk.cuifr.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

// [START blog_user_class]
@IgnoreExtraProperties
public class User {
    public ratingModel getRating() {
        return rating;
    }

    public void setRating(ratingModel rating) {
        this.rating = rating;
    }

    public ratingModel rating;
    public String email;
    public String username;
    public String firstName;
    public String lastName;
    public String phoneNumber;
    public String defaultOrganizationId;
    public String imageURL;
    public LatLng pickupPoint;
    public double fareAmount;

    public int getCarMilage() {
        return carMilage;
    }

    public void setCarMilage(int carMilage) {
        this.carMilage = carMilage;
    }

    private int carMilage;
    private String gender;



    public User() {
        //empty constructor for firebase
    }

    // Sets email and sets all other user attributes to empty string ("").
    public User(String email, String first, String last, String phone, String imageURL) {
        this.email = email;

        // set the other fields as empty strings:
        this.username = "";
        this.firstName = first;
        this.lastName = last;
        this.phoneNumber = phone;
        this.defaultOrganizationId = "";
        this.imageURL = imageURL;

    }

    public User(String email, String first, String last, String phone, String imageURL, int carMilage, String gender) {
        this.email = email;
        this.username = "";
        this.firstName = first;
        this.lastName = last;
        this.phoneNumber = phone;
        this.defaultOrganizationId = "";
        this.imageURL = imageURL;
        this.carMilage = carMilage;
        this.gender = gender;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email", email);
        result.put("username", username);
        result.put("firstName", firstName);
        result.put("lastName", lastName);
        result.put("phoneNumber", phoneNumber);
        result.put("defaultOrganizationId", defaultOrganizationId);
        result.put("gender",gender);
        result.put("carMilage",carMilage);

        return result;
    }
    // [END post_to_map]
}
// [END blog_user_class]
