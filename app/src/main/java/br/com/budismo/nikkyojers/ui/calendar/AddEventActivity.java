package br.com.budismo.nikkyojers.ui.calendar;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import br.com.budismo.nikkyojers.R;
import br.com.budismo.nikkyojers.data.FirebaseDatabaseHelper;
import br.com.budismo.nikkyojers.data.Post;
import br.com.budismo.nikkyojers.ui.feed.AddPostActivity;
import br.com.budismo.nikkyojers.ui.feed.AddPostActivityFragment;
import br.com.budismo.nikkyojers.util.Util;

public class AddEventActivity extends AppCompatActivity implements AddEventActivityFragment.AddEventListener{

    private boolean mEnablePost = false;
    private String KEY_ENABLE_POST_STATE = "key_enable_post_state";
    private FirebaseDatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState != null) {
            mEnablePost = savedInstanceState.getBoolean(KEY_ENABLE_POST_STATE, false);
        }
        mDatabaseHelper = new FirebaseDatabaseHelper();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(KEY_ENABLE_POST_STATE, mEnablePost);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onEnableToPost(boolean enable) {
        mEnablePost = enable;
        invalidateOptionsMenu();
    }

}
