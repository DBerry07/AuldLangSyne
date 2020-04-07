package com.revature.auldlangsyne;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Looper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.revature.auldlangsyne.database.Grocer;
import com.revature.auldlangsyne.database.GrocerViewModel;
//import com.google.android.libraries.places.api.Places;
//import com.google.android.libraries.places.api.net.PlacesClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapHolderFragment extends Fragment implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private LocationUpdateListener listener;
    private LocationManager locationManager;
    private boolean locationPermission = false;
    private Location lastLocation;
    private LatLng defaultLocation = new LatLng(43.6426, -79.3871);

    //PlacesClient placesClient;

    private Activity parent;

    private GrocerViewModel viewModel;

    private float DEFAULT_ZOOM = 17.0f;
    private int RADIUS_SIZE = 2000;

    public MapHolderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        parent = this.getParentFragment().getActivity();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(parent);

        listener = new LocationUpdateListener();
        locationManager = (LocationManager) parent.getSystemService(Context.LOCATION_SERVICE);

        //Places.initialize(getContext(), getResources().getString(R.string.google_maps_api_key));
        //placesClient = Places.createClient(getContext());

        mapView = (MapView) view.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        //viewModel = new ViewModelProvider(this).get(GrocerViewModel.class);

        viewModel = GrocerViewModel.getInstance(getActivity());

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        getLocationPermissions();
        updateLocationUI();
        getDeviceLocation();
    }

    private void getLocationPermissions() {
        if (parent.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
            locationPermission = true;
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (locationPermission) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastLocation = null;
                getLocationPermissions();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                locationPermission = true;
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, listener);
                break;
        }
        //updateLocationUI();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        mapView.onPause();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    private void getDeviceLocation() {
        try {
            if (locationPermission) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastLocation = task.getResult();
                            if (lastLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastLocation.getLatitude(),
                                                lastLocation.getLongitude()), DEFAULT_ZOOM));
                                findNearbyStores(lastLocation);
                            }
                        } else {
                            Log.d("LOCATION", "Current location is null. Using defaults.");
                            Log.e("LOCATION", "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    class LocationUpdateListener implements LocationListener {

        Date savedTime = null;


        @Override
        public void onLocationChanged(Location location) {
            Date currentTime = Calendar.getInstance().getTime();
            if (savedTime == null) {
                savedTime = currentTime;
            }
            if (getSeconds(Math.abs(currentTime.getTime() - savedTime.getTime())) > 15) {
                LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(DEFAULT_ZOOM));
            }

        }

        private int getSeconds(long difference) {
            int days = (int) (difference / (1000 * 60 * 60 * 24));
            int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
            int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
            int sec = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours) - (1000 * 60)) / 1000;
            return sec;
        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub

        }

    }

    private void findNearbyStores(Location deviceLocation) {
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                "location=" +
                deviceLocation.getLatitude() + "," + deviceLocation.getLongitude() +
                "&radius=" + RADIUS_SIZE +
                "&type=grocery" +
                "&key=" + getResources().getString(R.string.google_maps_api_key);

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        if (deviceLocation != null) {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    url,
                    null,
                    new Response.Listener<JSONObject>() {
                        public void onResponse(JSONObject response) {
                            //TODO: On json response
                            Log.d("Location Query Response", response.toString());
                            try {
                                parseLocationJSON(response);
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    }
            );
            requestQueue.add(jsonObjectRequest);
        }
    }

    private void parseLocationJSON(JSONObject response) throws JSONException {
        JSONArray results = response.getJSONArray("results");

        for (int i = 0; i < results.length(); i++) {
            JSONObject localGrocer = results.getJSONObject(i);
            Grocer grocer = new Grocer();
            JSONObject locData = (localGrocer.getJSONObject("geometry")).getJSONObject("location");
            grocer.setLatitude(Float.parseFloat(locData.getString("lat")));
            grocer.setLongitude(Float.parseFloat(locData.getString("lng")));
            grocer.setName(localGrocer.getString("name"));

            mMap.addMarker(new MarkerOptions().position(
                    new LatLng(grocer.getLatitude(), grocer.getLongitude()))
                    .title(grocer.getName()));

            viewModel.insert(grocer);

        }

    }
}
