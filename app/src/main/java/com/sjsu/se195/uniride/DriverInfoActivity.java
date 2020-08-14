package com.sjsu.se195.uniride;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp.baigktk.cuifr.MainActivity;
import com.fyp.baigktk.cuifr.R;
import com.fyp.baigktk.cuifr.models.PostInfoModel;
import com.fyp.baigktk.cuifr.models.UserModel;
import com.fyp.baigktk.cuifr.models.latlng;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class DriverInfoActivity extends AppCompatActivity {

    UserModel mUser=new UserModel();
    DatabaseReference mdb;

    int fareInt;
    long car_milage;
    private String riderKey=FirebaseAuth.getInstance().getCurrentUser().getUid();
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabaseReference = mDatabase.getReference();
    private TextView mName,mTotalPassengers,mSource,mDestination,mDate,mDepartureTime,mArrivaleTime, mAC,mMusic,mSmoking,mGender,mFareAmount;
    long fff;

    private Button mSubmitButton;
    LatLng pickupPoint;
    latlng pickupPointmodel =new latlng();
    PostInfoModel pm=new PostInfoModel();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_info);

        Bundle b=getIntent().getParcelableExtra("bundle");
        LatLng nearbyRP=b.getParcelable("nearbyRP");
        pickupPoint=b.getParcelable("pickupPoint");
        pickupPointmodel.setLat(String.valueOf(pickupPoint.latitude));
        pickupPointmodel.setLng(String.valueOf(pickupPoint.longitude));
        initializations();



        mdb= FirebaseDatabase.getInstance().getReference();
        mdb.child("Fuel").child("Price").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String va= (String) dataSnapshot.getValue(String.class);
                fff= Long.parseLong(va);


                calculateFareAmount();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        }
        );

        setInfoToViews();


        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendRequestToDriver();
            }
        });


float vvvvvvv=fff;

    }
    private void calculateFareAmount()
    {

        LatLng destinationPoint=getLocationFromAddress(this,pm.getDestination());
        double distanceTravelled= distance(pickupPoint.latitude,pickupPoint.longitude,destinationPoint.latitude,destinationPoint.longitude, 'K');
double dddd=getDistance(pickupPoint.latitude,pickupPoint.longitude,destinationPoint.latitude,destinationPoint.longitude);
        double fare=(dddd*(1.0/car_milage)*fff)/4;
        fareInt=(int)fare;
        mUser.setFareAmount(fareInt);

        mFareAmount.setText("Fare: "+fareInt);
    }
    public LatLng getLocationFromAddress(Context context,String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            //coder.getFromLocationName(strAddress, 5)
            address = coder.getFromLocationName(strAddress, 5);
            if (address.isEmpty()) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }
    public  String minuteToTime(int minute) {
        int hour = minute / 60;
        minute %= 60;
        String p = "";
        if (hour >= 12) {
            hour %= 12;
            p = "";
        }
        if (hour == 0) {
            hour = 12;
        }
        return (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute) + " " + p;
    }

    public  String minuteToTimee(int minute) {
        int hour = minute / 60;
        minute %= 60;
        String p = "AM";
        if (hour >= 12) {
            hour %= 12;
            p = "PM";
        }
        if (hour == 0) {
            hour = 12;
        }
        return (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute) + " " + p;
    }


    private void setInfoToViews() {
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");

        long val = pm.getTripDate();
        Date datee = null;
        try {
            datee = originalFormat.parse(String.valueOf(val));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        mName.setText("Name: "+pm.getAuthor());
        mTotalPassengers.setText("Max Passengers: "+pm.getPassengerCount());
        mSource.setText("Source: "+pm.getSource());
        mDestination.setText("Destination: "+pm.getDestination());
        mDate.setText("Date: "+datee.toString());
        mDepartureTime.setText("Departure Time: "+minuteToTimee((int) pm.getDepartureTime()));
mArrivaleTime.setText("Estimated Time:");
        mArrivaleTime.setText("Esimated Time: "+ (int) pm.getArrivalTime()+ "Minutes");

        if (pm.getDriverPreferences().isAc()) { mAC.setText("AC: Yes"); }
        else{ mAC.setText("AC: No"); }

        if (pm.getDriverPreferences().isSmoking()) {  mSmoking.setText("Smoking: Yes");   }
        else {  mSmoking.setText("Smoking: No"); }

        if(pm.getDriverPreferences().isMusic()) {   mMusic.setText("Music: Yes");}
        else {  mMusic.setText("Music: No");}

        if (pm.getDriverPreferences().getGender().equals("Male"))
        {
            mGender.setText("Gender: Male only");
        }
        else if (pm.getDriverPreferences().getGender().equals("Female"))
        {
            mGender.setText("Gender: Female only");
        }
        else if (pm.getDriverPreferences().getGender().equals("Both"))
        {
            mGender.setText("Gender: Any");
        }

    }

    private void initializations() {


        Intent i=getIntent();
        pm=(PostInfoModel) i.getSerializableExtra("driverInfo");
        car_milage=pm.getCarMilage();

        getRiderInfo();
        mName=findViewById(R.id.driver_info_name);
        mTotalPassengers=findViewById(R.id.driver_info_totalPassengers);
        mSource=findViewById(R.id.driver_info_Source);
        mDestination=findViewById(R.id.driver_info_destination);
        mDate=findViewById(R.id.driver_info_Date);
        mDepartureTime=findViewById(R.id.driver_info_departureTime);
        mArrivaleTime=findViewById(R.id.driver_info_arrivalTime);

        mAC=findViewById(R.id.driver_info_preferences_AC);
        mSmoking=findViewById(R.id.driver_info_preferences_Smoking);
        mMusic=findViewById(R.id.driver_info_preferences_Music);
        mGender=findViewById(R.id.driver_info_preferences_Gender);

        mFareAmount=findViewById(R.id.driver_info_fare_amount);
        mSubmitButton=findViewById(R.id.driver_info_fare_submit_btn);


    }

    private void getRiderInfo()
    {
        DatabaseReference usersRef=mDatabaseReference.child("users").child(riderKey);

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUser=dataSnapshot.getValue(UserModel.class);
                assert mUser != null;
                mUser.setPickupPoint(pickupPointmodel);
                mUser.setRiderKey(riderKey);
                mUser.setSource(pm.getSource());
                mUser.setDestination(pm.getDestination());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DriverInfoActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void SendRequestToDriver() {
        String myKey=FirebaseAuth.getInstance().getCurrentUser().getUid();
       // String myname=FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

        DatabaseReference riderRequestRef=mDatabaseReference.child("rideRequest").child(pm.getUserKey()).push();

      //  DatabaseReference riderRequestRef2=mDatabaseReference.child("UserRequest").child(pm.getUserKey()).child(myKey);

        riderRequestRef.setValue(mUser);

        //pm.set_imp(myname);
        //riderRequestRef.setValue(pm);
 //       riderRequestRef2.setValue(mUser);

        finish();
    }

    private double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'K') {
            dist = dist * 1.609344;
        } else if (unit == 'N') {
            dist = dist * 0.8684;
        }
        return (dist);
    }
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
    //

    private double getDistance(double lat, double lon,double lat1, double lon1){
        Location locationA = new Location("Point A");
        locationA.setLatitude(lat1);
        locationA.setLongitude(lon1);

        Location locationB = new Location("Point B");
        locationB.setLatitude(lat);
        locationB.setLongitude(lon);

        return locationA.distanceTo(locationB)/1000;   // in km
    }
}
