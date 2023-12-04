package edu.northeastern.stage.ui.adapters;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import edu.northeastern.stage.R;

public class ImageAdapter extends ArrayAdapter<Integer> {

    private LayoutInflater inflater;
    private Integer[] images;

    public ImageAdapter(Context context, Integer[] images) {
        super(context, R.layout.item_image_spinner, images);
        this.inflater = LayoutInflater.from(context);
        this.images = images;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_image_spinner, parent, false);

        ImageView imageView = view.findViewById(R.id.imageView);
        imageView.setImageResource(images[position]);

        return view;
    }

}
