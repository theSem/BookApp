package com.example.sam.bookapp;

import java.net.URL;

/**
 * Created by sam on 11/24/17.
 */

public class BookListing {

    private String[] bAuthors;
    private String bTitle;
    private String bURL;
    private String bGenre;
    private String bDate;
    private String bThumbnailURL;

    BookListing(String[] authors, String title, String url, String genre, String date, String thumbnailURL){
        bAuthors = authors;
        bTitle = title;
        bURL = url;
        bGenre = genre;
        bDate = date;
        bThumbnailURL = thumbnailURL;
    }


    public String[] getbAuthors() {
        return bAuthors;
    }

    public String getbTitle() {
        return bTitle;
    }

    public String getbURL() {
        return bURL;
    }

    public String getbGenre() {
        return bGenre;
    }

    public String getbDate() {
        return bDate;
    }

    public String getbThumbnailURL() {
        return bThumbnailURL;
    }
}
