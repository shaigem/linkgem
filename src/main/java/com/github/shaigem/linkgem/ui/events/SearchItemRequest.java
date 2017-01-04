package com.github.shaigem.linkgem.ui.events;

/**
 * Created on 2017-01-03.
 */
public class SearchItemRequest {

    private String term;

    public SearchItemRequest(String term) {
        this.term = term;
    }

    public String getSearchTerm() {
        return term;
    }
}
