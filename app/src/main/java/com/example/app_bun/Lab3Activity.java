package com.example.app_bun;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Lab3Activity extends AppCompatActivity {
    private static final String TAG = "Lab3Lifecycle";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab3);

        Button buttonOpenThird = findViewById(R.id.button_open_third);
        buttonOpenThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Lab3Activity.this, ThirdActivity.class);
                intent.putExtra("message", "Salut din Lab3Activity!");
                intent.putExtra("number1", 112);
                intent.putExtra("number2", 113);
                startActivityForResult(intent, 1);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        // Log de tip error
        Log.e(TAG, "onStart() called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Log de tip warning
        Log.w(TAG, "onResume() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Log de tip debug
        Log.d(TAG, "onPause() called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Log de tip info
        Log.i(TAG, "onStop() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Log de tip verbose pentru final
        Log.v(TAG, "onDestroy() called");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            String responseMessage = data.getStringExtra("responseMessage");
            int sum = data.getIntExtra("sum", 0);

            Toast.makeText(this, responseMessage + " Suma: " + sum, Toast.LENGTH_LONG).show();
        }
    }
}