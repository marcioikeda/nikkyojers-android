package br.com.budismo.nikkyojers.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import br.com.budismo.nikkyojers.R;

public class AddPostActivity extends AppCompatActivity implements AddPostActivityFragment.AddPostListener{

    private boolean mEnablePost = false;
    private String KEY_ENABLE_POST_STATE = "key_enable_post_state";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState != null) {
            mEnablePost = savedInstanceState.getBoolean(KEY_ENABLE_POST_STATE, false);
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
            return true;
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
