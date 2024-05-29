package com.fdddf.emailfetcher;

import javax.mail.Folder;
import javax.mail.search.SearchTerm;

public interface CustomFilter {
    public SearchTerm getCustomSearch(Folder folder);
}
