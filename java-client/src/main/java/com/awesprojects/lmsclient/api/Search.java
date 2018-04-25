package com.awesprojects.lmsclient.api;

public class Search {

    public enum Type{
        ANY,
        BOOKS,
        ARTICLES,
        AV
    }

    public enum Where{
        TITLE,
        AUTHORS,
        KEYWORDS
    }

    public enum Availability{
        ANY,
        AVAILABLE_ONLY
    }

}
