package com.fyp.baigktk.cuifr.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.fyp.baigktk.cuifr.ChatActivity;
import com.fyp.baigktk.cuifr.R;
import com.fyp.baigktk.cuifr.models.User;
import com.fyp.baigktk.cuifr.models.UserModel;
import com.fyp.baigktk.cuifr.models.UserModelRecieve;
import com.fyp.baigktk.cuifr.models.driveMap;
import com.fyp.baigktk.cuifr.models.ratingModel;
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

import java.util.HashMap;
import java.util.Map;

public class UserView extends Fragment {
    private DatabaseReference mFirebaseDatabaseReference;

    RecyclerView recyclerView;
    long val;
    FirebaseRecyclerAdapter<driveMap, viewHolder> mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmnet_user_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializations();
        loadRecycler();

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

    }

    private void loadRecycler() {
        final String myRef = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Query acceptedRideRef = FirebaseDatabase.getInstance().getReference().child("acceptedRidesforRider").child(myRef);
        FirebaseRecyclerOptions<driveMap> options =
                new FirebaseRecyclerOptions.Builder<driveMap>()
                        .setQuery(acceptedRideRef, driveMap.class)
                        .build();
        mAdapter = new FirebaseRecyclerAdapter<driveMap, viewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final viewHolder holder, int position, @NonNull final driveMap model) {
                holder.setDetails(model.getEmail());
                holder.chatButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Chat Functiality
                    String keyyy=myRef+model.getUid();

                        startActivity(new Intent(getContext(), ChatActivity.class).putExtra("string",keyyy));

                    }
                });


                holder.completeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    ratingDialogBox(model);



                    }
                });
            }

            @NonNull
            @Override
            public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_driver_info_map, parent, false);
                return new viewHolder(view);
            }
        };


        mAdapter.startListening();
        recyclerView.setAdapter(mAdapter);
    }

    private void removeNodes(driveMap model) {
        String riderKey=FirebaseAuth.getInstance().getCurrentUser().getUid();
        String messageKey=riderKey+model.getUid();
        String driverKey=model.getUid();


        //removing from accepted rides node
        DatabaseReference acceptedRides=FirebaseDatabase.getInstance().getReference().child("acceptedRides").child(model.getUid());
        Query query=acceptedRides.orderByChild("riderKey").equalTo(riderKey);
        query.getRef().removeValue();

        //removing from acceptedRidesforRider
        DatabaseReference acceptedRidesforRider=FirebaseDatabase.getInstance().getReference().child("acceptedRidesforRider").child(riderKey);
        Query query2=acceptedRidesforRider.orderByChild("uid").equalTo(model.getUid());
        query2.getRef().removeValue();

        //removing respective chats
        DatabaseReference messageRef=FirebaseDatabase.getInstance().getReference().child("messages").child(messageKey);
        messageRef.removeValue();

        //removing completed rides node
        DatabaseReference rideComplete=FirebaseDatabase.getInstance().getReference().child("completedRides").child(driverKey).child(riderKey);
        rideComplete.removeValue();


    }

    private void ratingDialogBox(final driveMap model) {
        String driverKey=model.getUid();
        String riderKey=FirebaseAuth.getInstance().getCurrentUser().getUid();



        DatabaseReference rideComplete=FirebaseDatabase.getInstance().getReference().child("completedRides").child(driverKey).child(riderKey).child("rideComplete");

        final Dialog rankDialog;
        final RatingBar ratingBar;
        rankDialog = new Dialog(getContext(), R.style.FullHeightDialog);
        rankDialog.setContentView(R.layout.rating_bar);
        rankDialog.setCancelable(true);
        ratingBar = (RatingBar)rankDialog.findViewById(R.id.dialog_ratingbar);



        //  ratingBar.setRating(userRankValue);
        final float[] ratingValue = new float[1];
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                ratingValue[0] =rating;

            }
        });


        final Button updateButton = (Button) rankDialog.findViewById(R.id.rank_dialog_button);

        rideComplete.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    updateButton.setEnabled(true);
                }
                else
                {
                    updateButton.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateRatingInFirebase(model,ratingValue[0]);
                removeNodes(model);
                rankDialog.dismiss();
            }
        });
        //now that the dialog is set up, it's time to show it
        rankDialog.show();
    }

    private void updateRatingInFirebase(driveMap model, final float dialogRatingCurrent) {

        final DatabaseReference ratingRef = FirebaseDatabase.getInstance().getReference().child("users").child(model.getUid()).child("rating");
        ratingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists())
                {
                    ratingModel rm=new ratingModel();
                    rm.setCurrentRating((long) dialogRatingCurrent);
                    rm.setCount(1);
                    ratingRef.setValue(rm);
                }
                else
                {
                    ratingModel rm=new ratingModel();
                    rm=dataSnapshot.getValue(ratingModel.class);
                    rm.setCount(rm.getCount()+1);
                    float newRating= ((float)rm.getCurrentRating()+dialogRatingCurrent)/rm.getCount();
                    rm.setCurrentRating((long) newRating);
                    ratingRef.setValue(rm);
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
        recyclerView = getView().findViewById(R.id.user_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        View mView;
        private TextView email;

        private Button completeBtn, chatButton;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

        }


        public void setDetails(String Email) {
            chatButton = mView.findViewById(R.id.driver_info_map_message_button);
            email = mView.findViewById(R.id.driver_info_map_email);
            completeBtn = mView.findViewById(R.id.driver_info_map_complete_button);
            email.setText(Email);


        }
    }
}