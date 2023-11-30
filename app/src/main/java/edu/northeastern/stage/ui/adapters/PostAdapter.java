package edu.northeastern.stage.ui.adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import edu.northeastern.stage.R;
import edu.northeastern.stage.model.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> postList;

    public PostAdapter(List<Post> postList) {
        this.postList = postList;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override

    public void onBindViewHolder(PostViewHolder holder, int position) {
        Post post = postList.get(position);
        holder.tvPostContent.setText(post.getPostContent());

        switch (post.getVisibilityState()) {
            case "public":
                holder.visibleState.setImageResource(R.drawable.profile_public);
                break;
            case "friends":
                holder.visibleState.setImageResource(R.drawable.profile_friends);
                break;
            case "private":
                holder.visibleState.setImageResource(R.drawable.profile_private);
                break;
            default:
                holder.visibleState.setImageResource(R.drawable.profile_public);
        }

        //open music link
        String url = postList.get(position).getMusicLink();
        holder.songCard.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            v.getContext().startActivity(i);
        });

        //display artist and track name
        holder.tvTrackName.setText(post.getTrackName());
        holder.tvArtistName.setText(post.getArtistName());
        //display song image
        Picasso.get()
                .load(post.getMusicImageUrl())
                .error(R.drawable.profile_recent_listened_error)
                .into(holder.tvMusicImage);

        // Set the like status on the ivLike ImageView
        holder.ivLike.setOnClickListener(v -> {
            boolean isLiked = !post.isLiked();
            post.setLiked(isLiked);
            holder.ivLike.setSelected(isLiked);
            //TODO: database update
        });

        //display user avatar
        Picasso.get()
                .load(post.getUserAvatarUrl())
                .error(R.drawable.default_pfp)
                .into(holder.ivUserAvatar);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {

        TextView tvPostContent, tvTrackName, tvArtistName;
        ImageView ivUserAvatar, ivLike, visibleState, tvMusicImage;
        LinearLayout songCard;

        public PostViewHolder(View itemView) {
            super(itemView);
            tvPostContent = itemView.findViewById(R.id.tvPostContent);
            ivUserAvatar = itemView.findViewById(R.id.ivUserAvatar);
            ivLike = itemView.findViewById(R.id.ivLike);
            visibleState = itemView.findViewById(R.id.visibleState);
            songCard = itemView.findViewById(R.id.songCard);
            tvTrackName = itemView.findViewById(R.id.tvTrackName);
            tvArtistName = itemView.findViewById(R.id.tvArtistName);
            tvMusicImage = itemView.findViewById(R.id.tvMusicImage);
        }
    }

    public void setPosts(List<Post> posts) {
        this.postList = posts;
        notifyDataSetChanged();
    }
}