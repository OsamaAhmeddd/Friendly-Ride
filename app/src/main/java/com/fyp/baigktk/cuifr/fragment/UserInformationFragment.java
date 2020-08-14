package com.fyp.baigktk.cuifr.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fyp.baigktk.cuifr.models.User;
import com.fyp.baigktk.cuifr.models.UserModel;
import com.fyp.baigktk.cuifr.models.UserModelRecieve;
import com.google.firebase.auth.FirebaseAuth;
import com.fyp.baigktk.cuifr.AddUserInformation;
import com.fyp.baigktk.cuifr.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class UserInformationFragment extends Fragment {

    private String ABOUT_TAB_TITLE;
    private TextView mEmail,mGender,mCarMilage,mRating;
    private TextView mTabTitle;
    private ImageButton mEditProfile;

    UserModel userModel;

    public UserInformationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        String firstName = getArguments().getString("userName");
        String uID = getArguments().getString("uID");
        ABOUT_TAB_TITLE = "About " + firstName;
        View rootView = inflater.inflate(R.layout.fragment_user_information, container, false);

        mCarMilage=rootView.findViewById(R.id.profile_user_information_milage);
        mEmail=rootView.findViewById(R.id.profile_user_information_email);
        mGender=rootView.findViewById(R.id.profile_user_information_gender);
        mRating=rootView.findViewById(R.id.profile_user_information_Rating);

        getandSetUserInfo();

        mTabTitle = (TextView) rootView.findViewById(R.id.user_info_title);
        mTabTitle.setText(ABOUT_TAB_TITLE);

        if(uID == getUid()){
            mEditProfile = rootView.findViewById(R.id.profile_user_information_edit);
            mEditProfile.setVisibility(View.VISIBLE);
            mEditProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), AddUserInformation.class));
                    getActivity().finish();
                }
            });
        }

        //createTitle(ABOUT_TAB_TITLE);
        return rootView;
    }

    public void getandSetUserInfo()
    {
        String UserKey=FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference profRef= FirebaseDatabase.getInstance().getReference().child("users").child(UserKey);
        profRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             //   Toast.makeText(getContext(),"bcbcbcbc",Toast.LENGTH_SHORT).show();

                userModel=dataSnapshot.getValue(UserModel.class);
                mEmail.setText(userModel.getEmail());
                mCarMilage.setText(userModel.getCarMilage()+" km/L");
                mGender.setText(userModel.getGender());
                if(userModel.getRating()!=null)
                {
                    mRating.setText("Rating: "+userModel.getRating().getCurrentRating());
                }
                else
                {
                    mRating.setText("Rating: This user has not been rated yet");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

}
