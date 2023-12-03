package edu.northeastern.stage.ui.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
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
    private AutoCompleteTextView songSearchACTV;
    private JsonObject selectedResult;

    public TrackSearchAdapter(@NonNull Context context, AutoCompleteTextView songSearchACTV) {
        super(context, 0);
        inflater = LayoutInflater.from(context);
        this.songSearchACTV = songSearchACTV;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Log.d("ViewHolder",getItem(position).toString());
        Log.d("ViewHolder",String.valueOf(position));
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
            viewHolder.bind(convertView.getContext(),result);
        }

        return convertView;
    }

    public JsonObject getSelectedResult() {
        return selectedResult;
    }

    public void setSelectedResult(JsonObject selectedResult) {
        this.selectedResult = selectedResult;
    }

    private static class ViewHolder {
        TextView trackTitleTV;
        TextView artistNameTV;
        ImageView albumIV;

        ViewHolder(View view) {
            trackTitleTV = view.findViewById(R.id.trackTitleTV);
            artistNameTV = view.findViewById(R.id.artistNameTV);
            albumIV = view.findViewById(R.id.songAlbumIV);
        }

        void bind(Context context, JsonObject result) {

            String artists = "";
            String imageURL = "";
            trackTitleTV.setText("");
            artistNameTV.setText("");
            albumIV.setImageDrawable(null);

            trackTitleTV.setText(result.get("name").getAsString());

            JsonArray artistsArray = result.getAsJsonArray("artists");
            if (artistsArray != null && artistsArray.size() > 0) {
                for (JsonElement artist : artistsArray) {
                    artists = artists + artist.getAsJsonObject().get("name").getAsString() + " ";
                }
            }
            artistNameTV.setText(artists);

            JsonObject albumObject = result.getAsJsonObject("album");
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
        }
    }
}
