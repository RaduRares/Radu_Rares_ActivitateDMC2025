package com.example.app_bun;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Lab4 extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private TextView textViewMargarina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab4);

        Button buttonAdd = findViewById(R.id.button_add);
        textViewMargarina = findViewById(R.id.textview_margarina);

        buttonAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, DataEntryActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Margarina margarina = data.getParcelableExtra("margarina");
            if (margarina != null) {
                textViewMargarina.setText("Margarina: " + margarina.getNume() + "\n"
                        + "Tip: " + margarina.getTip() + "\n"
                        + "Număr calorii: " + margarina.getNumarCalorii() + "\n"
                        + "Conține alergeni: " + (margarina.isContineAlergeni() ? "Da" : "Nu") + "\n"
                        + "Rating: " + margarina.getRating());
            }
        }
    }
}
