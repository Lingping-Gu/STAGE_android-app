package edu.northeastern.stage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostViewHolder> {

    private List<Post> postList;

    public PostsAdapter(List<Post> postList) {
        this.postList = postList;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        Post post = postList.get(position);
        // Set the data to your views here
        holder.tvMusicLink.setText(post.getMusicLink());
        holder.tvPostContent.setText(post.getPostContent());
        // Set other views like image views etc.
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {

        TextView tvMusicLink, tvPostContent;
        ImageView ivUserAvatar, ivLike, visibleState;

        public PostViewHolder(View itemView) {
            super(itemView);
            tvMusicLink = itemView.findViewById(R.id.tvMusicLink);
            tvPostContent = itemView.findViewById(R.id.tvPostContent);
            ivUserAvatar = itemView.findViewById(R.id.ivUserAvatar);
            ivLike = itemView.findViewById(R.id.ivLike);
            visibleState = itemView.findViewById(R.id.visibleState);
            // Initialize other views
        }
    }
}
