package com.revature.auldlangsyne;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.revature.auldlangsyne.database.Grocer;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.mViewHolder> {

    private ArrayList<Grocer> grocers;
    private Grocer thisLocation;
    private mViewHolder holder;
    private ViewGroup parent;

    public ListAdapter(ArrayList<Grocer> mLocations){
        grocers = mLocations;
    }

    @NonNull
    @Override
    public ListAdapter.mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        holder = new mViewHolder(v);
        this.parent = parent;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.mViewHolder holder, int position) {
        thisLocation = grocers.get(position);
        holder.locName.setText(thisLocation.getName());
        holder.locSHoursStart.setText(thisLocation.getSeniorStartTime().toString());
        holder.locSHoursEnd.setText(thisLocation.getSeniorEndTime().toString());
        //holder.locNHoursStart.setText(thisLocation.getNormalStartTime().toString());
        //holder.locNHoursEnd.setText(thisLocation.getNormalEndTime().toString());

        final GestureDetectorCompat mDetectorCompat =
                new GestureDetectorCompat(parent.getContext(), new MyGestureListener(holder, position, parent, thisLocation));
        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDetectorCompat.onTouchEvent(event);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return grocers.size();
    }

    public class mViewHolder extends RecyclerView.ViewHolder {

        public TextView locName;
        public TextView locSHoursStart;
        public TextView locSHoursEnd;
        public TextView locNHoursStart;
        public TextView locNHoursEnd;

        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            locName = itemView.findViewById(R.id.location_name);
            locSHoursStart = itemView.findViewById(R.id.location_senior_hours_start);
            locSHoursEnd = itemView.findViewById(R.id.location_senior_hours_end);
            locNHoursStart = itemView.findViewById(R.id.location_normal_hours_start);
            locNHoursEnd = itemView.findViewById(R.id.location_normal_hours_end);
        }
    }

    class MyGestureListener implements GestureDetector.OnGestureListener {

        mViewHolder holder;
        int position;
        ViewGroup gParent;
        Grocer location;

        private MyGestureListener(mViewHolder holder, int mPosition, ViewGroup parent, Grocer location){
            this.holder = holder;
            this.position = mPosition;
            this.gParent = parent;
            this.location = location;
        }


        @Override
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
            NavController navController = Navigation.findNavController(gParent);
            navController.navigate(ComboFragmentDirections.actionComboFragmentToDetailsFragment());
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }
}
