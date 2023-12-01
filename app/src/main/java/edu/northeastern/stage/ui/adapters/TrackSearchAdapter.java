package edu.northeastern.stage.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import edu.northeastern.stage.R;

public class TrackSearchAdapter extends ArrayAdapter<JsonObject> {

    private LayoutInflater inflater;

    public TrackSearchAdapter(Context context) {
        super(context, 0);
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_search, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        JsonObject result = getItem(position);
        if(result != null) {
            viewHolder.bind(result);
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView trackTitleTV;
        TextView artistNameTV;
        ImageView albumIV;
        Context context;

        ViewHolder(View view) {
            context = view.getContext();
            trackTitleTV = view.findViewById(R.id.trackTitleTV);
            artistNameTV = view.findViewById(R.id.artistNameTV);
            albumIV = view.findViewById(R.id.songAlbumIV);
        }

        void bind(JsonObject result) {
            trackTitleTV.setText(result.get("name").getAsString());

            String artists = "";
            String imageURL = "";

            JsonArray artistsArray = result.getAsJsonArray("artists");
            if (artistsArray != null && artistsArray.size() > 0) {
                for (JsonElement artist : artistsArray) {
                    artists = artists + artist.getAsJsonObject().get("name").getAsString() + " ";
                }
            }
            artistNameTV.setText(artists);

            JsonArray imagesArray = result.getAsJsonObject("album").getAsJsonArray("images");

            if(imagesArray != null && imagesArray.size() > 0) {
                imageURL = imagesArray.get(0).getAsJsonObject().get("url").getAsString();
            }

            Glide.with(context)
                    .load(imageURL)
//                  .placeholder(R.drawable.placeholder_image) // Set a placeholder image
//                  .error(R.drawable.error_image) // Set an error image
                    .into(albumIV);
        }
    }
}
