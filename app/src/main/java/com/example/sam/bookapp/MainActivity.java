package com.example.sam.bookapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<BookListing>>{

    public static final String LOG_TAG = MainActivity.class.getName();

    private static String url;

    private BookAdapter bookAdapter;

    ProgressBar loadingBar;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_page);

        //Initialize the loading bar
        loadingBar = findViewById(R.id.loadingBar);

        //Initialize the listview
        listView = findViewById(R.id.list);

        //Create a new adapter
        bookAdapter = new BookAdapter(this, new ArrayList<BookListing>());

        listView.setAdapter(bookAdapter);

        final EditText editText = findViewById(R.id.input);

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && i == KeyEvent.KEYCODE_ENTER){
                    //if (url != null && url.equals(createURL(String.valueOf(editText.getText())))) return false;
                    url = createURL(String.valueOf(editText.getText()));
                    createLoaderManager();
                    return true;
                }
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BookListing book = bookAdapter.getItem(i);

                if (isConnectedToNetwork()) {
                    Uri bookURI = Uri.parse(book != null ? book.getbURL() : null);
                    startActivity(new Intent(Intent.ACTION_VIEW, bookURI));
                }
            }
        });
        createLoaderManager();

    }

    public String createURL(String text){
        try {
            return "https://www.googleapis.com/books/v1/volumes?q=" + URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    public void createLoaderManager(){
        //if (getLoaderManager() != null) getLoaderManager().restartLoader(1,null,this);
        getLoaderManager().initLoader(1,null,null);
    }

    @Override
    public Loader<List<BookListing>> onCreateLoader(int i, Bundle bundle) {
        loadingBar.setVisibility(View.VISIBLE);
        return new SearchBooksTask(this, url);
    }

    @Override
    public void onLoadFinished(Loader<List<BookListing>> loader, List<BookListing> bookListings) {
        bookAdapter.clear();
        loadingBar.setVisibility(View.INVISIBLE);
        if (bookListings != null && !bookListings.isEmpty()) bookAdapter.addAll(bookListings);
        TextView emptyView = findViewById(R.id.emptyView);
        if (!isConnectedToNetwork()) emptyView.setText("Check internet connection.");
        if (bookListings != null && bookListings.isEmpty()) emptyView.setText("No results found.");
    }

    @Override
    public void onLoaderReset(Loader<List<BookListing>> loader) {
        bookAdapter.clear();
    }

    private boolean isConnectedToNetwork(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }
}
