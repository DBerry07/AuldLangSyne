package com.revature.auldlangsyne;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.revature.auldlangsyne.database.GrocerViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ComboFragment extends Fragment implements OnMapReadyCallback
        //, GestureDetector.OnGestureListener
        , View.OnClickListener{

    GoogleMap mMap;

    static FragmentManager fragmentManager;
    static View map;

    Boolean mapExpanded = false;

    public ComboFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_combo, container, false);


        FloatingActionButton fob = view.findViewById(R.id.floating_action_button);
        fob.setOnClickListener(this);

        fragmentManager = getChildFragmentManager();
        map = fragmentManager.findFragmentById(R.id.map_view_fragment).getView();

        /*SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*/

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap){
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        ViewGroup.LayoutParams mapLayoutParams = map.getLayoutParams();
        float factor = this.getResources().getDisplayMetrics().density;


        if (!mapExpanded) {
            mapLayoutParams.height = (int) (500 * factor);
            map.setLayoutParams(mapLayoutParams);
            mapExpanded = true;
        }
        else {
            mapLayoutParams.height = (int) (150 * factor);
            map.setLayoutParams(mapLayoutParams);
            mapExpanded = false;
        }
    }

    /*@Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float diffY = e2.getY() - e1.getY();

        FragmentManager fragmentManager = getSupportFragmentManager();
        View map = fragmentManager.findFragmentById(R.id.map_view_fragment).getView();
        ViewGroup.LayoutParams mapLayoutParams = map.getLayoutParams();
        //View list = fragmentManager.findFragmentById(R.id.list_fragment).getView();
        //ViewGroup.LayoutParams listLayoutParams = list.getLayoutParams();

        if (diffY > 0) {
            mapLayoutParams.height = 150;
            map.setLayoutParams(mapLayoutParams);
        }
        else if (diffY < 0) {
            mapLayoutParams.height = 500;
            map.setLayoutParams(mapLayoutParams);
        }
        return true;
    }*/
}
