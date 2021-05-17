package com.groceryapp.upcdata.fragments;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
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

import java.lang.reflect.Type;
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

        BarChart chart = view.findViewById(R.id.LineChart);
        rvShoppingHistory = view.findViewById(R.id.rvShoppingHistory);
        allShoppingTrips = new ArrayList<>();

        adapter = new ShoppingTripAdapter(getContext(), allShoppingTrips, onClickListener);

        rvShoppingHistory.setAdapter(adapter);
        rvShoppingHistory.setLayoutManager(linearLayoutManager);

        List<BarEntry> entries = new ArrayList<BarEntry>();
        dbHelper.queryShoppingTrips(allShoppingTrips, adapter, new DBHelper.ShoppingTripCallback() {
            @Override
            public void OnCallback(List<ShoppingTrip> trips) {

                if (trips.size() == 0){
                    Log.d(TAG, "No Shopping History!!");
                    chart.setVisibility(View.INVISIBLE);
                    goBackDialog();
                }
                else {
                    //Chart Stuff
                    float i = 0;
                    for (ShoppingTrip trip : allShoppingTrips) {
                        entries.add(new BarEntry(i, (float) trip.getTotalPrice()));
                        i++;
                    }
                    chart.getXAxis().setDrawGridLines(false);
                    chart.getAxisLeft().setDrawGridLines(false);
                    chart.getAxisLeft().setDrawAxisLine(false);
                    chart.getXAxis().setDrawAxisLine(false);
                    chart.getXAxis().setDrawLabels(false);
                    chart.getAxisLeft().setAxisMinimum(0);
                    chart.getAxisRight().setEnabled(false);
                    chart.getLegend().setEnabled(false);
                    chart.getDescription().setEnabled(false);
                    chart.setBackgroundColor(Color.WHITE);
                    BarDataSet dataSet = new BarDataSet(entries, "Label");
                    dataSet.setColor(Color.rgb(111, 142, 69));
                    BarData barData = new BarData(dataSet);
                    barData.setBarWidth(.7f);
                    chart.setData(barData);
                    chart.invalidate(); // refresh
                }
            }
        });



        Collections.sort(allShoppingTrips, Collections.reverseOrder());



    }

    private void goBackDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle("Looks like you don't have any shopping trips yet!");
        alertDialog.setMessage("Add a Shopping Trip to Begin Viewing Your Recent History");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "GO BACK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.popBackStackImmediate();
                    }
                });
        alertDialog.show();
    }
}