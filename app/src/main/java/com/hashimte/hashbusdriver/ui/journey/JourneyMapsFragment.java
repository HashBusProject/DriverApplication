package com.hashimte.hashbusdriver.ui.journey;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;

import com.google.android.gms.location.LocationRequest;

import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Routing;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hashimte.hashbusdriver.R;
import com.hashimte.hashbusdriver.api.ServicesImp;
import com.hashimte.hashbusdriver.map.MyRoutingListener;
import com.hashimte.hashbusdriver.model.DataSchedule;
import com.hashimte.hashbusdriver.model.Point;
import com.hashimte.hashbusdriver.model.SearchDataSchedule;

import java.util.ArrayList;
import java.util.List;

public class JourneyMapsFragment extends Fragment {
    private GoogleMap mMap;
    private Location myLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private Geocoder geocoder;
    private List<Point> pointForAJourney;
    private Point pick;
    private DataSchedule schedule;
    private Point start;
    private Point end;
    private SharedPreferences journeyPrefs;
    private Marker busLocationMarker;

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            setBusLocationMarker(locationResult.getLastLocation());
        }
    };

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager
                    .PERMISSION_GRANTED) {
//                mMap.setMyLocationEnabled(true);
//                myLocation = mMap.getMyLocation();
//                mMap.setOnMyLocationChangeListener(location -> myLocation = location);
                pointForAJourney = JourneyViewActivity.getPoints();
                schedule = ((JourneyViewActivity) getActivity()).getDataSchedule();
                journeyPrefs = ((JourneyViewActivity) getActivity()).getJourneyPrefs();

                if (pointForAJourney != null) {
                    Log.e("Data 5:", pointForAJourney.toString());
                    findRoutes(pointForAJourney);
                    startLocationUpdates();
                }
            }
        }


        public void findRoutes(List<Point> points) {
            if (points.get(0) == null || points.get(points.size() - 1) == null) {
                Toast.makeText(getContext(), "Unable to get location", Toast.LENGTH_LONG).show();
            } else {
                Log.i("points :", points.toString());
                List<LatLng> latLngs = new ArrayList<>();
                latLngs.add(new LatLng(points.get(0).getX(), points.get(0).getY()));
                mMap.addMarker(new MarkerOptions().position(latLngs.get(0)).title("Start Point"));
                boolean x = pick != null;
                for (int i = 1; i < points.size() - 1; i++) {
                    LatLng latLng = new LatLng(points.get(i).getX(), points.get(i).getY());
                    latLngs.add(latLng);
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng).title(points.get(i).getPointName());
                    mMap.addMarker(markerOptions);
                }
                latLngs.add(new LatLng(points.get(points.size() - 1).getX(), points.get(points.size() - 1).getY()));
                mMap.addMarker(new MarkerOptions().position(latLngs.get(latLngs.size() - 1)).title("End Point"));
                MyRoutingListener myRoutingListener = new MyRoutingListener(mMap, 6, getResources());
                Routing routing = new Routing.Builder()
                        .travelMode(AbstractRouting.TravelMode.DRIVING)
                        .withListener(myRoutingListener)
                        .alternativeRoutes(true)
                        .waypoints(latLngs)
                        .key("AIzaSyBH4sMITOfvKIIq5Sa7HuGq7oikYEujTYs")
                        .build();
                routing.execute();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_journey_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
            geocoder = new Geocoder(getContext());
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

            locationRequest = new LocationRequest.Builder(500)
                    .setMinUpdateIntervalMillis(500)
                    .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                    .build();
        }
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void stopLocationUpdates(){
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private Circle userLocationAccuracyCircle;
    private void setBusLocationMarker(Location location){
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if(busLocationMarker == null){
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.rotation(location.getBearing());
            markerOptions.anchor(0.5F, 0.5F);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icons8_bus_64));
            busLocationMarker = mMap.addMarker(markerOptions);
        }
        else {
            busLocationMarker.setPosition(latLng);
            busLocationMarker.setRotation(location.getBearing());

        }
        if(userLocationAccuracyCircle == null){
            CircleOptions circleOptions = new CircleOptions();
            circleOptions.center(latLng);
            circleOptions.strokeWidth(4);
            circleOptions.strokeColor(R.color.BurlyWood);
            circleOptions.fillColor(R.color.BurlyWood);
            circleOptions.radius(location.getAccuracy());
            userLocationAccuracyCircle = mMap.addCircle(circleOptions);
        }
        else {
            userLocationAccuracyCircle.setCenter(latLng);
            userLocationAccuracyCircle.setRadius(location.getAccuracy());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        startLocationUpdates();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopLocationUpdates();
    }

    @Override
    public void onResume() {
        super.onResume();
        pointForAJourney = JourneyViewActivity.getPoints();
        if (pointForAJourney != null)
            Log.e("Data :", pointForAJourney.toString());
    }
}