package br.com.budismo.nikkyojers.ui.feed;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import br.com.budismo.nikkyojers.R;
import br.com.budismo.nikkyojers.data.FirebaseDatabaseHelper;
import br.com.budismo.nikkyojers.data.Post;
import br.com.budismo.nikkyojers.util.Util;

public class AddPostActivity extends AppCompatActivity implements AddPostActivityFragment.AddPostListener{

    private boolean mEnablePost = false;
    private static final String KEY_ENABLE_POST_STATE = "key_enable_post_state";
    private FirebaseDatabaseHelper mDatabaseHelper;

    private FrameLayout mFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        mFrameLayout = findViewById(R.id.addpost_container);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState != null) {
            mEnablePost = savedInstanceState.getBoolean(KEY_ENABLE_POST_STATE, false);
        }
        mDatabaseHelper = new FirebaseDatabaseHelper();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!Util.isConnected(this)) {
            Snackbar.make(mFrameLayout, getString(R.string.snack_disconnected), Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_post, menu);
        menu.findItem(R.id.action_publish).setEnabled(mEnablePost);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_publish) {
            AddPostActivityFragment fragment = (AddPostActivityFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
            Post newPost = fragment.getPost();
            if (newPost == null) {
                Util.startFirebaseUIActivity(this);
            } else {
                mDatabaseHelper.writeNewPost(newPost, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError != null) {
                            Toast.makeText(AddPostActivity.this, databaseError.getCode() + ":" + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(AddPostActivity.this, getString(R.string.posted), Toast.LENGTH_LONG).show();
                        }
                        finish();
                    }
                });

                return true;
            }
        }

        return super.onOptionsItemSelected(item);
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
