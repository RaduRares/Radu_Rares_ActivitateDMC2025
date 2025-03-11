package com.example.app_bun;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ThirdActivity extends AppCompatActivity {
    private int number1, number2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);


        Intent intent = getIntent();
        String message = intent.getStringExtra("message");
        number1 = intent.getIntExtra("number1", 0);
        number2 = intent.getIntExtra("number2", 0);


        Toast.makeText(this, message + " Numere primite: " + number1 + " și " + number2, Toast.LENGTH_LONG).show();

        Button buttonBack = findViewById(R.id.button_back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("responseMessage", "Mesaj primit de la ThirdActivity");
                returnIntent.putExtra("sum", number1 + number2);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
    }
}