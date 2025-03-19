package com.example.app_bun;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Lab4 extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private ListView listView;
    private ArrayList<Margarina> margarineList;
    private ArrayAdapter<String> adapter;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab4);

        Button buttonAdd = findViewById(R.id.button_add);
        listView = findViewById(R.id.listView); // Asigură-te că există în XML!


        margarineList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        listView.setAdapter(adapter);

        buttonAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, DataEntryActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Margarina selectedMargarina = margarineList.get(position);
            Toast.makeText(this, "Ai selectat: " + selectedMargarina.getNume(), Toast.LENGTH_SHORT).show();
        });

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            margarineList.remove(position);
            adapter.remove(adapter.getItem(position));
            adapter.notifyDataSetChanged();
            return true;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Margarina margarina = data.getParcelableExtra("margarina");
            if (margarina != null) {
                margarineList.add(margarina);
                String displayText = margarina.getNume() + " - " +
                        margarina.getTip() + " - " +
                        margarina.getNumarCalorii() + " kcal - Expiră: " +
                        (margarina.getDataExpirare() != null ?
                                new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(margarina.getDataExpirare()) : "N/A");

                adapter.add(displayText);
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Margarina adăugată în listă!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Eroare: Obiect Margarina NULL!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Nicio margarină salvată! resultCode: " + resultCode, Toast.LENGTH_LONG).show();
        }
    }
}
