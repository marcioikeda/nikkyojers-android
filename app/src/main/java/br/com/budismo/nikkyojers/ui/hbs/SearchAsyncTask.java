package br.com.budismo.nikkyojers.ui.hbs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Created by marcio.ikeda on 30/01/2018.
 */

public class SearchAsyncTask extends AsyncTaskLoader<SearchResult>{
    public SearchAsyncTask(@NonNull Context context) {
        super(context);
    }

    @Nullable
    @Override
    public SearchResult loadInBackground() {
        //Perform search on the strings and return object with its location.

        return null;
    }
}
