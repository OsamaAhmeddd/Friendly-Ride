package com.fyp.baigktk.cuifr.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.fyp.baigktk.cuifr.PostInfo;
import com.fyp.baigktk.cuifr.R;
import com.fyp.baigktk.cuifr.models.Post;
import com.fyp.baigktk.cuifr.models.PostInfoModel;
import com.fyp.baigktk.cuifr.models.RoutePoints;
import com.fyp.baigktk.cuifr.models.UserModelRecieve;
import com.fyp.baigktk.cuifr.models.driveOfferModel;
import com.fyp.baigktk.cuifr.viewholder.PostListRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sjsu.se195.uniride.RiderDetailsActivity;

import java.util.ArrayList;
import java.util.List;


public class MyDriveOffersFragment extends Fragment implements PostListRecyclerAdapter.OnClickListenerr {

    RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private String DriverKey=FirebaseAuth.getInstance().getCurrentUser().getUid();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_drive_offers,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializations();
        loadDataFromFirebase();

    }

    private void loadDataFromFirebase() {
        String DriverKey= FirebaseAuth.getInstance().getCurrentUser().getUid();
        final List<PostInfoModel> postInfoList =new ArrayList<>();
        DatabaseReference postInfoRef=FirebaseDatabase.getInstance().getReference().child("posts").child("driveOffers");

        postInfoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        PostInfoModel postInfoModel = new PostInfoModel();
                        postInfoModel = ds.child("postInfo").getValue(PostInfoModel.class);
                        postInfoList.add(postInfoModel);
                }
                filterMyPosts(postInfoList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



      /*  Query query= FirebaseDatabase.getInstance().getReference().child("posts").child("driveOffers");
        FirebaseRecyclerOptions<PostInfoModel> options =
                new FirebaseRecyclerOptions.Builder<PostInfoModel>()
                        .setQuery(query, PostInfoModel.class)
                        .build();
        mAdapter=new FirebaseRecyclerAdapter<PostInfoModel, UserViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull PostInfoModel model) {
                holder.setDetails(model);
            }

            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_matched_post, parent, false);
                return new UserViewHolder(view);
            }
        };
        mAdapter.startListening();*/

    }

    private void filterMyPosts(List<PostInfoModel> postInfoList1) {
        List<PostInfoModel> postInfoList2 =new ArrayList<>();

        for(int i=0;i<postInfoList1.size();i++)
        {
            if (postInfoList1.get(i).getUserKey().equals(DriverKey))
            {
                postInfoList2.add(postInfoList1.get(i));
            }
        }

        mAdapter=new PostListRecyclerAdapter(postInfoList2,this);
        recyclerView.setAdapter(mAdapter);
    }



    private void initializations() {
        recyclerView=getView().findViewById(R.id.my_drive_offers_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onItemClick(int position) {

    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        View mView;
        private TextView sourceView;
        private TextView authorView;
        private TextView destinationView;
        private TextView passengerCountTextView;
        private TextView car_milage;


        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDetails(PostInfoModel post) {
            sourceView = (TextView) itemView.findViewById(R.id.post_source);
            authorView = (TextView) itemView.findViewById(R.id.post_cardview_author_name);
            destinationView = (TextView) itemView.findViewById(R.id.post_destination);
            car_milage=itemView.findViewById(R.id.matched_item_car_Milage);
            passengerCountTextView = itemView.findViewById(R.id.post_passenger_count);


            sourceView.setText("From: \n"+post.getSource());
            authorView.setText(post.getAuthor());
            destinationView.setText("To: \n"+post.getDestination());
            car_milage.setText("Car Milage:" +post.getCarMilage());
            passengerCountTextView.setText("Total Passengers: "+post.getPassengerCount());
        }

    }
}
