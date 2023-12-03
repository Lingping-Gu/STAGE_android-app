package edu.northeastern.stage.ui.adapters;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.stage.R;
import edu.northeastern.stage.model.Post;
import edu.northeastern.stage.ui.profile.ProfileFragment;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private Context context;
    private List<Post> postList;
    private String viewType;

    public PostAdapter(Context context, List<Post> postList, String viewType) {
        this.context = context;
        this.postList = postList;
        this.viewType = viewType;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override

    public void onBindViewHolder(PostViewHolder holder, int position) {
        Post post = postList.get(position);

        // set post Visibility
        // friend
        if ("friend".equals(viewType)) {
            String visibilityState = post.getVisibilityState();
            if ("private".equals(visibilityState)) {
                holder.itemView.setVisibility(View.GONE);
            } else {
                holder.itemView.setVisibility(View.VISIBLE);
            }
        }
        // stranger
        if ("stranger".equals(viewType)) {
            String visibilityState = post.getVisibilityState();
            if (!"public".equals(visibilityState)) {
                holder.itemView.setVisibility(View.GONE);
            } else {
                holder.itemView.setVisibility(View.VISIBLE);
            }
        }

        // set Visibility State Icon
        if (viewType == "owner") {
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
        }

        // set post content
        holder.tvPostContent.setText(post.getPostContent());

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
            ArrayList<String> likes = post.getLikes();
            // get current isLiked state
            boolean isLiked = likes.contains(post.getUser());
            // database update
            FirebaseExample firebaseExample = new FirebaseEaxmple();
            firebaseExample.likePost(isLiked);
            isLiked = !isLiked;
            // show the like icon
            holder.ivLike.setSelected(isLiked);
        });

        //display user avatar
        Picasso.get()
                .load(post.getUserAvatarUrl())
                .error(R.drawable.default_pfp)
                .into(holder.ivUserAvatar);

        // When click on the user avatar in the post, it goes to the profile of this user.
        holder.ivUserAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProfileFragment.class);
            context.startActivity(intent);
            intent.putExtra("PROFILE_OWNER_ID", post.getUser());
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            v.getContext().startActivity(i);
        });
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