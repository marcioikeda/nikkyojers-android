package br.com.budismo.nikkyojers.ui.feed;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.budismo.nikkyojers.R;
import br.com.budismo.nikkyojers.data.Post;
import br.com.budismo.nikkyojers.data.User;
import br.com.budismo.nikkyojers.util.Util;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by marcioikeda on 28/01/18.
 */

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.PostViewHolder> {

    private List<Post> mPosts = new ArrayList<>();
    private DatabaseReference mDatabase;

    public void addNewPost(Post post) {
        mPosts.add(post);
        notifyItemInserted(mPosts.size() - 1);
    }

    public void clear() {
        mPosts = new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post_feed, parent, false);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PostViewHolder holder, int position) {
        Post post = mPosts.get(mPosts.size() - position - 1);
        holder.tvPostTitle.setText(post.title);
        holder.tvPostDescription.setText(post.body);
        DatabaseReference userRef = mDatabase.child(post.uid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    holder.tvUserName.setText(user.name);
                    if (!TextUtils.isEmpty(user.photoUrl)) {
                        Util.bindUserPictureIntoView(holder.ivUserPhoto.getContext(), Uri.parse(user.photoUrl), holder.ivUserPhoto);
                    } else {
                        Util.bindPlaceholderPictureIntoView(holder.ivUserPhoto.getContext(), holder.ivUserPhoto);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mPosts != null ? mPosts.size() : 0;
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName;
        CircleImageView ivUserPhoto;
        TextView tvPostTitle;
        TextView tvPostDescription;

        public PostViewHolder(View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tv_username);
            ivUserPhoto = itemView.findViewById(R.id.iv_profile_image);
            tvPostTitle = itemView.findViewById(R.id.tv_post_title);
            tvPostDescription = itemView.findViewById(R.id.tv_post_description);
        }
    }
}
