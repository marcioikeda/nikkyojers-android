package br.com.budismo.nikkyojers.ui.hbs;

/**
 * Created by marcio.ikeda on 30/01/2018.
 */

public class SearchResult {

    boolean isMatch = false;
    private int sectionNumber;
    private String stringHighlighted;

    public int getSectionNumber() {
        return sectionNumber;
    }

    public void setSectionNumber(int sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    public String getStringHighlighted() {
        return stringHighlighted;
    }

    public void setStringHighlighted(String stringHighlighted) {
        this.stringHighlighted = stringHighlighted;
    }

    public boolean isMatch() {
        return isMatch;
    }

    public void setMatch(boolean match) {
        isMatch = match;
    }
}
