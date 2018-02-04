package br.com.budismo.nikkyojers.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.firebase.ui.auth.AuthUI;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created by marcio.ikeda on 23/01/2018.
 */

public class Util {

    public static final int RC_SIGN_IN = 1;

    public static void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void bindUserPictureIntoView(Context context, Uri photoUrl, ImageView imageView) {
        if (photoUrl == null || imageView == null) return;
        GlideApp.with(context)
                .load(photoUrl)
                .circleCrop()
                .into(imageView);
    }

    public static void logKeyHash(Context context) {
        // Add code to print out the key hash
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    public static void startFirebaseUIActivity(FragmentActivity activity) {
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
}
