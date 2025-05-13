package com.example.proiect_grafice;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Spinner spinner;
    Button button;
    String[] tipuri = {"PieChart", "BarChart"};
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        editText = findViewById(R.id.editTextValori);
        spinner = findViewById(R.id.spinnerGrafic);
        button = findViewById(R.id.buttonAfiseazaGrafic);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, tipuri);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        button.setOnClickListener(v -> {
            String input = editText.getText().toString();
            String[] parts = input.split(",");
            ArrayList<Float> valori = new ArrayList<>();

            for (String part : parts) {
                try {
                    valori.add(Float.parseFloat(part.trim()));
                } catch (NumberFormatException e) {

                }
            }

            Intent intent = new Intent(MainActivity.this, GraficActivity.class);
            intent.putExtra("tip", spinner.getSelectedItem().toString());
            intent.putExtra("valori", valori);
            startActivity(intent);
        });
    }
}
