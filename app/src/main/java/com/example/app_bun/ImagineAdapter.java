package com.example.app_bun;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.*;
import android.widget.*;
import java.net.URL;
import java.util.List;
import java.util.concurrent.*;

public class ImagineAdapter extends ArrayAdapter<ImageItem> {
    private ExecutorService executor = Executors.newFixedThreadPool(4);

    public ImagineAdapter(Context context, List<ImageItem> lista) {
        super(context, 0, lista);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageItem item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_imagine, parent, false);
        }

        ImageView img = convertView.findViewById(R.id.imageView);
        TextView descriere = convertView.findViewById(R.id.textView);

        descriere.setText(item.descriere);
        img.setImageBitmap(null); // curățăm imaginea veche

        executor.execute(() -> {
            try {
                URL url = new URL(item.imagineUrl);
                Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());
                img.post(() -> img.setImageBitmap(bitmap));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return convertView;
    }
}
