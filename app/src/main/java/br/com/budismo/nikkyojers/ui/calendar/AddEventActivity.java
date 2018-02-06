package br.com.budismo.nikkyojers.ui.calendar;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import br.com.budismo.nikkyojers.R;
import br.com.budismo.nikkyojers.util.Util;

public class AddEventActivity extends AppCompatActivity {

    private FrameLayout mFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        mFrameLayout = findViewById(R.id.addevent_container);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!Util.isConnected(this)) {
            Snackbar.make(mFrameLayout, getString(R.string.snack_disconnected), Snackbar.LENGTH_SHORT).show();
        }
    }
}
