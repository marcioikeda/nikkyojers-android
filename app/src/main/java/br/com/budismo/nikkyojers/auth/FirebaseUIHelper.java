package br.com.budismo.nikkyojers.auth;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.budismo.nikkyojers.data.User;

/**
 * Created by marcio.ikeda on 22/01/2018.
 */

public class FirebaseUIHelper {

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DatabaseReference mDatabase;
    private SignListener mListener;

    public interface SignListener {
        void onSignIn();
        void onSignOut();
    }

    public FirebaseUIHelper(SignListener listener) {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mListener = listener;
        initAuthStateListener();
    }

    private void initAuthStateListener() {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    mListener.onSignIn();
                    // Save/update user to firebase database
                    mDatabase.child("users").child(user.getUid())
                            .setValue(new User(user.getDisplayName(), user.getPhotoUrl().toString(), user.getProviderId()));
                } else {
                    // User is signed out
                    mListener.onSignOut();
                }
            }
        };
    }

    public void addAuthStateListener() {
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    public void removeAuthStateListener() {
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    public FirebaseUser getUser() {
        return mFirebaseAuth.getCurrentUser();
    }

    public void signOut() {
        mFirebaseAuth.signOut();
    }

}
