package com.fyp.baigktk.cuifr.fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.fyp.baigktk.cuifr.models.Post;

public class RecentPostsFragment extends PostListFragment {

    public RecentPostsFragment() {}

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        // [START recent_posts_query]
        // Last 100 posts, these are automatically the 100 most recent
        // due to sorting by push() keys
        Query recentPostsQuery;

        // Load Ride Request Posts:
        if(mPostType == Post.PostType.RIDER) {
            String DriverKey= FirebaseAuth.getInstance().getCurrentUser().getUid();

            recentPostsQuery = mDatabase.child("rideRequest").child(DriverKey)
                    .limitToFirst(100);
        }
        // Load Drive Offer Posts:
        else {

            recentPostsQuery = mDatabase.child("posts").child("driveOffers").child(getSelectedOrganizationId())
                    .child("driveOffers").limitToFirst(100);
        }
        // [END recent_posts_query]

        return recentPostsQuery;
    }
}
