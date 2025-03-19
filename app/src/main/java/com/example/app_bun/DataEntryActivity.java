package com.example.app_bun;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DataEntryActivity extends AppCompatActivity {

    private EditText editTextNume, editTextCalorii;
    private CheckBox checkBoxAlergeni;
    private Spinner spinnerTip;
    private RatingBar ratingBar;
    private Button buttonSave;
    private Calendar calendar;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());



    private EditText etDataExpirare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_entry);

        editTextNume = findViewById(R.id.edittext_nume);
        editTextCalorii = findViewById(R.id.editNumar_calorii);
        checkBoxAlergeni = findViewById(R.id.checkbox_deschis);
        spinnerTip = findViewById(R.id.spinner_tip);
        ratingBar = findViewById(R.id.ratingbar);
        buttonSave = findViewById(R.id.button_save);
        calendar = Calendar.getInstance();
        etDataExpirare = findViewById(R.id.etDataExpirare);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.tipuri_margarina, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTip.setAdapter(adapter);


        etDataExpirare.setOnClickListener(v -> {
            Calendar minDate = Calendar.getInstance();
            minDate.add(Calendar.WEEK_OF_YEAR, 1);
            Calendar maxDate = Calendar.getInstance();
            maxDate.add(Calendar.YEAR, 5);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                calendar.set(year, month, dayOfMonth);
                etDataExpirare.setText(dateFormat.format(calendar.getTime()));
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));


            datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
            datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());

            datePickerDialog.show();
        });


        buttonSave.setOnClickListener(v -> {
            String nume = editTextNume.getText().toString();
            boolean contineAlergeni = checkBoxAlergeni.isChecked();
            int calorii = Integer.parseInt(editTextCalorii.getText().toString());
            float rating = ratingBar.getRating();
            Margarina.TipMarg tip = Margarina.TipMarg.valueOf(spinnerTip.getSelectedItem().toString());

            Date dataExpirare = null;
            try {
                dataExpirare = dateFormat.parse(etDataExpirare.getText().toString());
            } catch (ParseException e) {
                Toast.makeText(this, "Data expirării invalidă!", Toast.LENGTH_SHORT).show();
                return;
            }


            if (nume.isEmpty()) {
                Toast.makeText(this, "Numele nu poate fi gol!", Toast.LENGTH_SHORT).show();
                return;
            }

            Margarina margarina = new Margarina(nume, contineAlergeni, calorii, rating, tip, dataExpirare);

            Intent resultIntent = new Intent();
            resultIntent.putExtra("margarina", margarina);
            setResult(RESULT_OK, resultIntent);


            Toast.makeText(this, "Margarina trimisă: " + margarina.getNume(), Toast.LENGTH_SHORT).show();

            finish();
        });

    }
}
