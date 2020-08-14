package com.fyp.baigktk.cuifr.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.fyp.baigktk.cuifr.ChatActivity;
import com.fyp.baigktk.cuifr.R;
import com.fyp.baigktk.cuifr.models.UserModelRecieve;
import com.fyp.baigktk.cuifr.models.driveMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class AcceptedRidesFragments extends Fragment {

    RecyclerView recyclerView;


    FirebaseRecyclerAdapter<UserModelRecieve, UserViewHolder> mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_accepted_rides, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializations();
        loadRecycler();


    }

    private void loadRecycler() {
        String myRef = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Query acceptedRideRef = FirebaseDatabase.getInstance().getReference().child("acceptedRides").child(myRef);
        FirebaseRecyclerOptions<UserModelRecieve> options =
                new FirebaseRecyclerOptions.Builder<UserModelRecieve>()
                        .setQuery(acceptedRideRef, UserModelRecieve.class)
                        .build();

        mAdapter = new FirebaseRecyclerAdapter<UserModelRecieve, UserViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final UserViewHolder holder, final int position, @NonNull final UserModelRecieve model) {

                holder.setDetails(model.getFirstName() + " " + model.getLastName(), model.getEmail(), model.getFareAmount(),model.getSource(),model.getDestination());
                holder.chatBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String myRef = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        String keyyyyy = model.getRiderKey() + myRef;

                        startActivity(new Intent(getContext(), ChatActivity.class).putExtra("string", keyyyyy));
                    }
                });

                holder.complete_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        makeNodesForAdmin(model);
                        addRideCompleteBoolean(model);
                        mAdapter.getRef(position).removeValue();

                        // updateUserModel

                    }
                });
            }

            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_accepted_ride, parent, false);
                return new UserViewHolder(view);
            }
        };
        mAdapter.startListening();
        recyclerView.setAdapter(mAdapter);
    }

    private void addRideCompleteBoolean(UserModelRecieve model) {
        String driverKey=FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference rideComplete=FirebaseDatabase.getInstance().getReference().child("completedRides").child(driverKey).child(model.getRiderKey());
        rideComplete.child("rideComplete").setValue("yes");
    }


    private void makeNodesForAdmin(UserModelRecieve model) {
        String driverRef = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //accepted rides node ref
        DatabaseReference acceptedRides = FirebaseDatabase.getInstance().getReference().child("acceptedRides").child(driverRef);
        Query query = acceptedRides.orderByChild("riderKey").equalTo(model.getRiderKey());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                DatabaseReference adminDriverRef = FirebaseDatabase.getInstance().getReference().child("adminDriverRef").push();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    UserModelRecieve um;
                    um = ds.getValue(UserModelRecieve.class);
                    adminDriverRef.setValue(um);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //acceptedRidesforRider node ref
        DatabaseReference acceptedRidesforRider = FirebaseDatabase.getInstance().getReference().child("acceptedRidesforRider").child(model.getRiderKey());
        Query query2 = acceptedRidesforRider.orderByChild("uid").equalTo(driverRef);
        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DatabaseReference adminDriverRef = FirebaseDatabase.getInstance().getReference().child("adminRiderRef").push();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    driveMap dm = ds.getValue(driveMap.class);
                    adminDriverRef.setValue(dm);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    private void initializations() {
        recyclerView = getView().findViewById(R.id.accepted_rides_fragment);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    public static class UserViewHolder extends RecyclerView.ViewHolder {

        View mView;
        private TextView name;
        private TextView email;
        private TextView fare;
        private TextView source;
        private TextView destination;

        private Button chatBtn, complete_btn;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDetails(String Name, String Email, double Fare,String Source,String Destination) {

            name = itemView.findViewById(R.id.accepted_rides_name);
            email = itemView.findViewById(R.id.accepted_rides_Email);
            fare = itemView.findViewById(R.id.accepted_rides_fare);
            chatBtn = itemView.findViewById(R.id.accepted_rides_btn_chat);
            complete_btn = itemView.findViewById(R.id.complete_rides_btn_chat);
            source=itemView.findViewById(R.id.accepted_rides_source);
            destination=itemView.findViewById(R.id.accepted_rides_destination);


            source.setText("From: "+Source);
            destination.setText("To: "+Destination);
            name.setText("Name: " + Name);
            email.setText("Email: " + Email);
            fare.setText("Fare: " + Fare);

            complete_btn.setText("Complete");

        }

    }
}
