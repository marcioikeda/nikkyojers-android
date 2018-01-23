package br.com.budismo.nikkyojers.ui;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.budismo.nikkyojers.R;
import br.com.budismo.nikkyojers.auth.FirebaseUIHelper;
import br.com.budismo.nikkyojers.data.Post;
import br.com.budismo.nikkyojers.util.Util;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddPostActivityFragment extends Fragment {

    EditText mEditTitle;
    EditText mEditDescription;

    public AddPostActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_post, container, false);
        mEditTitle = view.findViewById(R.id.edit_post_title);
        mEditDescription = view.findViewById(R.id.edit_post_description);
        bindUser(view);
        Util.showKeyboard(view.findViewById(R.id.edit_post_title));
        return view;
    }

    private void bindUser(View view) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        ImageView imageView = view.findViewById(R.id.iv_profile_image);
        Util.bindUserPictureIntoView(view.getContext(), user.getPhotoUrl(), imageView);
        TextView tvUserName = view.findViewById(R.id.tv_username);
        tvUserName.setText(user.getDisplayName());
    }

    public Post getPost() {
        return null;
    }
}
