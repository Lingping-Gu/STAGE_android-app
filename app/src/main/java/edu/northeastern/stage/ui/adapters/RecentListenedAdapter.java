package edu.northeastern.stage.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;
import edu.northeastern.stage.R;

public class RecentListenedAdapter extends RecyclerView.Adapter<RecentListenedAdapter.ViewHolder> {

    private List<String> recentListenedUrls;

    public RecentListenedAdapter(List<String> urls) {
        this.recentListenedUrls = urls;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recent_listened, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        // If there are no images, we'll count the 'None' state as one item
        return recentListenedUrls.isEmpty() ? 1 : Math.min(recentListenedUrls.size(), 4);
    }

    public void setImageUrls(List<String> urls) {
        this.recentListenedUrls = urls;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get()
                .load(recentListenedUrls.get(position))
                .error(R.drawable.profile_recent_listened_error) // shown on error
                .into(holder.recentListenedImageView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView recentListenedImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            recentListenedImageView = itemView.findViewById(R.id.recentListenedImage);
        }
    }
}
