package br.com.budismo.nikkyojers.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.budismo.nikkyojers.R;
import br.com.budismo.nikkyojers.data.Post;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by marcioikeda on 28/01/18.
 */

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.PostViewHolder> {

    List<Post> mPosts;

    public List<Post> getPosts() {
        return mPosts;
    }

    public void setPosts(List<Post> posts) {
        this.mPosts = posts;
        notifyDataSetChanged();
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post_feed, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        Post post = mPosts.get(position);
        holder.tvPostTitle.setText(post.title);
        holder.tvPostDescription.setText(post.body);
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
