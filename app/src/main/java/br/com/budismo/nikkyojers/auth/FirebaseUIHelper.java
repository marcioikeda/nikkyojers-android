package br.com.budismo.nikkyojers.auth;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.User;
import com.firebase.ui.auth.provider.IdpProvider;
import com.firebase.ui.auth.provider.Provider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

/**
 * Created by marcio.ikeda on 22/01/2018.
 */

public class FirebaseUIHelper {

    public static final int RC_SIGN_IN = 1;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private SignListener mListener;

    public interface SignListener {
        void onSignIn();
        void onSignOut();
    }

    public FirebaseUIHelper(SignListener listener) {
        mFirebaseAuth = FirebaseAuth.getInstance();
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

    public void startFirebaseUIActivity(FragmentActivity activity) {
        activity.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setAvailableProviders(
                                Arrays.asList(
                                        new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                        new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()
                                )
                        )
                        .build(),
                RC_SIGN_IN);
    }

    public FirebaseUser getUser() {
        return mFirebaseAuth.getCurrentUser();
    }

}
