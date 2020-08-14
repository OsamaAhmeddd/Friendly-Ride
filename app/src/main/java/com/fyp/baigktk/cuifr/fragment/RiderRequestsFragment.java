package com.fyp.baigktk.cuifr.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.fyp.baigktk.cuifr.R;
import com.fyp.baigktk.cuifr.models.UserModelRecieve;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.sjsu.se195.uniride.RiderDetailsActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class RiderRequestsFragment extends Fragment {

    RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<UserModelRecieve, UserViewHolder> mAdapter;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rider_requests,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializations();
        loadDataFromFirebase();

    }

    private void loadDataFromFirebase() {
        String DriverKey= FirebaseAuth.getInstance().getCurrentUser().getUid();

        Query query= FirebaseDatabase.getInstance().getReference().child("rideRequest").child(DriverKey);
        FirebaseRecyclerOptions<UserModelRecieve> options =
                new FirebaseRecyclerOptions.Builder<UserModelRecieve>()
                        .setQuery(query, UserModelRecieve.class)
                        .build();
        mAdapter=new FirebaseRecyclerAdapter<UserModelRecieve, UserViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, final int position, @NonNull final UserModelRecieve model) {
                holder.setDetails(model.getFirstName()+" "+model.getLastName(),model.getEmail(),model.getFareAmount());
              holder.mDetailsBtn.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {


                      startActivity(new Intent(getActivity(), RiderDetailsActivity.class).putExtra("model",model));
                  }
              });

            }

            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_rider_requests, parent, false);
                return new UserViewHolder(view);            }
        };

        mAdapter.startListening();
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter!=null)
            mAdapter.stopListening();
    }

    private void initializations() {
        recyclerView=getView().findViewById(R.id.fragment_rider_requests_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
    public static class UserViewHolder extends RecyclerView.ViewHolder {

        View mView;
        Button mDetailsBtn;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDetails(String rider_name, String rider_email,double rider_Fare) {
            TextView name = mView.findViewById(R.id.item_rider_request_name);
            TextView email = mView.findViewById(R.id.item_rider_request_email);
            TextView Fare = mView.findViewById(R.id.item_rider_request_Fare);
            mDetailsBtn=mView.findViewById(R.id.item_rider_request_details_Btn);


            name.setText(rider_name);
            email.setText(rider_email);
            Fare.setText(String.valueOf(rider_Fare));
        }

    }
}
