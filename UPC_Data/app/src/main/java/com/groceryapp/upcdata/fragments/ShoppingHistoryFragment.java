package com.groceryapp.upcdata.fragments;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.groceryapp.upcdata.DB.GroceryItem.GroceryItem;
import com.groceryapp.upcdata.DB.ShoppingTrip.ShoppingTrip;
import com.groceryapp.upcdata.DBHelper;
import com.groceryapp.upcdata.R;
import com.groceryapp.upcdata.adapters.GroceryItemAdapter;
import com.groceryapp.upcdata.adapters.ShoppingTripAdapter;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ShoppingHistoryFragment extends Fragment {

    public final String TAG = "ShoppingHistoryFragment";

    private RecyclerView rvShoppingHistory;
    protected ShoppingTripAdapter adapter;
    protected List<ShoppingTrip> allShoppingTrips;
    DBHelper dbHelper = new DBHelper();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shopping_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        ShoppingTripAdapter.OnClickListener onClickListener = new ShoppingTripAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                //Make this show Detailed View of Trip
            }
        };

        LineChart chart = view.findViewById(R.id.LineChart);
        rvShoppingHistory = view.findViewById(R.id.rvShoppingHistory);
        allShoppingTrips = new ArrayList<>();

        adapter = new ShoppingTripAdapter(getContext(), allShoppingTrips, onClickListener);

        rvShoppingHistory.setAdapter(adapter);
        rvShoppingHistory.setLayoutManager(linearLayoutManager);

        List<Entry> entries = new ArrayList<Entry>();
        dbHelper.queryShoppingTrips(allShoppingTrips, adapter, new DBHelper.ShoppingTripCallback() {
            @Override
            public void OnCallback(List<ShoppingTrip> trips) {

                //Chart Stuff
                for (ShoppingTrip trip : allShoppingTrips){
                    entries.add(new Entry((trip.returnDateAsFloat()), (float)trip.getTotalPrice()));
                }
                chart.getXAxis().setValueFormatter(new LineChartXAxisValueFormatter());
                chart.getXAxis().setLabelCount(3);
                chart.getLegend().setEnabled(false);
                chart.getDescription().setEnabled(false);
                LineDataSet dataSet = new LineDataSet(entries, "Label");
                dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                dataSet.setValueTextColor(355);
                LineData lineData = new LineData(dataSet);
                chart.setData(lineData);
                chart.invalidate(); // refresh
            }
        });



        Collections.sort(allShoppingTrips, Collections.reverseOrder());



    }

    public static class LineChartXAxisValueFormatter extends IndexAxisValueFormatter{
        @Override
        public String getFormattedValue(float value) {

            // Convert float value to date string
            // Convert from seconds back to milliseconds to format time  to show to the user
            long emissionsMilliSince1970Time = ((long) value);

            // Show time in local version
            Date timeMilliseconds = new Date(emissionsMilliSince1970Time);
            DateFormat dateTimeFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault());

            return dateTimeFormat.format(timeMilliseconds);
    }
    }
}