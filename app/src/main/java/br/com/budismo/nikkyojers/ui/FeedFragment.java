package br.com.budismo.nikkyojers.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.budismo.nikkyojers.R;
import br.com.budismo.nikkyojers.data.Post;

/**
 * Created by marcioikeda on 28/01/18.
 */

public class FeedFragment extends Fragment {

    //Firebase Database
    private DatabaseReference mPostsDatabaseReference;
    private ChildEventListener mChildEventListener;

    private FeedAdapter mFeedAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        mPostsDatabaseReference = FirebaseDatabase.getInstance().getReference().child("posts");
        mFeedAdapter = new FeedAdapter();
        RecyclerView recyclerView = view.findViewById(R.id.rv_feed);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(mFeedAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        attachDatabaseReadListener();
    }

    @Override
    public void onPause() {
        super.onPause();
        detachDatabaseReadListener();
        mFeedAdapter.clear();
    }

    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Post post = dataSnapshot.getValue(Post.class);
                    mFeedAdapter.addNewPost(post);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mPostsDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null) {
            mPostsDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }
}
