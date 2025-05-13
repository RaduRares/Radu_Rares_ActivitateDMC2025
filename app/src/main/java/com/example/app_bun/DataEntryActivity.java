package com.example.app_bun;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DataEntryActivity extends AppCompatActivity {

    private EditText editTextNume, editTextCalorii, etDataExpirare;
    private CheckBox checkBoxAlergeni;
    private Spinner spinnerTip;
    private RatingBar ratingBar;
    private Button buttonSave;
    private Calendar calendar;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private CheckBox checkBoxOnline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_entry);


        // Inițializare view-uri
        editTextNume = findViewById(R.id.edittext_nume);
        editTextCalorii = findViewById(R.id.editNumar_calorii);
        checkBoxAlergeni = findViewById(R.id.checkbox_deschis);
        spinnerTip = findViewById(R.id.spinner_tip);
        ratingBar = findViewById(R.id.ratingbar);
        buttonSave = findViewById(R.id.button_save);
        etDataExpirare = findViewById(R.id.etDataExpirare);
        calendar = Calendar.getInstance();
        checkBoxOnline = findViewById(R.id.checkbox_online);

        // Aplicarea setărilor din SharedPreferences
        SharedPreferences prefs = getSharedPreferences("setari", MODE_PRIVATE);
        String textSize = prefs.getString("text_size", "Mediu");
        String textColor = prefs.getString("text_color", "Negru");

        float size = 18f; // default
        if (textSize.equals("Mic")) size = 14f;
        else if (textSize.equals("Mare")) size = 22f;

        int color = Color.BLACK;
        if (textColor.equals("Roșu")) color = Color.RED;
        else if (textColor.equals("Albastru")) color = Color.BLUE;

        editTextNume.setTextSize(size);
        editTextCalorii.setTextSize(size);
        etDataExpirare.setTextSize(size);

        editTextNume.setTextColor(color);
        editTextCalorii.setTextColor(color);
        etDataExpirare.setTextColor(color);

        // Configurare spinner (asigură-te că valorile din R.array.tipuri_margarina corespund exact cu enum-ul TipMarg)
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.tipuri_margarina, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTip.setAdapter(adapter);

        // Dacă se primește un obiect de tip Margarina pentru editare, se completează câmpurile
        Margarina margarinaEdit = getIntent().getParcelableExtra("margarina");
        if (margarinaEdit != null) {
            editTextNume.setText(margarinaEdit.getNume());
            checkBoxAlergeni.setChecked(margarinaEdit.isContineAlergeni());
            editTextCalorii.setText(String.valueOf(margarinaEdit.getNumarCalorii()));
            ratingBar.setRating(margarinaEdit.getRating());
            spinnerTip.setSelection(margarinaEdit.getTip().ordinal());
            etDataExpirare.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    .format(margarinaEdit.getDataExpirare()));
        }

        // Configurare DatePicker pentru data expirării
        etDataExpirare.setOnClickListener(v -> {
            Calendar minDate = Calendar.getInstance();
            minDate.add(Calendar.WEEK_OF_YEAR, 1);
            Calendar maxDate = Calendar.getInstance();
            maxDate.add(Calendar.YEAR, 5);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(year, month, dayOfMonth);
                        etDataExpirare.setText(dateFormat.format(calendar.getTime()));
                    },
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

            datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
            datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
            datePickerDialog.show();
        });
        buttonSave.setOnClickListener(v -> {
            String nume = editTextNume.getText().toString().trim();
            String caloriiText = editTextCalorii.getText().toString().trim();
            String dataText = etDataExpirare.getText().toString().trim();

            if(nume.isEmpty() || caloriiText.isEmpty() || dataText.isEmpty()){
                Toast.makeText(this, "Completează toate câmpurile!", Toast.LENGTH_SHORT).show();
                return;
            }

            int calorii;
            try {
                calorii = Integer.parseInt(caloriiText);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Caloriile trebuie să fie număr!", Toast.LENGTH_SHORT).show();
                return;
            }

            float rating = ratingBar.getRating();
            Margarina.TipMarg tip;
            try {
                tip = Margarina.TipMarg.valueOf(spinnerTip.getSelectedItem().toString());
            } catch (IllegalArgumentException e) {
                Toast.makeText(this, "Tip margarină invalid!", Toast.LENGTH_SHORT).show();
                return;
            }

            Date dataExpirare;
            try {
                dataExpirare = dateFormat.parse(dataText);
            } catch (ParseException e) {
                Toast.makeText(this, "Data expirării invalidă!", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean contineAlergeni = checkBoxAlergeni.isChecked();

            Margarina margarina = new Margarina(nume, contineAlergeni, calorii, rating, tip, dataExpirare);

            // ✅ După ce ai creat obiectul, salvează-l în Firebase dacă checkbox-ul e bifat
            if (checkBoxOnline.isChecked()) {
                FirebaseDatabase database = FirebaseDatabase.getInstance("https://lab11-37e71-default-rtdb.europe-west1.firebasedatabase.app/");
                DatabaseReference ref = database.getReference("margarine");
                String key = ref.push().getKey(); // generez un ID unic
                ref.child(key).setValue(margarina); // salvez margarina
            }


            Intent resultIntent = new Intent();
            resultIntent.putExtra("margarina", margarina);
            setResult(RESULT_OK, resultIntent);

            Toast.makeText(this, "Margarina trimisă: " + margarina.getNume(), Toast.LENGTH_SHORT).show();

            try {
                FileOutputStream fos = openFileOutput("margarine.txt", MODE_APPEND);
                ObjectOutputStream oos;
                File file = new File(getFilesDir(), "margarine.txt");
                if(file.length() == 0){
                    oos = new ObjectOutputStream(fos);
                } else {
                    oos = new DamAppendLaFile(fos);
                }
                oos.writeObject(margarina);
                oos.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            finish();
        });
    }
}
