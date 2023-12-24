package com.hashimte.hashbusdriver.map;

import android.os.AsyncTask;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.Duration;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;

import java.util.concurrent.TimeUnit;

public class DirectionsTask extends AsyncTask<Void, Void, String> {

    LatLng busLatLng;
    LatLng pickPointLatLng;

    public DirectionsTask(LatLng busLatLng, LatLng pickPointLatLng) {
        this.busLatLng = busLatLng;
        this.pickPointLatLng = pickPointLatLng;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            DirectionsResult result = DirectionsApi.newRequest(getGeoContext())
                    .mode(TravelMode.DRIVING)
                    .origin(busLatLng)
                    .destination(pickPointLatLng)
                    .await();
            if (result != null) {
                DirectionsRoute route = result.routes[0];
                DirectionsLeg leg = route.legs[0];
                Duration duration = leg.duration;
                return duration.humanReadable;
            } else {
                return null;
            }
        } catch (Exception e) {
            // Handle exceptions
            return null;
        }
    }



    private GeoApiContext getGeoContext() {
        GeoApiContext geoApiContext = new GeoApiContext();
        return geoApiContext.setQueryRateLimit(3)
                .setApiKey("AIzaSyDyZbg_i9y9T5_tdCRX3evZdD89rKPMWqk")
                .setConnectTimeout(1, TimeUnit.SECONDS)
                .setReadTimeout(1, TimeUnit.SECONDS)
                .setWriteTimeout(1, TimeUnit.SECONDS);
    }
}