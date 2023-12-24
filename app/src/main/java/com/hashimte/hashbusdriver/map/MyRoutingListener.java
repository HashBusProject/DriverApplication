package com.hashimte.hashbusdriver.map;

import android.content.res.Resources;
import android.util.Log;

import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.hashimte.hashbusdriver.R;

import java.util.ArrayList;
import java.util.List;

public class MyRoutingListener implements RoutingListener {

    private GoogleMap map;
    private int width;
    private Resources resources;
    private LatLng pick;

    public MyRoutingListener(GoogleMap map, int width, Resources resources) {
        this.map = map;
        this.width = width;
        this.resources = resources;
    }

    public void setPick(LatLng pick) {
        this.pick = pick;
    }

    @Override
    public void onRoutingFailure(RouteException e) {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        PolylineOptions polyOptions = new PolylineOptions();
        LatLng polylineStartLatLng = null;
        LatLng polylineEndLatLng = null;
        List<Polyline> polylines = new ArrayList<>();
        for (int i = 0; i < route.size(); i++) {
            if (i == shortestRouteIndex) {
                polyOptions.color(resources.getColor(R.color.blue_700));
                polyOptions.width(width);
                polyOptions.addAll(route.get(shortestRouteIndex).getPoints());
                Polyline polyline = map.addPolyline(polyOptions);
                polylineStartLatLng = polyline.getPoints().get(0);
                int k = polyline.getPoints().size();
                polylineEndLatLng = polyline.getPoints().get(k - 1);
                polylines.add(polyline);
            }
        }
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        boundsBuilder
                .include(polylineStartLatLng)
                .include(polylineEndLatLng);
        if(pick != null)
            boundsBuilder.include(pick);
        LatLngBounds bounds = boundsBuilder.build();
        int padding = 100;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        map.animateCamera(cu);
    }

    @Override
    public void onRoutingCancelled() {

    }

}