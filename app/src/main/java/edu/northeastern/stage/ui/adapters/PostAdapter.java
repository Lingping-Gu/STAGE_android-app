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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.stage.API.Spotify;
import edu.northeastern.stage.R;
import edu.northeastern.stage.model.Post;
import edu.northeastern.stage.ui.profile.ProfileFragment;

// TODO: check if I'm looking at Lingping's profile and the third post out of 5 posts
//  is for herself only, how the recycler view looks

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private Context context;
    private List<Post> postList;
    private String viewType;
    private String currentUserId;
    private String postOwnerId;
    private Spotify spotify = new Spotify();

    public PostAdapter(Context context, List<Post> postList, String currentUserId) {
        this.context = context;
        this.postList = postList;
        this.currentUserId = currentUserId;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        Post post = postList.get(position);

        postOwnerId = post.getOwnerID();

        if (isOwner()) {
            viewType = "owner";
        } else if (isFriend()) {
            viewType = "friend";
        } else {
            viewType = "stranger";
        }

        // set post visibility
        // friend
        if (viewType.equals("friend")) {
            String visibilityState = post.getVisibilityState();
            if (viewType.equals("private")) {
                holder.itemView.setVisibility(View.GONE);
            } else {
                holder.itemView.setVisibility(View.VISIBLE);
            }
        }
        // stranger
        if (viewType.equals("stranger")) {
            String visibilityState = post.getVisibilityState();
            if (!viewType.equals("public")) {
                holder.itemView.setVisibility(View.GONE);
            } else {
                holder.itemView.setVisibility(View.VISIBLE);
            }
        }

        // set Visibility State Icon
        if (viewType == "owner") {
            switch (post.getVisibilityState()) {
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
        holder.tvPostContent.setText(post.getContent());

        //open music link
        String url = post.getSpotifyURL();
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
                .load(post.getImageURL())
                .error(R.drawable.profile_recent_listened_error)
                .into(holder.tvMusicImage);



        // TODO: liked status
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

        // TODO: think about how to navigate to certain profile
        // When click on the user avatar in the post, it goes to the profile of this user.
        holder.ivUserAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProfileFragment.class);
            context.startActivity(intent);
            intent.putExtra("PROFILE_OWNER_ID", post.getOwnerID());
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            v.getContext().startActivity(i);
        });
    }

    private boolean isLiked(Long timestamp) {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

        DatabaseReference reference = mDatabase
                .getReference("users")
                .child(postOwnerId)
                .child("posts");

        Query postQuery = reference.orderByChild("timestamp").equalTo(timestamp);

        postQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for(DataSnapshot postSnapshot : snapshot.getChildren()) {
                        ArrayList<String> likes = postSnapshot.child("likes");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // get the correct post reference by timestamp
        // get list of users that likes the post
        // if currentUser is in the list of users, then show red heart
        // if currentUser is not in the list of users, then show blank heart
        // if currentUser is in the list of users and the user clicks, then remove user from list
        // if currentUser is not in the list of users and the user clicks, then add user
    }

    private boolean isOwner() {
        return currentUserId.equals(postOwnerId);
    }

    // TODO: Implement in DataBaseExample
    private boolean isFriend() {
        return true;
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