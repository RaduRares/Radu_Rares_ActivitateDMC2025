package com.example.app_bun;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class ListaImaginiActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<ImageItem> listaImagini;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_imagini);

        listView = findViewById(R.id.listViewImagini);
        listaImagini = new ArrayList<>();

        listaImagini.add(new ImageItem("https://upload.wikimedia.org/wikipedia/commons/thumb/b/bd/Red_squirrel_%2821808%29.jpg/1920px-Red_squirrel_%2821808%29.jpg", "veverita","https://commons.wikimedia.org/wiki/Main_Page"));
        listaImagini.add(new ImageItem("https://images.pexels.com/photos/337909/pexels-photo-337909.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2", "masina", "https://www.pexels.com/ro-ro/fotografie/337909/"));
        listaImagini.add(new ImageItem("https://images.pexels.com/photos/909907/pexels-photo-909907.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2", "Audi", "https://www.pexels.com/photo/black-audi-a-series-parked-near-brown-brick-house-909907/"));
        listaImagini.add(new ImageItem("https://upload.wikimedia.org/wikipedia/commons/thumb/b/bd/Red_squirrel_%2821808%29.jpg/1920px-Red_squirrel_%2821808%29.jpg", "Far masina ", "https://www.pexels.com/photo/red-car-head-light-638479/"));
        listaImagini.add(new ImageItem("https://images.pexels.com/photos/39855/lamborghini-brno-racing-car-automobiles-39855.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2", "Lamborgini", "https://www.pexels.com/photo/yellow-sports-car-during-day-time-39855/"));

        ImagineAdapter adapter = new ImagineAdapter(this, listaImagini);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            ImageItem item = listaImagini.get(position);
            Intent intent = new Intent(this, DetaliiImagineActivity.class);
            intent.putExtra("linkPagina", item.paginaWeb);
            startActivity(intent);
        });
    }
}
