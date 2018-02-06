package br.com.budismo.nikkyojers.app;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by marcio.ikeda on 06/02/2018.
 */

public class NikkyojersApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
