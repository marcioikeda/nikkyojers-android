package br.com.budismo.nikkyojers.ui.calendar;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import br.com.budismo.nikkyojers.R;
import br.com.budismo.nikkyojers.data.FirebaseDatabaseHelper;

public class AddEventActivity extends AppCompatActivity {

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

}
