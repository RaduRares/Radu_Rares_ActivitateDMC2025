package com.example.app_bun;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private Spinner spinnerTextSize, spinnerTextColor;
    private Button btnSalveaza;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        spinnerTextSize = findViewById(R.id.spinnerTextSize);
        spinnerTextColor = findViewById(R.id.spinnerTextColor);
        btnSalveaza = findViewById(R.id.btnSalveazaSetari);

        ArrayAdapter<CharSequence> adapterSize = ArrayAdapter.createFromResource(this,
                R.array.text_sizes, android.R.layout.simple_spinner_item);
        adapterSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTextSize.setAdapter(adapterSize);

        ArrayAdapter<CharSequence> adapterColor = ArrayAdapter.createFromResource(this,
                R.array.text_colors, android.R.layout.simple_spinner_item);
        adapterColor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTextColor.setAdapter(adapterColor);

        // Preîncarcă valorile din SharedPreferences dacă există
        SharedPreferences prefs = getSharedPreferences("setari", MODE_PRIVATE);
        spinnerTextSize.setSelection(prefs.getInt("size_pos", 0));
        spinnerTextColor.setSelection(prefs.getInt("color_pos", 0));

        btnSalveaza.setOnClickListener(v -> {
            SharedPreferences.Editor editor = getSharedPreferences("setari", MODE_PRIVATE).edit();
            editor.putString("text_size", spinnerTextSize.getSelectedItem().toString());
            editor.putString("text_color", spinnerTextColor.getSelectedItem().toString());
            editor.putInt("size_pos", spinnerTextSize.getSelectedItemPosition());
            editor.putInt("color_pos", spinnerTextColor.getSelectedItemPosition());
            editor.apply();
            Toast.makeText(this, "Setări salvate!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
