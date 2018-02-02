package br.com.budismo.nikkyojers.ui.hbs;

import android.content.Context;
import android.os.AsyncTask;

import br.com.budismo.nikkyojers.R;

/**
 * Created by marcio.ikeda on 30/01/2018.
 */

public class SearchAsyncTask extends AsyncTask<String, Void, SearchResult> {

    private OnSearch mCallback;
    private Context mContext;

    public interface OnSearch {
        void onSearched(SearchResult result);
    }

    public SearchAsyncTask(Context context, OnSearch callback) {
        mContext = context;
        mCallback = callback;
    }

    @Override
    protected SearchResult doInBackground(String... strings) {
        String query = strings[0];

        //Search on whenever the string are, on this case it is on the app, on the resources
        String highlighted = "<font color='red'>"+query+"</font>";
        String fullText = mContext.getString(R.string.section_1_content);
        SearchResult result = new SearchResult();
        result.setMatch(true);
        result.setfullTextHighlighted(fullText.replace(query, highlighted));
        result.setSectionNumber(1);
        result.setQuery(query);
        result.setIndexOfQuery(fullText.indexOf(query));

        return result;
    }

    @Override
    protected void onPostExecute(SearchResult searchResult) {
        mCallback.onSearched(searchResult);
    }
}
