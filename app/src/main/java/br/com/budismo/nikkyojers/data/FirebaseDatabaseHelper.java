package br.com.budismo.nikkyojers.data;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by marcioikeda on 28/01/18.
 */

public class FirebaseDatabaseHelper {

    private static final String TAG = "FirebaseDatabaseHelper";

    private DatabaseReference mDatabase;

    public FirebaseDatabaseHelper() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void writeNewPost(Post post, DatabaseReference.CompletionListener listener) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("posts").push().getKey();
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        //childUpdates.put("/user-posts/" + post.uid + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates, listener);
    }

    public void createNewEvent(Event event, DatabaseReference.CompletionListener listener) {
        String key = mDatabase.child("events").push().getKey();
        mDatabase.child("events").child(key).setValue(event, listener);
    }

}
