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

        //Search on whenever the string are, on this case it is on the app, on the resources.
        //This search result needs to be improved, it returns the first match ordering by the sections.
        String highlighted = "<font color='red'>"+query+"</font>";
        String fullText1 = mContext.getString(R.string.section_1_content);
        String fullText2 = mContext.getString(R.string.section_2_content);
        String fullText3 = mContext.getString(R.string.section_3_1_content);
        SearchResult result = new SearchResult();
        result.setQuery(query);

        if (fullText1.contains(query)) {
            result.setMatch(true);
            result.setfullTextHighlighted(fullText1.replace(query, highlighted));
            result.setSectionNumber(0);
            result.setIndexOfQuery(fullText1.indexOf(query));
        } else if (fullText2.contains(query)) {
            result.setMatch(true);
            result.setfullTextHighlighted(fullText2.replace(query, highlighted));
            result.setSectionNumber(1);
            result.setIndexOfQuery(fullText2.indexOf(query));
        } else if (fullText3.contains(query)) {
            result.setMatch(true);
            result.setfullTextHighlighted(fullText3.replace(query, highlighted));
            result.setSectionNumber(2);
            result.setIndexOfQuery(fullText3.indexOf(query));
        } else {
            result.setMatch(false);
        }

        return result;
    }

    @Override
    protected void onPostExecute(SearchResult searchResult) {
        mCallback.onSearched(searchResult);
    }
}
