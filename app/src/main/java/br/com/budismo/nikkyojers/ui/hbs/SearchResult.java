package br.com.budismo.nikkyojers.ui.hbs;

/**
 * Created by marcio.ikeda on 30/01/2018.
 */

public class SearchResult {

    boolean isMatch = false;
    private int sectionNumber;
    private String query;
    private String fullTextHighlighted;
    private int indexOfQuery;


    public int getSectionNumber() {
        return sectionNumber;
    }

    public void setSectionNumber(int sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    public String getfullTextHighlighted() {
        return fullTextHighlighted;
    }

    public void setfullTextHighlighted(String fullTextHighlighted) {
        this.fullTextHighlighted = fullTextHighlighted;
    }

    public boolean isMatch() {
        return isMatch;
    }

    public void setMatch(boolean match) {
        isMatch = match;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getIndexOfQuery() {
        return indexOfQuery;
    }

    public void setIndexOfQuery(int indexOfQuery) {
        this.indexOfQuery = indexOfQuery;
    }
}
