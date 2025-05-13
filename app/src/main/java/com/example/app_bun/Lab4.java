package com.example.app_bun;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Lab4 extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private static final int REQUEST_EDIT = 2;
    private int pozEditat = -1;
    private ListView listView;
    private ArrayList<Margarina> margarineList;
    private MargarinaAdapter adapter;
    private Button buttonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab4);
        Button buttonAdd = findViewById(R.id.button_add);

        Button btnInsert = findViewById(R.id.btnInsert);
        Button btnSelectAll = findViewById(R.id.btnSelectAll);
        Button btnSelectNume = findViewById(R.id.btnSelectNume);
        Button btnInterval = findViewById(R.id.btnInterval);
        Button btnStergeCalorii = findViewById(R.id.btnStergeCalorii);
        Button btnUpdateLitere = findViewById(R.id.btnUpdateLitere);
        buttonAdd = findViewById(R.id.button_add);
        Button buttonSettings = findViewById(R.id.button_settings);
        buttonSettings.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });
        Button btnImagini = findViewById(R.id.btnImagini);
        btnImagini.setOnClickListener(v -> {
            startActivity(new Intent(this, ListaImaginiActivity.class));
        });

        Button btnFavoriteOnline = findViewById(R.id.btnFavoriteOnline);
        btnFavoriteOnline.setOnClickListener(v -> {
            Intent intent = new Intent(this, FavoriteOnlineActivity.class);
            startActivity(intent);
        });

        MargarinaDatabase db = MargarinaDatabase.getDatabase(this);
        MargarinaDao dao = db.margarinaDao();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("margarine");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Toast.makeText(Lab4.this, "Baza de date a fost actualizată!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        listView = findViewById(R.id.listView);

        margarineList = new ArrayList<>();
        adapter = new MargarinaAdapter(this, margarineList);
        listView.setAdapter(adapter);

        buttonAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, DataEntryActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Margarina selectedMargarina = margarineList.get(position);
            Toast.makeText(this, "Ai selectat: " + selectedMargarina.getNume(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, DataEntryActivity.class);
            intent.putExtra("margarina", selectedMargarina);
            pozEditat = position;
            startActivityForResult(intent, REQUEST_EDIT);
        });

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            Margarina fav = margarineList.get(position);
            try {
                FileOutputStream fos = openFileOutput("favorite.txt", MODE_APPEND);
                ObjectOutputStream oos;
                File file = new File(getFilesDir(), "favorite.txt");
                if(file.length() == 0){
                    oos = new ObjectOutputStream(fos);
                } else {
                    oos = new DamAppendLaFile(fos);
                }
                oos.writeObject(fav);
                oos.close();
                fos.close();
                Toast.makeText(this, "Salvat ca favorit!", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        });

        btnInsert.setOnClickListener(v -> {
            if (!margarineList.isEmpty()) {
                Margarina m = margarineList.get(margarineList.size() - 1);
                MargarinaEntity entity = new MargarinaEntity(
                        m.getNume(),
                        m.isContineAlergeni(),
                        m.getNumarCalorii(),
                        m.getRating(),
                        m.getTip().name(),
                        m.getDataExpirare()
                );
                dao.insert(entity);
                Toast.makeText(this, "Inserat în DB!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Lista e goală!", Toast.LENGTH_SHORT).show();
            }
        });

        btnSelectAll.setOnClickListener(v -> {
            List<MargarinaEntity> dbList = dao.getAll();
            margarineList.clear();
            for (MargarinaEntity e : dbList) {
                margarineList.add(new Margarina(
                        e.nume,
                        e.contineAlergeni,
                        e.numarCalorii,
                        e.rating,
                        Margarina.TipMarg.valueOf(e.tip),
                        e.dataExpirare
                ));
            }
            adapter.notifyDataSetChanged();
        });

        btnSelectNume.setOnClickListener(v -> {
            EditText input = new EditText(this);
            new AlertDialog.Builder(this)
                    .setTitle("Introduceți nume")
                    .setView(input)
                    .setPositiveButton("Caută", (dialog, which) -> {
                        MargarinaEntity e = dao.findByNume(input.getText().toString());
                        margarineList.clear();
                        if (e != null) {
                            margarineList.add(new Margarina(e.nume, e.contineAlergeni, e.numarCalorii, e.rating, Margarina.TipMarg.valueOf(e.tip), e.dataExpirare));
                        }
                        adapter.notifyDataSetChanged();
                    }).setNegativeButton("Anulează", null).show();
        });

        btnInterval.setOnClickListener(v -> {
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            final EditText min = new EditText(this);
            final EditText max = new EditText(this);
            min.setHint("Min");
            max.setHint("Max");
            layout.addView(min);
            layout.addView(max);

            new AlertDialog.Builder(this)
                    .setTitle("Interval calorii")
                    .setView(layout)
                    .setPositiveButton("Filtrează", (dialog, which) -> {
                        int vMin = Integer.parseInt(min.getText().toString());
                        int vMax = Integer.parseInt(max.getText().toString());
                        List<MargarinaEntity> list = dao.getByCaloriiInterval(vMin, vMax);
                        margarineList.clear();
                        for (MargarinaEntity e : list) {
                            margarineList.add(new Margarina(e.nume, e.contineAlergeni, e.numarCalorii, e.rating, Margarina.TipMarg.valueOf(e.tip), e.dataExpirare));
                        }
                        adapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("Anulează", null).show();
        });

        btnStergeCalorii.setOnClickListener(v -> {
            EditText input = new EditText(this);
            input.setHint("Valoare calorii");
            new AlertDialog.Builder(this)
                    .setTitle("Șterge margarinele care au calorii mai multe decat nr >")
                    .setView(input)
                    .setPositiveButton("Șterge", (dialog, which) -> {
                        int val = Integer.parseInt(input.getText().toString());
                        int stersi = dao.deleteCaloriiMaiMariDecat(val);
                        Toast.makeText(this, "Șters " + stersi + " margarine", Toast.LENGTH_SHORT).show();
                    }).setNegativeButton("Anulează", null).show();
        });

        btnUpdateLitere.setOnClickListener(v -> {
            EditText input = new EditText(this);
            input.setHint("Literă");
            new AlertDialog.Builder(this)
                    .setTitle("Incrementare calorii pentru nume care încep cu...")
                    .setView(input)
                    .setPositiveButton("Aplică", (dialog, which) -> {
                        dao.incrementCaloriiForNamesStartingWith(input.getText().toString());
                        Toast.makeText(this, "Actualizat!", Toast.LENGTH_SHORT).show();
                    }).setNegativeButton("Anulează", null).show();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            Margarina margarina = data.getParcelableExtra("margarina");
            if (margarina != null) {
                if (requestCode == REQUEST_CODE) {
                    margarineList.add(margarina);
                } else if (requestCode == REQUEST_EDIT && pozEditat != -1) {
                    margarineList.set(pozEditat, margarina);
                    pozEditat = -1;
                }
                adapter.notifyDataSetChanged();
            }
        }
    }
}