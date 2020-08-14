package com.fyp.baigktk.cuifr.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.fyp.baigktk.cuifr.MainSubcategoryActivity;
import com.fyp.baigktk.cuifr.PostDetailActivity;
import com.fyp.baigktk.cuifr.R;
import com.fyp.baigktk.cuifr.UserInformation;
import com.fyp.baigktk.cuifr.models.Post;
import com.fyp.baigktk.cuifr.models.User;
import com.fyp.baigktk.cuifr.viewholder.PostViewHolder;
import com.yalantis.phoenix.PullToRefreshView;

public abstract class PostListFragment extends Fragment {

    private static final String TAG = "PostListFragment";
    public static final String EXTRA_POST_TYPE = "postType";
    public static final String EXTRA_ORGANIZATION_ID = "organizationId";

    public static final String EXTRA_TRIP_DATE = "tripDate";

    private User currentUser;
    private User postUser;
    private PullToRefreshView mPullToRefreshView;

    protected DatabaseReference mDatabase;
    protected DatabaseReference mUserReference;

    private FirebaseRecyclerAdapter<Post, PostViewHolder> mAdapter;
    protected RecyclerView mRecycler;
    protected LinearLayoutManager mManager;
    // protected boolean postType; //true = driverpost ; false = riderequest
    protected Post.PostType mPostType;
    private String username;

    private String mSelectedOrganizationId;

    private int mTripDate = -1;

    public PostListFragment() {}

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // postType = getArguments().getBoolean(PostListFragment.EXTRA_POST_TYPE);
        // System.out.println("PostListFragment line67 PostType = " + postType);

        mPostType = Post.PostType.valueOf(getArguments().getString(PostListFragment.EXTRA_POST_TYPE));

        mSelectedOrganizationId = getArguments().getString(PostListFragment.EXTRA_ORGANIZATION_ID);

        // May pass trip date as a filter:
        mTripDate = getArguments().getInt(PostListFragment.EXTRA_TRIP_DATE);

        View rootView;
        rootView = inflater.inflate(R.layout.fragment_all_posts, container, false);

        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END create_database_reference]

        mRecycler = rootView.findViewById(R.id.messages_list);
        mRecycler.setHasFixedSize(true);

        mPullToRefreshView = (PullToRefreshView) rootView.findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, 2000);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        mManager.setOrientation(LinearLayoutManager.VERTICAL);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);
        // Get User Object and Set up FirebaseRecyclerAdapter with the Query:
        setCurrentUserAndLoadPosts();

    }


    // [START post_stars_transaction]
    private void onStarClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Post p = mutableData.getValue(Post.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }

                if (p.stars.containsKey(getUid())) {
                    // Unstar the post and remove self from stars
                    p.starCount = p.starCount - 1;
                    p.stars.remove(getUid());
                } else {
                    // Star the post and add self to stars
                    p.starCount = p.starCount + 1;
                    p.stars.put(getUid(), true);
                }

                // Set value and report transaction success
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError);
            }
        });
    }
    // [END post_stars_transaction]

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter=null;
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    // TODO:
    public String getSelectedOrganizationId() {
//        System.out.println("Getting selected org ID from MainSubcategoryActivity...");
        if (getActivity() instanceof MainSubcategoryActivity) {
            return ((MainSubcategoryActivity)getActivity()).getSelectedOrganizationId();
        }
        else {
            /*
                DO NOT USE: mSelectedOrganizationId will not be updated by OrgSpinner.
                 Make sure to add additional if-statement if add another Activity that
                 uses this fragment, and have it return the currently
                 selected organization ID.
             */
            return mSelectedOrganizationId;
        }



    }



    public abstract Query getQuery(DatabaseReference databaseReference);


    public void setCurrentUserAndLoadPosts() {

        // System.out.println("Starting to set user....");

        mDatabase.child("users").child(getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Need to get the user object before loading posts because the query to find posts requires user.

                // Get User object and use the values to update the UI
                currentUser = dataSnapshot.getValue(User.class);

               // loadPosts();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //getUid()
    }



        }



