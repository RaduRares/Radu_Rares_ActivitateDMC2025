package com.example.app_bun;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class MargarinaAdapter extends ArrayAdapter<Margarina> {
    private final Context context;
    private final List<Margarina> lista;

    public MargarinaAdapter(Context context, List<Margarina> lista) {
        super(context, 0, lista);
        this.context = context;
        this.lista = lista;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Margarina margarina = lista.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_margarina, parent, false);
        }

        TextView textNume = convertView.findViewById(R.id.text_nume);
        TextView textInfo = convertView.findViewById(R.id.text_info);

        textNume.setText(margarina.getNume());
        textInfo.setText("Tip: " + margarina.getTip() +
                " | Calorii: " + margarina.getNumarCalorii() +
                " | Rating: " + margarina.getRating());

        return convertView;
    }
}
