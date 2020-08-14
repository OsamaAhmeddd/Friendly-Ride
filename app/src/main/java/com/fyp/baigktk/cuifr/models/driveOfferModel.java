package com.fyp.baigktk.cuifr.models;

import java.sql.Time;
import java.util.List;
import java.util.Map;

public class driveOfferModel {

    private PostInfoModel postInfo;
    private List<RoutePoints> routePoints;




    public PostInfoModel getPostInfo() {
        return postInfo;
    }

    public void setPostInfo(PostInfoModel postInfo) {
        this.postInfo = postInfo;
    }

    public List<RoutePoints> getRoutePoints() {
        return routePoints;
    }

    public void setRoutePoints(List<RoutePoints> routePoints) {
        this.routePoints = routePoints;
    }
}
