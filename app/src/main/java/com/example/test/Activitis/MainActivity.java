package com.example.test.Activitis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.test.Adapters.HourlyAdapters;
import com.example.test.Domains.Hourly;
import com.example.test.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapterHourly;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRecycleView(); //add
    }

    private void initRecycleView() { //add
        ArrayList<Hourly> items=new ArrayList<>(); //add

        items.add(new Hourly("9 pm",28,"cloudy"));
        items.add(new Hourly("10 pm",29,"cloudy"));
        items.add(new Hourly("11 pm",28,"windy"));
        items.add(new Hourly("12 pm",27,"rainy"));
        items.add(new Hourly("1 am",26,"storm"));



        recyclerView.findViewById(R.id.view1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        adapterHourly=new HourlyAdapters(items);
        recyclerView.setAdapter(adapterHourly);




    }
}