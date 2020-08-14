package com.fyp.baigktk.cuifr.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fyp.baigktk.cuifr.R;
import com.fyp.baigktk.cuifr.models.PostInfoModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;



public class PostListRecyclerAdapter extends RecyclerView.Adapter<PostListRecyclerAdapter.SearchPostViewHolder> {

    private List<PostInfoModel> mDataset;
    private OnClickListenerr mOnClickListenerr;

    // Provide a suitable constructor (depends on the kind of dataset)
    public PostListRecyclerAdapter(List<PostInfoModel> myDataset,OnClickListenerr mOnClickListenerr) {
        mDataset = myDataset;
        this.mOnClickListenerr=mOnClickListenerr;
    }

    @Override
    public SearchPostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_matched_post, parent, false);

        return new SearchPostViewHolder(itemView,mOnClickListenerr);
    }

    @Override
    public void onBindViewHolder(SearchPostViewHolder holder, int position) {
        holder.bindToPost(mDataset.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }




    // View Holder inner class:


    public  class SearchPostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView sourceView,date;
        private TextView authorView;
        private TextView destinationView;
        private TextView passengerCountTextView;
        private TextView car_milage,post_time;
        private TextView estimatedTravelTimeTextView;
        private TextView estimatedTravelDistanceTextView;
        OnClickListenerr onClickListenerr;

        public SearchPostViewHolder(View itemView,OnClickListenerr onClickListenerr) {
            super(itemView);
post_time=itemView.findViewById(R.id.post_time);
            sourceView = (TextView) itemView.findViewById(R.id.post_source);
            authorView = (TextView) itemView.findViewById(R.id.post_cardview_author_name);
            destinationView = (TextView) itemView.findViewById(R.id.post_destination);
            car_milage=itemView.findViewById(R.id.matched_item_car_Milage);
            this.onClickListenerr=onClickListenerr;
            passengerCountTextView = itemView.findViewById(R.id.post_passenger_count);
            estimatedTravelTimeTextView = itemView.findViewById(R.id.carpool_estimated_trip_time);
            estimatedTravelDistanceTextView = itemView.findViewById(R.id.carpool_estimated_trip_distance);
date=itemView.findViewById(R.id.post_date);
            itemView.setOnClickListener(this);
        }

        public void bindToPost(PostInfoModel post) {
            sourceView.setText("From: \n" + post.getSource());
            authorView.setText(post.getAuthor());
            destinationView.setText("To: \n" + post.getDestination());
            car_milage.setText("Car Milage:" + post.getCarMilage());
            passengerCountTextView.setText("Total Passengers: " + post.getPassengerCount());
            //  estimatedTravelTimeTextView.setText();
            // estimatedTravelDistanceTextView.setText("Z23 miles");
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");

            long val = post.getTripDate();
            Date datee = null;
            try {
                datee = originalFormat.parse(String.valueOf(val));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            date.setText(datee.toString());


post_time.setText((int) post.getArrivalTime()+ "Minutes");
        }
        public  String minuteToTime(int minute) {
            int hour = minute / 60;
            minute %= 60;
            String p = "";
            if (hour >= 12) {
                hour %= 12;
                p = "";
            }
            if (hour == 0) {
                hour = 12;
            }
            return (hour < 10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute) + " " + p;
        }

        @Override
        public void onClick(View v) {
            onClickListenerr.onItemClick(getAdapterPosition());
        }
    }

    public interface OnClickListenerr {
        void onItemClick(int position);
    }
}
