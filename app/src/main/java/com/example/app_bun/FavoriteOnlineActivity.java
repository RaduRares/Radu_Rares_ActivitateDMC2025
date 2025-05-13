package com.example.app_bun;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FavoriteOnlineActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_online);

        ListView listView = findViewById(R.id.listViewFavorite);
        ArrayList<Margarina> lista = new ArrayList<>();
        MargarinaAdapter adapter = new MargarinaAdapter(this, lista);
        listView.setAdapter(adapter);
        DatabaseReference ref = FirebaseDatabase.getInstance("https://lab11-37e71-default-rtdb.europe-west1.firebasedatabase.app/").getReference("margarine");


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                lista.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Margarina m = ds.getValue(Margarina.class);
                    lista.add(m);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(FavoriteOnlineActivity.this, "Eroare la citire!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
