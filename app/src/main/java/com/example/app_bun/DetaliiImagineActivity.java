package com.example.app_bun;

import android.os.Bundle;
import android.webkit.WebView;
import androidx.appcompat.app.AppCompatActivity;

public class DetaliiImagineActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalii_imagine);

        WebView webView = findViewById(R.id.webView);
        String link = getIntent().getStringExtra("linkPagina");
        if (link != null) {
            webView.loadUrl(link);
        }
    }
}
