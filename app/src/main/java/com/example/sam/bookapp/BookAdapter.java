package com.example.sam.bookapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import java.util.List;

/**
 * Created by sam on 11/29/17.
 */

public class BookAdapter extends ArrayAdapter<BookListing> {
    BookAdapter(@NonNull Context context, List<BookListing> books) {
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        //Get the BookListing item at specified index
        BookListing book = getItem(position);

        //If there's nothing there, inflate it
        if (convertView == null) convertView = LayoutInflater.from(getContext()).inflate(R.layout.book_listing_view, parent, false);

        //Store Views to change
        TextView authorName = convertView.findViewById(R.id.authorName);
        TextView bookName = convertView.findViewById(R.id.bookName);
        ImageView thumbnail = convertView.findViewById(R.id.thumbnail);

        //Change the views
        assert book != null;
        authorName.setText(book.getbAuthors()[0]);
        bookName.setText(book.getbTitle());
        Ion.with(thumbnail).load(book.getbThumbnailURL());

        return convertView;
    }

}
