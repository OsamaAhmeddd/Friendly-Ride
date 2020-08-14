package com.fyp.baigktk.cuifr;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.fyp.baigktk.cuifr.models.Post;
import com.fyp.baigktk.cuifr.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class  MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    private ImageButton mSignOut;
    private User user;
    private DatabaseReference mDatabase;
    Button driverButton,riderButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();
        setContentView(R.layout.activity_0_main);
        this.getUser(getUid());
        mSignOut = (ImageButton) this.findViewById(R.id.profile_page_sign_out);
        mSignOut.setVisibility(View.GONE);
        driverButton =findViewById(R.id.driver_mode_button);
        riderButton=findViewById(R.id.rider_mode_button);
        if(this.user == null){
            // mSignOut.setVisibility(View.VISIBLE);
            mSignOut.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this, SignInActivity.class));
                    finish();
                }
            });
        }


        mDatabase = FirebaseDatabase.getInstance().getReference();
        String riderKey=FirebaseAuth.getInstance().getCurrentUser().getUid();
        final long[] val = new long[1];
    mDatabase.child("users").child(riderKey).child("carMilage").addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             val[0] =(long)dataSnapshot.getValue();
            if(val[0] ==0) {
                driverButton.setVisibility(View.GONE);
            }


        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });


        findViewById(R.id.driver_mode_button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, MainSubcategoryActivity.class);
                intent.putExtra(MainSubcategoryActivity.EXTRA_POST_TYPE_TO_SHOW, Post.PostType.RIDER.name());
                startActivity(intent);
            }
        });

        // "I am a passenger" -> Show me Drive Offers (including Carpools):
        findViewById(R.id.rider_mode_button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, MainSubcategoryActivity.class);
                intent.putExtra(MainSubcategoryActivity.EXTRA_POST_TYPE_TO_SHOW, Post.PostType.DRIVER.name());
                startActivity(intent);
            }
        });
        pushTokenToFirebase();
        setNavBar(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return true;
//        } else if (i == R.id.action_show_organizations) {
//            startActivity(new Intent(this, ShowOrganizationsActivity.class));
//            finish();
//            return true;
        } else if (i == R.id.edit_profile){
            startActivity(new Intent(this, AddUserInformation.class));
            finish();
            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void getUser(String uid) {
          //   Toast.makeText(MainActivity.this,"hello dear",Toast.LENGTH_SHORT).show();

        // System.out.println("Starting to set user....");
        try {
            mDatabase.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Need to get the user object before loading posts because the query to find posts requires user.

                    // Get User object and use the values to update the UI
                    user = dataSnapshot.getValue(User.class);

                       Toast.makeText(MainActivity.this,"hello dear",Toast.LENGTH_SHORT).show();


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



        }
        catch (NullPointerException e){
            System.out.println("Null user");
            this.user = null;
        }
        //getUid()

    }

    //This method will push this Firebasetoken online so that
    //  the cloud functions may use it.
    public void pushTokenToFirebase(){


        Map<String, Object> childUpdates = new HashMap<>();
        String instance_id = FirebaseInstanceId.getInstance().getId();
        String instance_token = FirebaseInstanceId.getInstance().getToken();

        System.out.println(getUid());
        System.out.println(FirebaseInstanceId.getInstance().getId());
        System.out.println(instance_token);

        childUpdates.put("/users/"+getUid()+"/firebase_instance_id/", instance_id);
        childUpdates.put("/users/"+getUid()+"/firebase_instance_token/", instance_token);
        mDatabase.updateChildren(childUpdates);
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

    public void message(View view) {
        startActivity(new Intent(this,ChatActivity.class));
    }
}
