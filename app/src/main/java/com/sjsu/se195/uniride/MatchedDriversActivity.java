package com.sjsu.se195.uniride;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fyp.baigktk.cuifr.R;
import com.fyp.baigktk.cuifr.models.PostInfoModel;
import com.fyp.baigktk.cuifr.viewholder.PostListRecyclerAdapter;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MatchedDriversActivity extends AppCompatActivity implements PostListRecyclerAdapter.OnClickListenerr {
    private List<PostInfoModel> postInfoList=new ArrayList<>();
    private RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    Bundle b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matched_drivers);
        Intent i=getIntent();
        b=getIntent().getParcelableExtra("bundle");
        LatLng nearbyRP=b.getParcelable("nearbyRP");
        LatLng pickupPoint=b.getParcelable("pickupPoint");
        postInfoList=(List<PostInfoModel>) i.getSerializableExtra("postList");
        intializations();

    }

    private void intializations() {
        recyclerView=findViewById(R.id.matched_Drivers_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new PostListRecyclerAdapter(postInfoList,this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onItemClick(int position) {
        PostInfoModel pm= postInfoList.get(position);
        Intent intent=new Intent(MatchedDriversActivity.this,DriverInfoActivity.class);
        intent.putExtra("driverInfo",(Serializable)pm);
        intent.putExtra("bundle",b);
        startActivity(intent);
    }
}
