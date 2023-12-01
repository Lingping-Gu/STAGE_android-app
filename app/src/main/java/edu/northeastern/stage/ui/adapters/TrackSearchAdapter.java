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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import edu.northeastern.stage.R;

public class TrackSearchAdapter extends ArrayAdapter<JsonElement> {

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

        JsonElement result = getItem(position);
        if(result != null) {
            viewHolder.bind(result);
        }

        return convertView;
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

        void bind(JsonObject result) {
            trackTitleTV.setText(result.get("name").getAsString());
            ArrayList<JsonObject> artists = new ArrayList<>();
            artists = result.get("artist").getAsJsonObject();

            artistNameTV.setText(result.get("name").getAsString());
        }

    }
}
