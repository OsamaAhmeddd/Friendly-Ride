package com.fyp.baigktk.cuifr.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fyp.baigktk.cuifr.R;
import com.fyp.baigktk.cuifr.models.RouteWayPoint;
import com.fyp.baigktk.cuifr.viewholder.OnItemClickListener;
import com.fyp.baigktk.cuifr.viewholder.RouteWayPointListRecyclerAdapter;

import java.util.ArrayList;



public class RouteWayPointListFragment extends Fragment implements OnItemClickListener {
    private static final String TAG = "RouteWayPointListFragment";
    public static final String EXTRA_ROUTE_WAYPOINT_LIST = "RouteWayPointListFragment.routeWayPointList";

    private RecyclerView mRecycler;
    private RouteWayPointListRecyclerAdapter mAdapter;
    private LinearLayoutManager mManager;

    private ArrayList<RouteWayPoint> mRouteWaypointList;

    public RouteWayPointListFragment() {}

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Get list of RouteWayPoint objects to display:
        mRouteWaypointList = getArguments().getParcelableArrayList(RouteWayPointListFragment.EXTRA_ROUTE_WAYPOINT_LIST);

        if (mRouteWaypointList == null) {
            throw new IllegalArgumentException(TAG + ": Must pass EXTRA_SEARCH_RESULTS.");
        }

        if (mRouteWaypointList.size() == 0) {
            throw new IllegalArgumentException(TAG + ": mRouteWaypointList size must be greater than 0.");
        }

        View rootView;

        rootView = inflater.inflate(R.layout.fragment_route_waypoints, container, false);

        mRecycler = rootView.findViewById(R.id.messages_list);

        mRecycler.setHasFixedSize(true);


        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        System.out.println("in Fragment onActivityCreated");
        super.onActivityCreated(savedInstanceState);

        //postType = savedInstanceState.getBundle("postType");

        if (mRouteWaypointList.size() > 0) {
            // Set up Layout Manager, reverse layout
            mManager = new LinearLayoutManager(getActivity());
            mManager.setReverseLayout(true);
            mManager.setStackFromEnd(true);
            mRecycler.setLayoutManager(mManager);

            loadWayPoints();
        }



    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (mAdapter != null) {
//            // mAdapter.cleanup(); // TODO?
//        }
    }


    private void loadWayPoints() {
        System.out.println("About to load waypoints.....");

        // With Potential Carpool objects:
        mAdapter = new RouteWayPointListRecyclerAdapter(mRouteWaypointList);

        mRecycler.setAdapter(mAdapter);

        mAdapter.setClickListener(this);
    }

    @Override
    public void onClick(View view, int position) {
        // Get potential carpool post clicked from position in list:

        RouteWayPoint routeWayPointClicked = mRouteWaypointList.get(position);

        System.out.println("routeWayPointClicked = " + routeWayPointClicked);

        // DO NOTHING...
    }
}
