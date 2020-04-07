package com.revature.auldlangsyne;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.revature.auldlangsyne.database.Grocer;
import com.revature.auldlangsyne.database.GrocerViewModel;

import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {

    private static ArrayList<Grocer> grocers;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private GrocerViewModel viewModel;

    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_list, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        final Observer<List<Grocer>> grocerObserver = new Observer<List<Grocer>>() {
            @Override
            public void onChanged(@Nullable final List<Grocer> newGrocers) {
                grocers = (ArrayList) newGrocers;
                adapter = new ListAdapter(grocers);
                recyclerView.setAdapter(adapter);
            }
        };

        viewModel = GrocerViewModel.getInstance(this.getActivity());
        viewModel.allGrocers.observe(getViewLifecycleOwner(), grocerObserver);

        //grocers = new ArrayList<Grocer>();
        //makeData();

        return view;
    }

    public List<Grocer> makeData(){

        List<Grocer> grocers = new ArrayList<Grocer>();
        for (int i = 1; i <= 8; i++){
            String str1, str2;
            str1 = "0" + i;
            str2 = "0" + (i + 1);
            grocers.add(new Grocer(
                    "LocationName #"+str1,
                    LocalTime.parse(str1+":00"),
                    LocalTime.parse(str1 + ":30"),
                    "0.0",
                    "0.0"
            ));
        }
        return grocers;

    }
}
