package com.fyp.baigktk.cuifr.viewholder;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fyp.baigktk.cuifr.PostInfo;
import com.fyp.baigktk.cuifr.R;
import com.fyp.baigktk.cuifr.models.Post;

public class PostViewHolder extends RecyclerView.ViewHolder {

    public TextView sourceView;
    public TextView authorView;
    /*public ImageView starView;
    public TextView numStarsView;*/
    public TextView destinationView;
    public ImageView background;
    public TextView to;

    public TextView postTripDateText;
    public TextView postDateTimeText;

    public PostViewHolder(View itemView) {
        super(itemView);

        sourceView = (TextView) itemView.findViewById(R.id.post_source);
        authorView = (TextView) itemView.findViewById(R.id.post_cardview_author_name);
        destinationView = (TextView) itemView.findViewById(R.id.post_destination);
        background = (ImageView) itemView.findViewById(R.id.card_background);
        to = (TextView) itemView.findViewById(R.id.post_card_address_to);

        postTripDateText = itemView.findViewById(R.id.post_date);
        postDateTimeText = itemView.findViewById(R.id.post_time);
    }

    public void bindToPost(String name, Post.PostType postType, Post post) {
        authorView.setText(name);
        sourceView.setText((post.source));
        destinationView.setText((post.destination));
        String t ="to";
        to.setText(t);

        System.out.println("PostViewHolder:bindToPost: post.postType = " + post.postType);

        if (postType == Post.PostType.RIDER) {
            background.setImageResource(R.drawable.driver_card); // NOTE: driver card is for Ride Request.
        }
        else if (postType == Post.PostType.DRIVER) {
            background.setImageResource(R.drawable.rider_card); // NOTE: rider card is for Drive Offer.
        }
        else if (postType == Post.PostType.CARPOOL) {
            sourceView.setText("CARPOOL: \n" + sourceView.getText()); // DEBUG ONLY.

            background.setImageResource(R.drawable.carpool_card);

            // TODO: add view elements for # passengers, waypoints, trip time, etc.
        }

        postTripDateText.setText(PostInfo.getTripDateText(post));
        postDateTimeText.setText("Arrive at " + PostInfo.getArrivalDateTimeText(post));
    }

    private String trimAddress(String adr){
        String result = adr.substring(0, adr.indexOf(", CA"));
        String[] r = result.split(" ");
        boolean firstCharacter = false;
        if(r.length>1){
            result = "";
            for(int i = 0; i<r.length; i++){
                if(i==0){
                    if(r[0].matches("\\d+(?:\\.\\d+)?")){
                        continue;
                    }
                    else{
                        result = r[0] + " " + r[1];
                        firstCharacter = true;
                    }
                    continue;
                }
                if(i==1 && !firstCharacter){
                    result = r[1];
                }else{
                    result = result + " " + r[i];
                }
            }
        }
        return result;
    }
}
