package com.example.sam.bookapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.example.sam.bookapp.MainActivity.LOG_TAG;

/**
 * Created by sam on 11/24/17.
 */

class QueryUtils {

    static List<BookListing> findBooks(String url){
        URL newURL = formatURL(url);
        String output = "";

        try {
            output = makeHTTPRequest(newURL);
        } catch (IOException e) {
            Log.e(LOG_TAG,"Cannot connect", e);
        }

        return extractData(output);
    }

    private static List<BookListing> extractData(String jsonResponse){

        if (TextUtils.isEmpty(jsonResponse)) return null;

        List<BookListing> newList = new ArrayList<BookListing>();

        try {
            JSONObject baseObject = new JSONObject(jsonResponse);

            JSONArray objectArray = baseObject.getJSONArray("items");

            for (int i = 0; i < objectArray.length(); i++){
                JSONObject currentObject = (JSONObject) objectArray.get(i);
                JSONObject volumeInfo = currentObject.getJSONObject("volumeInfo");

                String[] authors;
                if (volumeInfo.has("authors")){
                    JSONArray authorsJson = volumeInfo.getJSONArray("authors");
                    authors = new String[authorsJson.length()];
                        for (int j = 0; j < authors.length; j++) {
                            authors[j] = authorsJson.getString(j);
                        }
                } else {
                    authors = new String[]{""};
                }

                String title = "";
                String url = "";
                String genre = "";
                String date = "";
                String thumbnail = "";

                if (volumeInfo.has("title")) title = volumeInfo.getString("title");
                if (volumeInfo.has("infoLink")) url = volumeInfo.getString("infoLink");
                if (volumeInfo.has("categories")) genre = volumeInfo.getJSONArray("categories").getString(0);
                if (volumeInfo.has("publishedDate")) date = volumeInfo.getString("publishedDate");
                if (volumeInfo.has("imageLinks")){if (volumeInfo.getJSONObject("imageLinks").has("thumbnail")) thumbnail = volumeInfo.getJSONObject("imageLinks").getString("thumbnail");}

                newList.add(new BookListing(authors,title,url,genre,date,thumbnail));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return newList;

    }

    private static URL formatURL(String url){
        URL newURL = null;
        try {
            newURL = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return newURL;
    }

    private static String makeHTTPRequest(URL url) throws IOException{
        String output = "";
        if (url == null) return output;

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(2000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                output = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "bad connection: " + urlConnection.getResponseCode());
                return null;
            }
        } catch (IOException e){
            Log.e(LOG_TAG, "it all broke", e);
        } finally {

            if (urlConnection != null){
                urlConnection.disconnect();
            }

            if (inputStream != null){
                inputStream.close();
            }
        }

        return output;

    }

    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder sb = new StringBuilder();
        if (inputStream != null){
            InputStreamReader isr = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bf = new BufferedReader(isr);
            String line = bf.readLine();
            while (line != null){
                sb.append(line);
                line = bf.readLine();
            }
        }
        return sb.toString();
    }


}
