package com.example.adiputra.sewainbali;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {
    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private ArrayList<History> historyArrayList;

    public HistoryFragment() {
        // Required empty public constructor
    }

    void addData(){
        historyArrayList = new ArrayList<>();
        historyArrayList.add(new History(R.drawable.motor1, "Honda New CBR","Adi Putra Motor", "Sport","Sukses"));
        historyArrayList.add(new History(R.drawable.motor2, "Honda Scoopy","Adi Putra Motor", "Matic","Sukses"));
        historyArrayList.add(new History(R.drawable.motor3, "Yamaha Nmax","Putu Balik", "Matic","Waiting for Payment"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        addData();

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        adapter = new HistoryAdapter(historyArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }

}
