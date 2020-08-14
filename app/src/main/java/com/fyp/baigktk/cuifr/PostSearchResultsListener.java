package com.fyp.baigktk.cuifr;

import com.fyp.baigktk.cuifr.models.Carpool;
import com.fyp.baigktk.cuifr.models.Post;

import java.util.ArrayList;



public interface PostSearchResultsListener {
    void onSearchResultsFound(ArrayList<Post> searchResults, ArrayList<Carpool> potentialCarpools);
}
