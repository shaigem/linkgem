package com.github.shaigem.linkgem.ui.events;

/**
 * Request to search for a item given a search term.
 *
 * @author Ronnie Tran
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
