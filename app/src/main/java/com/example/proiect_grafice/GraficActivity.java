package com.example.proiect_grafice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
public class GraficActivity extends AppCompatActivity {
    PieChart pieChart;
    BarChart barChart;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafix);

        pieChart = findViewById(R.id.pieChart);
        barChart = findViewById(R.id.barChart);

        String tip = getIntent().getStringExtra("tip");
        ArrayList<Float> valori = (ArrayList<Float>) getIntent().getSerializableExtra("valori");

        if (tip.equals("PieChart")) {
            pieChart.setVisibility(View.VISIBLE);
            ArrayList<PieEntry> entries = new ArrayList<>();
            for (int i = 0; i < valori.size(); i++) {
                entries.add(new PieEntry(valori.get(i), "Valoare " + (i+1)));
            }
            PieDataSet dataSet = new PieDataSet(entries, "Valori");
            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            PieData data = new PieData(dataSet);
            pieChart.setData(data);
            pieChart.invalidate();

        } else if (tip.equals("BarChart")) {
            barChart.setVisibility(View.VISIBLE);
            ArrayList<BarEntry> entries = new ArrayList<>();
            for (int i = 0; i < valori.size(); i++) {
                entries.add(new BarEntry(i, valori.get(i)));
            }
            BarDataSet dataSet = new BarDataSet(entries, "Valori");
            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            BarData data = new BarData(dataSet);
            barChart.setData(data);
            barChart.invalidate();
        }
    }
}
