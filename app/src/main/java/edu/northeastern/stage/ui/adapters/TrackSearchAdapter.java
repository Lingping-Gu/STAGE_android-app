package edu.northeastern.stage.ui.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.northeastern.stage.R;

public class TrackSearchAdapter extends ArrayAdapter<JsonObject> {

    private LayoutInflater inflater;
    private ArrayList<JsonObject> results;
    private Context context;

    public TrackSearchAdapter(Context context, ArrayList<JsonObject> results) {
        super(context, R.layout.item_search, results);
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.results = results;
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
        View view = inflater.inflate(R.layout.item_search, parent, false);

        TextView trackTitleTV = view.findViewById(R.id.trackTitleTV);
        TextView artistNameTV = view.findViewById(R.id.artistNameTV);
        ImageView albumIV = view.findViewById(R.id.songAlbumIV);

        trackTitleTV.setText(results.get(position).get("name").getAsString());
        JsonArray artistsArray = results.get(position).getAsJsonArray("artists");

        if (artistsArray != null && !artistsArray.isJsonNull() && artistsArray.size() > 0) {
            StringBuilder artistsSB = new StringBuilder();
            Iterator<JsonElement> iterator = artistsArray.iterator();

            while (iterator.hasNext()) {
                artistsSB.append(iterator.next().getAsJsonObject().get("name").getAsString());

                if (iterator.hasNext()) {
                    artistsSB.append(", ");
                }
            }
            artistNameTV.setText(artistsSB.toString());
        }

        String imageURL = "";
        JsonObject albumObject = results.get(position).getAsJsonObject("album");
        if (albumObject != null) {
            JsonArray imagesArray = albumObject.getAsJsonArray("images");
            if (imagesArray != null && imagesArray.size() > 0) {
                imageURL = imagesArray.get(0).getAsJsonObject().get("url").getAsString();
            }
        }

        Glide.with(context)
                .load(imageURL)
//                  .placeholder(R.drawable.placeholder_image) // Set a placeholder image
//                  .error(R.drawable.error_image) // Set an error image
                .into(albumIV);

        return view;
    }
}