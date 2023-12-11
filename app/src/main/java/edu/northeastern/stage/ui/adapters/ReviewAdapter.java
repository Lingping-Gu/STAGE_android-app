package edu.northeastern.stage.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import edu.northeastern.stage.R;
import edu.northeastern.stage.model.Post;
import edu.northeastern.stage.model.Review;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> implements NavigationCallback {
    private List<Review> reviewList;
    private FirebaseDatabase mDatabase;
    private NavigationCallback navigationCallback;
    private Context context;

    public ReviewAdapter(Context context, List<Review> reviewList, NavigationCallback navigationCallback) {
        this.reviewList = reviewList;
        this.mDatabase = FirebaseDatabase.getInstance();
        this.context = context;
        this.navigationCallback = navigationCallback;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviewList.get(position);
        String userId = review.getUserID();
        // Fetch profile picture resource and set it
        DatabaseReference userRef = mDatabase
                .getReference("users")
                .child(review.getUserID())
                .child("profilePicResourceName");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if(context.getResources().getIdentifier(snapshot.getValue(String.class), "drawable", context.getPackageName()) == 0) {
                        holder.avatarImageView.setImageResource(context.getResources().getIdentifier("user", "drawable", context.getPackageName()));
                    } else {
                        holder.avatarImageView.setImageResource(context.getResources().getIdentifier(snapshot.getValue(String.class), "drawable", context.getPackageName()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors here
            }
        });

        // set username
        setProfileUsername(holder, userId);

        holder.contentTextView.setText(review.getContent());
        holder.ratingTextView.setText(String.valueOf(review.getRating()));
        Instant instant = Instant.ofEpochMilli(review.getTimestamp());
        ZonedDateTime dateTime = instant.atZone(ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = dateTime.format(formatter);
        holder.timestampTextView.setText(formattedDateTime);

        holder.avatarImageView.setOnClickListener(v -> {
            String reviewOwnerId = review.getUserID();
            navigationCallback.onNavigateToProfile(reviewOwnerId);
        });
    }

    private void setProfileUsername(ReviewViewHolder holder, String userID) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userID)
                .child("userName");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                holder.userIdTextView.setText(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    @Override
    public void onNavigateToProfile(String profileOwnerId) {

    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView userIdTextView;
        TextView contentTextView;
        TextView ratingTextView;
        TextView timestampTextView;
        ImageView avatarImageView;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            userIdTextView = itemView.findViewById(R.id.tvUserId);
            contentTextView = itemView.findViewById(R.id.tvPostContent);
            ratingTextView = itemView.findViewById(R.id.tvIndividualRating);
            timestampTextView = itemView.findViewById(R.id.tvTimestamp);
            avatarImageView = itemView.findViewById(R.id.ivUserAvatar);
        }
    }
}

