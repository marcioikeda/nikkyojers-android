package br.com.budismo.nikkyojers.util;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import br.com.budismo.nikkyojers.R;

/**
 * Created by marcio.ikeda on 23/01/2018.
 */

public class Util {

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
}
