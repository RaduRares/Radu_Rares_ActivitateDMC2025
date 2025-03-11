package com.example.app_bun;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class DataEntryActivity extends AppCompatActivity {

    private EditText editTextNume, editTextCalorii;
    private CheckBox checkBoxAlergeni;
    private Spinner spinnerTip;
    private RatingBar ratingBar;
    private Button buttonSave;

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


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.tipuri_margarina, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTip.setAdapter(adapter);

        buttonSave.setOnClickListener(v -> {
            String nume = editTextNume.getText().toString();
            boolean contineAlergeni = checkBoxAlergeni.isChecked();
            int calorii = Integer.parseInt(editTextCalorii.getText().toString());
            float rating = ratingBar.getRating();


            Margarina.TipMarg tip = Margarina.TipMarg.valueOf(spinnerTip.getSelectedItem().toString());

            // Creare obiect margarină
            Margarina margarina = new Margarina(nume, contineAlergeni, calorii, rating, tip);


            Intent resultIntent = new Intent();
            resultIntent.putExtra("margarina", margarina);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}
