package com.revature.auldlangsyne;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.revature.auldlangsyne.database.Grocer;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {

    Grocer location;

    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_details, container, false);

        //location = DetailsFragmentArgs.fromBundle(getArguments()).getLocation();

        TextView name = view.findViewById(R.id.location_name);
        TextView sStart = view.findViewById(R.id.location_senior_hours_start);
        TextView sEnd = view.findViewById(R.id.location_senior_hours_end);
        //TextView nStart = view.findViewById(R.id.location_normal_hours_start);
        //TextView nEnd = view.findViewById(R.id.location_normal_hours_end);

        name.setText(location.getName());
        sStart.setText(location.getSeniorStartTime().toString());
        sEnd.setText(location.getSeniorEndTime().toString());
        //nStart.setText(location.getNormalStartTime().toString());
        //nEnd.setText(location.getNormalEndTime().toString());

        return view;
    }
}
