package com.example.nutritionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nutritionapp.model.MyFireStoreHandler;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowWeightProgressActivity extends AppCompatActivity {

    TextView tvFullName;
    ListView listView;

    String email;
    List<String> userWeights;

    private BarChart barChart;
    private BarDataSet barDataSet;
    private BarData barData;
    private ArrayList<BarEntry> barEntries;

    List<Double> userWeightsAsDoubles = new ArrayList<>();

    String progressOwnerEmail;
    String progressOwnerFullname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_weight_progress);

        Intent intent = getIntent();
        progressOwnerEmail = intent.getExtras().getString("email of progress owner");
        progressOwnerFullname = intent.getExtras().getString("fullname of progress owner");

        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        email = sharedPreferences.getString("email", "no email");
        String fullName = sharedPreferences.getString("fullName", "guest");

        tvFullName = findViewById(R.id.tvFullNameInWeights);
        barChart = findViewById(R.id.chartWeights);

        tvFullName.setText(progressOwnerFullname);

        userWeights = new ArrayList();

        MyFireStoreHandler.getUserData(progressOwnerEmail,(success, userData) -> {

            Map<String, Object> mapUserWeigthsByDates = (Map<String, Object>)userData.get("weights");

            if (mapUserWeigthsByDates==null) {
                return;
            }

            populateListView(mapUserWeigthsByDates);

            drawBarChart(mapUserWeigthsByDates);

        } );
    }

    private double convertToDouble(Object num) {
        if (num instanceof Long) {
            return ((Long) num).doubleValue();
        }
        return (double)num;
    }

    private void populateListView(Map<String, Object> mapUserWeigthsByDates) {

        Map<Date, Double> mapUserWeigthsByDates2 = new HashMap<>();
        for(String keyDateStr : mapUserWeigthsByDates.keySet()) {
            try {
                mapUserWeigthsByDates2.put(new SimpleDateFormat("yyyy-MM-dd").parse(keyDateStr),
                        convertToDouble(mapUserWeigthsByDates.get(keyDateStr)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        ArrayList<Date> listReverseSortedWeights = new ArrayList<>();

        for(String keyDate : mapUserWeigthsByDates.keySet()) {
            try {
                listReverseSortedWeights.add(new SimpleDateFormat("yyyy-MM-dd").parse(keyDate));
            } catch (ParseException e) {
                Log.e("MYDATES", "failed to parse date", e);
            }
        }

        Collections.sort(listReverseSortedWeights, new Comparator<Date>() {
            @Override
            public int compare(Date o1, Date o2) {
                return -o1.compareTo(o2);
            }

            @Override
            public boolean equals(Object obj) {
                return false;
            }
        });

        for (Date dateVal : listReverseSortedWeights) {
            Date keyDate = dateVal;
            Double valWeight = 0D;
            try {
                valWeight = (Double)mapUserWeigthsByDates2.get(keyDate);
            }
            catch (Exception e) {
                Log.d("weightsProgress", "Failed to parse double from: " + mapUserWeigthsByDates2.get(keyDate));
                valWeight = 0D;
            }

            String valWeightStr = String.valueOf(valWeight);
            int posOfDecPoint = valWeightStr.indexOf(".");
            String weightBeforeDecPointAsStr = "";
            int weightAfterDecPointRounded = 0;
            if (posOfDecPoint >= 0) {
                weightBeforeDecPointAsStr = valWeightStr.substring(0,posOfDecPoint);
                String weightAfterDecPoint = valWeightStr.substring(posOfDecPoint+1,
                        (posOfDecPoint+3 <= valWeightStr.length()) ? posOfDecPoint+3:valWeightStr.length());
                weightAfterDecPointRounded = Integer.parseInt(weightAfterDecPoint);
                weightAfterDecPointRounded = Math.round(weightAfterDecPointRounded/10f) * 10;
            }
            String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(keyDate);
            userWeights.add (dateStr + ":  " + weightBeforeDecPointAsStr + "." + weightAfterDecPointRounded);
        }

        listView = findViewById(R.id.lvWeights);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, userWeights);
        listView.setAdapter(adapter);
    }

    private void drawBarChart(Map<String, Object> mapUserWeigthsByDates) {

        Map<Date, Double> mapUserWeigthsByDates2 = new HashMap<>();
        for(String keyDateStr : mapUserWeigthsByDates.keySet()) {
            try {
                mapUserWeigthsByDates2.put(new SimpleDateFormat("yyyy-MM-dd").parse(keyDateStr),
                        convertToDouble(mapUserWeigthsByDates.get(keyDateStr)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        ArrayList<Date> listReverseSortedWeights = new ArrayList<>();

        for(String keyDate : mapUserWeigthsByDates.keySet()) {
            try {
                listReverseSortedWeights.add(new SimpleDateFormat("yyyy-MM-dd").parse(keyDate));
            } catch (ParseException e) {
                Log.e("MYDATES", "failed to parse date", e);
            }
        }

        Collections.sort(listReverseSortedWeights, new Comparator<Date>() {
            @Override
            public int compare(Date o1, Date o2) {
                return +o1.compareTo(o2);
            }

            @Override
            public boolean equals(Object obj) {
                return false;
            }
        });

        for( Date dateVal : listReverseSortedWeights) {
            Date keyDate = dateVal;
            Double valWeight = 0D;
            try {
                valWeight = (Double)mapUserWeigthsByDates2.get(keyDate);
            }
            catch (Exception e) {
                Log.d("weightsProgress", "Failed to parse double from: " + mapUserWeigthsByDates2.get(keyDate));
                valWeight = 0D;
            }
            userWeightsAsDoubles.add(valWeight);
        }

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        float xVal = 1;
        for (double weight : userWeightsAsDoubles) {
            barEntries.add(new BarEntry(xVal, (float) weight));
            xVal++;
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "");
        barData = new BarData(barDataSet);
        barChart.setData(barData);
        barDataSet.setDrawValues(true);
        barDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        barDataSet.setValueTextColor(Color.WHITE);
        barDataSet.setValueTextSize(18f);
        barChart.setDrawValueAboveBar(true);
        barChart.animateXY(2000, 2000);
        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        barChart.setMaxVisibleValueCount(60);

        barChart.notifyDataSetChanged();
        barChart.invalidate();
    }

}

