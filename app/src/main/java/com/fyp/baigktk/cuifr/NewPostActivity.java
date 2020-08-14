package com.fyp.baigktk.cuifr;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.fyp.baigktk.cuifr.MainActivity;
import com.fyp.baigktk.cuifr.R;
import com.fyp.baigktk.cuifr.fragment.DatePickerFragment;
import com.fyp.baigktk.cuifr.fragment.TimePickerFragment;
import com.fyp.baigktk.cuifr.models.DriverOfferPost;
import com.fyp.baigktk.cuifr.models.DriverPreferences;
import com.fyp.baigktk.cuifr.models.PostInfoModel;
import com.fyp.baigktk.cuifr.models.RideRequestModel;
import com.fyp.baigktk.cuifr.models.RideRequestPost;
import com.fyp.baigktk.cuifr.models.RoutePoints;
import com.fyp.baigktk.cuifr.models.User;
import com.fyp.baigktk.cuifr.models.driveOfferModel;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;
import com.sjsu.se195.uniride.MatchedDriversActivity;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;

import org.json.JSONObject;
import org.w3c.dom.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import static java.lang.Integer.parseInt;

//import com.google.android.gms.maps.model.Marker;

public class NewPostActivity extends BaseActivity implements OnMapReadyCallback, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    Button  valueeee;
    private static final String TAG = "NewPostActivity";
    private static final String REQUIRED = "Required";
    //private static final String REQUIRED = "Required";
    int NUMBER_OF_PAGES = 5;
    CarouselView formCarousel;
    private String source_place;
    private Boolean source_check = false;
    private String destination_place;
    private boolean pickup_point_check = false;
    private Boolean destination_check = false;
    private Boolean route_present = false;
    private Boolean state_changed = false;
    private int currentPosition;
    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    // [END declare_database_ref]

    private AutocompleteSupportFragment mSourceField;
    private EditText sourceFieldWatcher;
    private AutocompleteSupportFragment mDestinationField;
    private FloatingActionButton mSubmitButton;
    private boolean postType = false; //true = driveOffer; false = rideRequest

    private EditText mpassengerCount;
    private RadioGroup genderGroup,smokingGroup,musicGroup,acGroup;
    private DriverPreferences driverPreferences=new DriverPreferences();
    private RideRequestModel rideRequestModel=new RideRequestModel();

    private LatLng mpickupPoint;
    Location location1;
    Location location2;

    private String mPostOrganizationId;

    private static final String DRIVER_TITLE = "Offer a Ride";
    private static final String RIDER_TITLE = "Request a Ride";
    int passengerCount;
    private GoogleMap m_map;
    private GMapV2Direction md;
    String hellllll;
    private TimePickerFragment starting_time = new TimePickerFragment();
    private TimePickerFragment ending_time = new TimePickerFragment();
    private boolean clickedOnArrivalTime = false;
    private Button mArriveTime;
    private Button mDepartTime;
    private int departureTime = 0;
    private int arrivalTime = 0;
    LatLng isl=new LatLng(33.6844,73.0479);

    private DatePickerFragment date = new DatePickerFragment();
    private Button pick_day;
    private int tripDate;

    private MarkerOptions set_marker;
    private MarkerOptions[] markers = new MarkerOptions[2];
    private LatLng location_latlng; //source
    private LatLng location_latlng2;    //destination
    private boolean first_time_running = false;
    List<List<HashMap<String, String>>> routePoints = new ArrayList<List<HashMap<String,String>>>() ;
    List<HashMap<String, String>> routePointsLimited = new ArrayList<>() ;


    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    public void onMapReady(GoogleMap map) {
        //mapReady = true;
        m_map = map;
//        m_map.clear();
        //      m_map.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(33.6844,73.0479) , 14.0f) );
        m_map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(33.6844,73.0479), 14.0f));

        m_map.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener);
        m_map.setOnMyLocationClickListener(onMyLocationClickListener);
        enableMyLocationIfPermitted();

        m_map.getUiSettings().setZoomControlsEnabled(true);
        m_map.setMinZoomPreference(11);

        if(this.location_latlng!=null) {
            set_marker = new MarkerOptions()
                    .position(new LatLng(this.location_latlng.latitude, this.location_latlng.longitude))
                    .title("title").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_black_48dp));
            m_map.addMarker(set_marker);
            CameraPosition target = CameraPosition.builder().target(location_latlng).zoom(14).build();
            m_map.moveCamera(CameraUpdateFactory.newCameraPosition(target));
        }


        //Check if the array positions are empty if so fill it up

        if (markers[0] == null && source_place != null) {
            markers[0] = new MarkerOptions();
        }
        if (markers[1] == null && destination_place != null) {
            markers[1] = new MarkerOptions();
        }

        //Set the latitude and longitude of the markers
        if (source_place != null) {
            markers[0].position(new LatLng(this.location_latlng.latitude, this.location_latlng.longitude))
                    .title("title").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_black_48dp));
            m_map.addMarker(markers[0]);
        }
        if (destination_place != null) {
            markers[1].position(new LatLng(this.location_latlng2.latitude, this.location_latlng2.longitude))
                    .title("title").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_black_48dp));
            m_map.addMarker(markers[1]);
        }

        if (pickup_point_check) {
            mpickupPoint = location_latlng;
            m_map.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                @Override
                public void onCameraMove() {
                    Log.d("Camera postion change" + "", m_map.getCameraPosition().target + "");
                    CameraPosition target = CameraPosition.builder().target(location_latlng).zoom(14).build();
                    mpickupPoint = m_map.getCameraPosition().target;
                }
            });
        }

        try {
            drawDirections();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (location_latlng!=null)
            setCamera();
    }

    private void drawDirections() throws ExecutionException, InterruptedException {
        md = new GMapV2Direction();
        Document doc;

        //if(source_place != null && !source_place.equals("") && destination_place != null && destination_place.equals("")) {
        if (destination_place != null) {
            doc = (Document) new GMapV2Direction().execute(location_latlng, location_latlng2).get();
            ArrayList<LatLng> directionPoint = md.getDirection(doc);
            PolylineOptions rectLine = new PolylineOptions().width(3).color(
                    Color.RED);
            Toast.makeText(this, "hello", Toast.LENGTH_SHORT);

// ... get a map.
// Add a thin red line from London to New York.
            Polyline line = m_map.addPolyline(new PolylineOptions()
                    .add(new LatLng(51.5, -0.1), new LatLng(40.7, -74.0))
                    .width(5)
                    .color(Color.RED));

            String url = getDirectionsUrl(location_latlng, location_latlng2);
            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute(url);


            if (doc != null) {
                for (int i = 0; i < directionPoint.size(); i++) {
                    rectLine.add(directionPoint.get(i));
                }
            } else {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Polyline polylin = m_map.addPolyline(rectLine);
        }

    }

    private void setCamera() {
        if (!pickup_point_check) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            if (markers[0] != null) {
                builder.include(markers[0].getPosition());
            }
            if (markers[1] != null) {
                builder.include(markers[1].getPosition());
            }
            LatLngBounds bounds = builder.build();
            int padding = 100; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            int zoomLevel = 0;
            m_map.moveCamera(cu);
            //This is only done to set the zoom for a single point at a comfortable level
            if (destination_place == null) {
                m_map.animateCamera(CameraUpdateFactory.zoomTo(15));
            }

            //CameraPosition target = CameraPosition.builder().target(location_latlng).zoom(14).build();
            //m_map.moveCamera(CameraUpdateFactory.newCameraPosition(target));
        }
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        postType = getIntent().getBooleanExtra("driveOffer", true);

        if (postType) {
            setContentView(R.layout.activity_2_drive_offer_post);
        } else {
            setContentView(R.layout.activity_2_ride_request_post);
        }
        mpassengerCount = findViewById(R.id.passengerCount);

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        mSubmitButton = (FloatingActionButton) findViewById(R.id.fab_submit_post);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();
                if (!postType)
                    getRiderData();
            }
        });

        formCarousel = (CarouselView) findViewById(R.id.carouselView);
        formCarousel.setPageCount(NUMBER_OF_PAGES);
        formCarousel.setViewListener(viewListener);
        formCarousel.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i1, float t, int i2) {
            }

            @Override
            public void onPageSelected(int position) {
                mSubmitButton.setVisibility(View.GONE);
                currentPosition = position;
                pickup_point_check = false;
                if (!Places.isInitialized()) {
                    Places.initialize(getApplicationContext(), "AIzaSyD3yaDZiY0l-kyX57r05Q0tGN_6dbjjkug");
                }
// Create a new Places client instance.
                PlacesClient placesClient = Places.createClient(NewPostActivity.this);


                if (position == 0) {
                    pickup_point_check = false;

                    mSourceField = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.field_source);
                    mSourceField.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));
                    mSourceField.setCountry("PK");
                    mSourceField.setOnPlaceSelectedListener(new PlaceSelectionListener() {

                        @Override
                        public void onPlaceSelected(@NonNull Place place) {
                            //TODO: Get info about the selected place
                            Log.i(TAG, "Place: " + place.getName());
                            source_place = place.getAddress().toString();
                            NewPostActivity.this.source_check = true;
                            state_changed = true; //Source was changed
                            //if((source_place != null && !source_place.equals(""))){
                            NewPostActivity.this.location_latlng = NewPostActivity.this.getLocationFromAddress(NewPostActivity.this, NewPostActivity.this.source_place);
                            if (NewPostActivity.this.destination_check) {
                                NewPostActivity.this.route_present = true;
                                NewPostActivity.this.destination_check = false;
                                NewPostActivity.this.location_latlng2 = NewPostActivity.this.getLocationFromAddress(NewPostActivity.this, NewPostActivity.this.destination_place);
                            } else {
                                NewPostActivity.this.route_present = false;
                            }
                            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                            mapFragment.getMapAsync(NewPostActivity.this);
                            //}
                        }

                        @Override
                        public void onError(Status status) {
                            //TODO: Handle the error
                            Log.i(TAG, "An error occured: " + status);
                        }
                    });
                    if (state_changed) {
                        state_changed = false;
                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                        mapFragment.getMapAsync(NewPostActivity.this);
                    }
                }
                if (position == 1) {
                    pickup_point_check = false;
                    mDestinationField = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.field_destination);
                    mDestinationField.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));
                    mDestinationField.setCountry("PK");

                    mDestinationField.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                        @Override
                        public void onPlaceSelected(Place place) {
                            //TODO: Get info about the selected place
                            Log.i(TAG, "Place: " + place.getName());
                            destination_place = place.getAddress().toString();
                            NewPostActivity.this.destination_check = true;
                            state_changed = true;//Destination was changed
                            //if(destination_place != null && !destination_place.equals("") && (destination_place_redraw_check == null || !destination_place.equals(destination_place_redraw_check))) {
                            NewPostActivity.this.location_latlng2 = NewPostActivity.this.getLocationFromAddress(NewPostActivity.this, NewPostActivity.this.destination_place);
                            if (NewPostActivity.this.source_check) {
                                NewPostActivity.this.route_present = true;
                                NewPostActivity.this.source_check = false;
                                NewPostActivity.this.location_latlng = NewPostActivity.this.getLocationFromAddress(NewPostActivity.this, NewPostActivity.this.source_place);
                            } else {
                                NewPostActivity.this.route_present = false;
                            }
                            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
                            mapFragment.getMapAsync(NewPostActivity.this);
                            //}
                        }

                        @Override
                        public void onError(Status status) {
                            //TODO: Handle the error
                            Log.i(TAG, "An error occured: " + status);
                        }
                    });
                    if (state_changed) {
                        state_changed = false;
                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                        mapFragment.getMapAsync(NewPostActivity.this);
                    }
                }
                if (position == 2) {
                    pickup_point_check = false;
                    if (postType) {
                        setDriverPreferences();

                    }
                    else {
                        pickup_point_check = true;
                        NewPostActivity.this.location_latlng = NewPostActivity.this.getLocationFromAddress(NewPostActivity.this, NewPostActivity.this.source_place);
                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.pickup_map);
                        mapFragment.getMapAsync(NewPostActivity.this);
                    }
                }
                if (position == 3) {
                    //this.showTimePickerDialog(R.id.)

                    //if(starting_time.gethour() != 25) || )
                    NewPostActivity.this.mDepartTime = findViewById(R.id.departTime);
                    NewPostActivity.this.mDepartTime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showTimePickerDialog(v, false);
                        }
                    });
                }
                if (position == 4) {
                    NewPostActivity.this.pick_day = findViewById(R.id.postDate);
                    NewPostActivity.this.pick_day.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDatePickerDialog(v);
                        }
                    });
                    mSubmitButton.setVisibility(View.VISIBLE);
                    mSubmitButton.invalidate();
                    //post_from = getLayoutInflater().inflate(R.layout.post_date_carousel, null);
                } else {
                    mSubmitButton.setVisibility(View.GONE);
                    mSubmitButton.invalidate();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void getRiderData() {

        rideRequestModel.setDestination(destination_place);
        rideRequestModel.setSource(source_place);
        rideRequestModel.setDate(tripDate);
        rideRequestModel.setPickUpPoint(mpickupPoint);




        final List<driveOfferModel> driveOffersList=new ArrayList<>();
        final Query driverPostRef=mDatabase.child("posts").child("driveOffers");
        final int[] cccc = new int[1];
        final String[] passenger_counter = new String[1];
        driverPostRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String riderDestination_tripDate = rideRequestModel.getDestination() + "_" + rideRequestModel.getDate();
                long passengerDestination_tripDate = rideRequestModel.getDepTIme();
                String hh = null;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String destination_tripDate = (String) ds.child("postInfo").child("destination_tripDate").getValue();
                    String time_of_trip = ds.child("postInfo").child("departureTime").getValue().toString();
                    String aaaaa = ds.child("postInfo").child("passengerCount").getValue().toString();
                    passenger_counter[0] = aaaaa;
                    // mpassengerCount = findViewById(R.id.passengerCount);


                    if (destination_tripDate.equals(riderDestination_tripDate) && (parseInt(time_of_trip) >= passengerDestination_tripDate) && (parseInt(passenger_counter[0]) >= passengerCount)) {
                        PostInfoModel postInfoModel = new PostInfoModel();
                        postInfoModel = ds.child("postInfo").getValue(PostInfoModel.class);
//         cccc[0] = Math.toIntExact((long) ds.child("postInfo").child("passengerCount").getValue());
                        List<RoutePoints> routePointsList = new ArrayList<>();
                        for (DataSnapshot dm : ds.child("routePoints").getChildren()) {
                            RoutePoints routePoints = new RoutePoints();
                            routePoints = dm.getValue(RoutePoints.class);
                            routePointsList.add(routePoints);
                            hh = ds.getKey();
                        }
                        driveOfferModel driveOfferModel = new driveOfferModel();
                        driveOfferModel.setPostInfo(postInfoModel);
                        driveOfferModel.setRoutePoints(routePointsList);

                        driveOffersList.add(driveOfferModel);
                        int vallll = parseInt(passenger_counter[0]);


                        int a = --vallll;

                        mDatabase.child("posts").child("driveOffers").child(String.valueOf(hh)).child("postInfo").child("passengerCount").setValue(a);

                    }
                    else
                    {

                 //       Toast.makeText(NewPostActivity.this,"No Offers Found",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(NewPostActivity.this,MainActivity.class));
                    }
                }


                filterUsingDistanceFunc(driveOffersList, rideRequestModel);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void filterUsingDistanceFunc(List<driveOfferModel> driveOffersList, RideRequestModel rideRequestModel) {
        List<PostInfoModel> postInfoList=new ArrayList<>();
        LatLng pickupRequestPoint=rideRequestModel.getPickUpPoint();
        LatLng nearby_RP = null;
        for(int i=0;i<driveOffersList.size();i++)
        {
            PostInfoModel pm=new PostInfoModel();
            pm=driveOffersList.get(i).getPostInfo();

            for(RoutePoints rp:driveOffersList.get(i).getRoutePoints())
            {
                LatLng rP=new LatLng(Double.valueOf(rp.getLat()),Double.valueOf(rp.getLng())) ;
                double kms=distance(pickupRequestPoint.latitude,pickupRequestPoint.longitude,rP.latitude, rP.longitude,'K');
                if(kms<1.5)
                {
                    postInfoList.add(pm);
                    nearby_RP=rP;
                    break;
                }
                int y=0;
                y++;
            }
        }

        //rP(point from which pickup is close from route)
        //postInfoList mei sari display krany wali posts hain
        Bundle args=new Bundle();
        args.putParcelable("nearbyRP",nearby_RP);
        args.putParcelable("pickupPoint",pickupRequestPoint);
        startActivity(new Intent(NewPostActivity.this, MatchedDriversActivity.class).putExtra("postList",(Serializable)postInfoList).putExtra("bundle",args));

    }


    private void setDriverPreferences() {
        mpassengerCount = findViewById(R.id.passengerCount);
        genderGroup=findViewById(R.id.gender_radioGroup);
        smokingGroup=findViewById(R.id.smoking_RadioGroup);
        musicGroup=findViewById(R.id.music_RadioGroup);
        acGroup=findViewById(R.id.aC_RadioGroup);
        int genderId=genderGroup.getCheckedRadioButtonId();
        switch(genderId){
            case R.id.gender_male:
                driverPreferences.setGender("Male");
                break;
            case R.id.gender_female:
                driverPreferences.setGender("Female");
                break;
            case R.id.gender_both:
                driverPreferences.setGender("Both");
                break;
            default:
                driverPreferences.setGender("Both");
        }
        int smokingID=smokingGroup.getCheckedRadioButtonId();
        switch(smokingID){
            case R.id.smoking_Yes:
                driverPreferences.setSmoking(true);
                break;
            case R.id.smoking_No:
                driverPreferences.setSmoking(false);
                break;
            default:
                driverPreferences.setSmoking(true);
        }
        int musicId=musicGroup.getCheckedRadioButtonId();
        switch(musicId){
            case R.id.music_Yes:
                driverPreferences.setMusic(true);
                break;
            case R.id.music_No:
                driverPreferences.setMusic(false);
                break;
            default:
                driverPreferences.setMusic(true);
        }
        int acId=acGroup.getCheckedRadioButtonId();
        switch(acId){
            case R.id.ac_Yes:
                driverPreferences.setAc(true);
                break;
            case R.id.smoking_No:
                driverPreferences.setAc(false);
                break;
            default:
                driverPreferences.setAc(true);
        }
        driverPreferences.setPassengerCount(mpassengerCount.getText().toString());

    }

    ViewListener viewListener = new ViewListener() {
        @Override
        public View setViewForPosition(int i) {
            if (!Places.isInitialized()) {
                Places.initialize(getApplicationContext(), "AIzaSyD3yaDZiY0l-kyX57r05Q0tGN_6dbjjkug");
            }
// Create a new Places client instance.
            PlacesClient placesClient = Places.createClient(NewPostActivity.this);


            View post_from = null;
            pickup_point_check = false;
            if (i == 0) {
                pickup_point_check = false;
                post_from = getLayoutInflater().inflate(R.layout.post_source_carousel, null);
                LatLng isl=new LatLng(33.6844,73.0479);
//                m_map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
//                        .getMap();
//
//        m_map.moveCamera(CameraUpdateFactory.newLatLng(isl));


                mSourceField = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.field_source);
                mSourceField.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));
                mSourceField.setCountry("PK");
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                mapFragment.getMapAsync(NewPostActivity.this);
                mSourceField.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(Place place) {
                        //TODO: Get info about the selected place
                        //    Toast.makeText(NewPostActivity.this,"hello",Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "Place: " + place.getName());
                        source_place = place.getAddress().toString();
                        // NewPostActivity.this.location_latlng = NewPostActivity.this.getLocationFromAddress(NewPostActivity.this, NewPostActivity.this.source_place);
                        location_latlng = getLocationFromAddress(NewPostActivity.this, source_place);

                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                        mapFragment.getMapAsync(NewPostActivity.this);
                    }

                    @Override
                    public void onError(Status status) {
                        //TODO: Handle the error
                        Log.i(TAG, "An error occured: " + status);
                    }
                });


            }
            if (i == 1) {
                pickup_point_check = false;
                post_from = getLayoutInflater().inflate(R.layout.post_destination_carousel, null);
                mDestinationField = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.field_destination);

                mDestinationField.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));


                mDestinationField.setCountry("PK");
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
                mapFragment.getMapAsync(NewPostActivity.this);
                mDestinationField.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(Place place) {
                        //TODO: Get info about the selected place
                        Log.i(TAG, "Place: " + place.getName());
                        destination_place = place.getAddress().toString();
                        if (destination_place != null && !destination_place.equals("")) {
                            NewPostActivity.this.location_latlng = NewPostActivity.this.getLocationFromAddress(NewPostActivity.this, NewPostActivity.this.source_place);
                            NewPostActivity.this.location_latlng2 = NewPostActivity.this.getLocationFromAddress(NewPostActivity.this, NewPostActivity.this.destination_place);



                            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
                            mapFragment.getMapAsync(NewPostActivity.this);
                        }
                    }

                    @Override
                    public void onError(Status status) {
                        //TODO: Handle the error
                        Log.i(TAG, "An error occured: " + status);
                    }
                });
            }
            if (i == 2) {
                if (postType) {
                    post_from = getLayoutInflater().inflate(R.layout.post_passengercount_carousel, null);
                    mpassengerCount = (EditText) post_from.findViewById(R.id.passengerCount);
                    hellllll=mpassengerCount.getText().toString();
                } else {
                    pickup_point_check = false;
                    post_from = getLayoutInflater().inflate(R.layout.post_pickuppoint_carousel, null);
                    if (source_place != null) {
                        NewPostActivity.this.location_latlng = NewPostActivity.this.getLocationFromAddress(NewPostActivity.this, NewPostActivity.this.source_place);
                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.pickup_map);
                        mapFragment.getMapAsync(NewPostActivity.this);
                    }
                }
                mSubmitButton.setVisibility(View.VISIBLE);
                mSubmitButton.invalidate();
            }
            if (i == 3) {

                post_from = getLayoutInflater().inflate(R.layout.post_time_carousel, null);

//                Location location1 = new Location("");
//                location1.setLatitude(location_latlng.latitude);
//                location1.setLongitude(location_latlng.longitude);
//
//                Location location2 = new Location("");
//                location2.setLatitude(location_latlng2.latitude);
//                location2.setLongitude(location_latlng2.longitude);
//                float distanceInMeters = location1.distanceTo(location2);
//                int speedIs10MetersPerMinute = 10;
//                float estimatedDriveTimeInMinutes = distanceInMeters / speedIs10MetersPerMinute;
//

            }

            if (i == 4) {


                post_from = getLayoutInflater().inflate(R.layout.post_date_carousel, null);




            }
            else {
                mSubmitButton.setVisibility(View.GONE);
                mSubmitButton.invalidate();
            }
            // NewPostActivity.this.setTitle((TextView) post_from.findViewById(R.id.carousel_title));
            return post_from;
        }
    };



    private void setTitle(TextView tv) {
        if (postType) tv.setText(DRIVER_TITLE);
        else tv.setText(RIDER_TITLE);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String time;
        if (hourOfDay > 12) {
            time = (hourOfDay - 12) + ":" + minute + "PM";
        } else {
            time = (hourOfDay) + ":" + minute + "AM";
        }

        if (clickedOnArrivalTime) {
            this.mArriveTime.setText(time);
            arrivalTime = (hourOfDay * 100) + (minute); //Format is H = hours, M = minutes: ->HHMM
        } else {
            this.mDepartTime.setText(time);
            departureTime = (hourOfDay * 100) + (minute); //Format is H = hours, M = minutes: ->HHMM
        }
        rideRequestModel.setDepTIme(departureTime);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        System.out.println("onDateSet: " + year);

        String picked_date = month + " " + day + ", " + year;
        this.pick_day.setText(picked_date);
        tripDate = (year * 10000) + (month * 100) + (day); //date format is yearMonthDay (YYYYMMDD)
    }

    //This function gets called to make the dialog for time picker
    public void showTimePickerDialog(View v, Boolean arrival_time) {
        //arrivalTime = True : arrival time button, else departure time button
        if (arrival_time) {
            clickedOnArrivalTime = true;
            starting_time.show(getFragmentManager(), "timePicker");
        } else {
            clickedOnArrivalTime = false;
            ending_time.show(getFragmentManager(), "timePicker");
        }
    }

    //This function gets called to make the date picker dialog
    public void showDatePickerDialog(View v) {

        date.show(getFragmentManager(), "datePicker");
    }

    private void submitPost() {
        final String source = source_place;
        final String destination = destination_place;

        //LatLng pickupPoint_temp;
        int passengerCount_temp = 0;
        if (postType && !mpassengerCount.getText().toString().equals("")) {
            passengerCount_temp = parseInt(mpassengerCount.getText().toString());
        } else if (postType && mpassengerCount.getText().toString().equals("")) {
            mpassengerCount.setError(REQUIRED);
            return;
        } else {
            //TODO: do something with mpickuppoint ??
        }

        passengerCount = passengerCount_temp;
        final LatLng pickupPoint = mpickupPoint;

        //if drive offer post and passenger count empty
        if (postType && passengerCount_temp == 0) {
            mpassengerCount.setError("Must be greater than 0.");
            return;
        }

        // TODO: set mPostOrganizationId here?

        //TODO: probably not needed
        //if ride request post and pickup point empty
        /*if (!postType && TextUtils.isEmpty(pickupPoint_temp)) {
            mpickupPoint.setError(REQUIRED);
            return;
        }*/

        // Title is required
        if (TextUtils.isEmpty(source)) {
            //mSourceField.setError(REQUIRED);
            return;
        }

        // Body is required
        if (TextUtils.isEmpty(destination)) {
            //mDestinationField.setError(REQUIRED);
            return;
        }

        // Disable button so there are no multi-posts
        setEditingEnabled(false);
        Toast.makeText(this, "Posting...", Toast.LENGTH_SHORT).show();

        // [START single_value_read]
        final String userId = getUid();
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);

                        // [START_EXCLUDE]
                        if (user == null) {
                            // User is null, error out
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(NewPostActivity.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {

                            mPostOrganizationId = user.defaultOrganizationId; // TODO: Let user choose which org to post under.

                            System.out.println("Saving a new Post with orgId = " + user.defaultOrganizationId);

                            // Write new post
                            if (postType) {
                                writeNewDriveOfferPost(userId, UserInformation.getShortName(user),
                                        source, destination, passengerCount, departureTime, arrivalTime, tripDate, mPostOrganizationId);
                            } else {
                                writeNewRideRequestPost(userId, UserInformation.getShortName(user),
                                        source, destination, pickupPoint, departureTime, arrivalTime, tripDate, mPostOrganizationId);
                            }
                        }

                        // Finish this Activity, back to the stream
                        setEditingEnabled(true);
                        finish();
                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        // [START_EXCLUDE]
                        setEditingEnabled(true);
                        // [END_EXCLUDE]
                    }
                });
        // [END single_value_read]
    }

    private void setEditingEnabled(boolean enabled) {
        //mSourceField.setEnabled(enabled);
        //mDestinationField.setEnabled(enabled);
        if (enabled) {
            mSubmitButton.setVisibility(View.VISIBLE);
        } else {
            mSubmitButton.setVisibility(View.GONE);
        }
    }

    // [START write_fan_out]

    //creating a drive offer
    private void writeNewDriveOfferPost(String userId, String username, String source, String destination, int count,



                                        int dep_time, int arr_time, int t_day, String organizationId) {

        Location location1 = new Location("");
        location1.setLatitude(location_latlng.latitude);
        location1.setLongitude(location_latlng.longitude);
        float vvvv= (float) SphericalUtil.computeDistanceBetween(location_latlng,location_latlng2);

        Location location2 = new Location("");
        location2.setLatitude(location_latlng2.latitude);
        location2.setLongitude(location_latlng2.longitude);
        float distanceInMeters = location1.distanceTo(location2);
        int speedIs10MetersPerMinute = 10;
        float estimatedDriveTimeInMinutes = (distanceInMeters / speedIs10MetersPerMinute)/15;
        float vallll= (float) CalculationByDistance(location_latlng,location_latlng2);
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("posts").child("driveOffers").push().getKey();
        final DatabaseReference driverPostRef=mDatabase.child("posts").child("driveOffers").child(key);

        final PostInfoModel postInfoModel=new PostInfoModel();

        String UserKey= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference carMilageRef=mDatabase.child("users").child(UserKey).child("carMilage");

        postInfoModel.setUserKey(UserKey);
        postInfoModel.setArrivalTime((long)estimatedDriveTimeInMinutes);
        postInfoModel.setAuthor(username);
        postInfoModel.setDepartureTime(dep_time);
        postInfoModel.setDestination(destination);
        postInfoModel.setDriverPreferences(driverPreferences);
        postInfoModel.setDestination_tripDate(destination+"_"+t_day);
        postInfoModel.setPassengerCount(Long.valueOf(mpassengerCount.getText().toString()));
        postInfoModel.setSource(source);
        postInfoModel.setTripDate(t_day);
        carMilageRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long car_milage=(long)dataSnapshot.getValue();
                postInfoModel.setCarMilage(car_milage);
                driverPostRef.child("postInfo").setValue(postInfoModel);
                driverPostRef.child("postInfo").child("driverPreferences").setValue(driverPreferences);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        DriverOfferPost driverPost = new DriverOfferPost(userId, username, source, destination, count, dep_time, arr_time, t_day);
        driverPost.organizationId = organizationId;
        driverPost.postId = key;

        Map<String, Object> postValues = driverPost.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/driveOffers/" + key, postValues);
        childUpdates.put("/user-posts/" + userId + "/driveOffers/" + key, postValues);
        childUpdates.put("/organization-posts/" + organizationId + "/driveOffers/" + key, postValues);

        for (int j=0;j<routePoints.get(0).size();j+=10)
        {
            routePointsLimited.add(routePoints.get(0).get(j));
        }
        for (int i=0; i<routePointsLimited.size();i++)///setting routepoints of the route
        {
            driverPostRef.child("routePoints").push().setValue(routePointsLimited.get(i));
        }
        addNotification();
    }

    //creating a ride request
    private void writeNewRideRequestPost(String userId, String username, String source, String destination, LatLng pickupPoint,
                                         int dep_time, int arr_time, int t_day, String organizationId) {
        // Create new post at /user-posts/$userid/$postid and at
        System.out.println("--> in writeNewRideRequestPost.");
        // /posts/$postid simultaneously
        String key = mDatabase.child("posts").child("rideRequests").push().getKey();

        RideRequestPost rideRequest = new RideRequestPost(userId, username, source, destination, dep_time, arr_time, t_day);
        rideRequest.organizationId = organizationId;
        rideRequest.postId = key;

        Map<String, Object> postValues = rideRequest.toMap();
        RideRequestPost rideRequest_pickupPoint = new RideRequestPost(pickupPoint);
        Map<String, Object> postValuesPickupPoint = rideRequest_pickupPoint.toMap_pickupPoint();

        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> childUpdates2 = new HashMap<>();
        childUpdates.put("/posts/rideRequests/" + key, postValues);

        childUpdates.put("/user-posts/" + userId + "/rideRequests/" + key, postValues);
        childUpdates.put("/organization-posts/" + organizationId + "/rideRequests/" + key, postValues);
        mDatabase.updateChildren(childUpdates);

//        childUpdates2.put("/posts/rideRequests/" + key + "/pickup-point/", postValuesPickupPoint);
//        childUpdates2.put("/user-posts/" + userId + "/rideRequests/" + key + "/pickup-point/", postValuesPickupPoint);
        mDatabase.updateChildren(childUpdates2);
    }

    //    public void drawlinedirection()
//    {
//
//
//        Paint   mPaint = new Paint();
//        mPaint.setDither(true);
//        mPaint.setColor(Color.RED);
//        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
//        mPaint.setStrokeJoin(Paint.Join.ROUND);
//        mPaint.setStrokeCap(Paint.Cap.ROUND);
//        mPaint.setStrokeWidth(2);
//
//        GeoPoint gP1 = new GeoPoint(19240000,-99120000);
//        GeoPoint gP2 = new GeoPoint(37423157, -122085008);
//
//        Point p1 = new Point();
//        Point p2 = new Point();
//        Path path = new Path();
//
//        Projection projection=mapv.getProjection();
//        projection.toPixels(gP1, p1);
//        projection.toPixels(gP2, p2);
//
//        path.moveTo(p2.x, p2.y);
//        path.lineTo(p1.x,p1.y);
//
//        canvas.drawPath(path, mPaint);
//
//    }
    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Key
        String key = "key=" + "AIzaSyAZJn-XFJo3OVHMP_sSPcsxt7eL_sF47HI";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }
    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Except downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
                routePoints=routes;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
            String distance = "";
            String duration = "";

            if (result.size() < 1) {
                Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    if (j == 0) {    // Get distance from the list
                        distance = (String) point.get("distance");
                        continue;
                    } else if (j == 1) { // Get duration from the list
                        duration = (String) point.get("duration");
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(2);
                lineOptions.color(Color.RED);
            }

            //  tvDistanceDuration.setText("Distance:" + distance + ", Duration:" + duration);

            // Drawing polyline in the Google Map for the i-th route
            m_map.addPolyline(lineOptions);
        }
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


    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }

    private void addNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(NewPostActivity.this)
                        .setSmallIcon(R.drawable.ic_date_range_white_48dp)
                        .setContentTitle("New Offer Added")
                        .setContentText("Click to heck it");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {//Can add more as per requirement
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);
        }
    }

    private void enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else if (m_map != null) {
            m_map.setMyLocationEnabled(true);
        }
    }

    private void showDefaultLocation() {
        Toast.makeText(this, "Location permission not granted, " +
                        "showing default location",
                Toast.LENGTH_SHORT).show();
        LatLng redmond = new LatLng(33.6844, 73.0479);
        m_map.moveCamera(CameraUpdateFactory.newLatLng(redmond));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocationIfPermitted();
                } else {
                    showDefaultLocation();
                }
                return;
            }

        }
    }

    private GoogleMap.OnMyLocationButtonClickListener onMyLocationButtonClickListener =
            new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    m_map.setMinZoomPreference(15);
                    return false;
                }
            };

    private GoogleMap.OnMyLocationClickListener onMyLocationClickListener =
            new GoogleMap.OnMyLocationClickListener() {
                @Override
                public void onMyLocationClick(@NonNull Location location) {

                    m_map.setMinZoomPreference(12);

                    CircleOptions circleOptions = new CircleOptions();
                    circleOptions.center(new LatLng(location.getLatitude(),
                            location.getLongitude()));

                    circleOptions.radius(200);
                    circleOptions.fillColor(Color.RED);
                    circleOptions.strokeWidth(6);

                    m_map.addCircle(circleOptions);
                }
            };

}