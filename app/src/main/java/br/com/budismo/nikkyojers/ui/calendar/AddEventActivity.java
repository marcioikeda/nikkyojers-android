package br.com.budismo.nikkyojers.ui.calendar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import br.com.budismo.nikkyojers.R;

public class AddEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
