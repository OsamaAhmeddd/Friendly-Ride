package com.sjsu.se195.uniride;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fyp.baigktk.cuifr.ChatActivity;
import com.fyp.baigktk.cuifr.MainSubcategoryActivity;
import com.fyp.baigktk.cuifr.R;
import com.fyp.baigktk.cuifr.models.Post;
import com.fyp.baigktk.cuifr.models.UserModelRecieve;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class RiderDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap m_map;
    private MarkerOptions set_marker;
    private LatLng pickUp_latlng; //source
    private Button accept_request,reject_request;
    UserModelRecieve model=new UserModelRecieve();
    String myKey= FirebaseAuth.getInstance().getCurrentUser().getUid();
    String riderKey=null;

    private TextView mName,mFare;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rider_details);
        initializations();

        setInformation();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.pickup_map2);
        mapFragment.getMapAsync(RiderDetailsActivity.this);

        reject_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectRequest();
            }
        });
        accept_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String driverKey=FirebaseAuth.getInstance().getCurrentUser().getUid();
                Intent i=getIntent();
                UserModelRecieve userModelRecieve= (UserModelRecieve) i.getSerializableExtra("model");
                String riderkey=userModelRecieve.getRiderKey();

                DatabaseReference acceptedRideRef=FirebaseDatabase.getInstance().getReference().child("acceptedRides").child(driverKey).push();
                acceptedRideRef.setValue(userModelRecieve);

                DatabaseReference riderAcceptedRef=FirebaseDatabase.getInstance().getReference().child("acceptedRidesforRider").child(riderkey).push();
                Map<String,String> driverInfo=new HashMap<>();
                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                driverInfo.put("uid",user.getUid());
                driverInfo.put("email",user.getEmail());
                riderAcceptedRef.setValue(driverInfo);



                DatabaseReference rideRequestRef=FirebaseDatabase.getInstance().getReference().child("rideRequest").child(driverKey);
                Query query=rideRequestRef.orderByChild("riderKey").equalTo(riderkey);
                query.getRef().removeValue();
                String myKey=riderkey+FirebaseAuth.getInstance().getCurrentUser().getUid();

                startActivity(new Intent(RiderDetailsActivity.this, ChatActivity.class).putExtra("string",myKey));


            }
        });
    }

    private void rejectRequest() {
        //String requestId
        DatabaseReference requestRef= FirebaseDatabase.getInstance().getReference().child("rideRequest").child(myKey).child(riderKey);
        requestRef.removeValue();
        startActivity(new Intent(RiderDetailsActivity.this, MainSubcategoryActivity.class).putExtra("postType", Post.PostType.RIDER.name()));
        finish();
    }


    private void setInformation() {


        mName.setText("Name: "+model.getFirstName()+" "+model.getLastName());
        mFare.setText("Fare: "+Double.toString(model.getFareAmount()));
    }

    private void initializations() {
        Intent i=getIntent();
        model=(UserModelRecieve) i.getSerializableExtra("model");
        double lat=Double.valueOf(model.getPickupPoint().getLat());
        double lng=Double.valueOf(model.getPickupPoint().getLng());
        pickUp_latlng=new LatLng(lat,lng);
        riderKey=model.getRiderKey();

        mName=findViewById(R.id.rider_details_name);
        mFare=findViewById(R.id.rider_details_fare);

        accept_request=findViewById(R.id.rider_details_accept_btn);
        reject_request=findViewById(R.id.rider_details_reject_btn);

    }






    @Override
    public void onMapReady(GoogleMap googleMap) {
        m_map = googleMap;
        m_map.clear();
        set_marker = new MarkerOptions()
                .position(new LatLng(this.pickUp_latlng.latitude, this.pickUp_latlng.longitude))
                .title("title").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_black_48dp));
        m_map.addMarker(set_marker);
        CameraPosition target = CameraPosition.builder().target(pickUp_latlng).zoom(14).build();
        m_map.moveCamera(CameraUpdateFactory.newCameraPosition(target));
    }
}
