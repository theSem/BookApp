package com.example.sam.bookapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

import static com.example.sam.bookapp.MainActivity.LOG_TAG;

/**
 * Created by sam on 11/24/17.
 */

public class SearchBooksTask extends AsyncTaskLoader<List<BookListing>> {

    private String url;

    @Override
    public List<BookListing> loadInBackground() {
        if (url == null || TextUtils.isEmpty(url)) return null;
        return QueryUtils.findBooks(url);
    }

    @Override

    protected void onStartLoading(){
        forceLoad();
    }

    SearchBooksTask(Context context, String url){
        super(context);
        this.url = url;
    }
}
