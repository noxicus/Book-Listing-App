package com.example.booklistingapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {

    }

    /**
     * Method for fetching data from api
     *
     * @param stringUrl https request
     * @return List of objects from parsing json response
     */
    public static List<Book> fetchBookData(String stringUrl) {

        String response = null;
        // Creates url object from http request
        URL url = createUrl(stringUrl);

        try {
            response = makeHttpRequest(url);

        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Book> books = extractData(response);

        return books;
    }


    /**
     * For creating URL object from string
     *
     * @param stringUrl string of https request
     * @return URL object
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        if (stringUrl != null) {
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return url;
    }

    /**
     * Method for making https request, opening connection and receiving response.
     * Handles response if it request is successful and notify if does'nt
     *
     * @param url object from @createUrl method
     * @return String JSON response
     * @throws IOException
     */

    private static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";

        // Check input parameter url
        if (url == null)
            return jsonResponse;

        InputStream inputStream = null;
        HttpsURLConnection httpsURLConnection = null;

        try {
            // Opens connection on url and sets parameters
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod("GET");
            httpsURLConnection.setReadTimeout(10000);
            httpsURLConnection.setConnectTimeout(15000);
            httpsURLConnection.connect();
            // Checks for response code
            if (httpsURLConnection.getResponseCode() == 200) {
                inputStream = httpsURLConnection.getInputStream();
                jsonResponse = inputStreamToString(inputStream);
            } else {
                // Message for error if response code isn't 200 (OK)
                Log.i(LOG_TAG, "Error had appeared, No: " + httpsURLConnection.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpsURLConnection != null) {
                httpsURLConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Method for handling InputStream and getting
     *
     * @param inputStream
     * @return JSON response string
     * @throws IOException
     */

    private static String inputStreamToString(InputStream inputStream) throws IOException {

        StringBuilder sb = new StringBuilder();
        if (inputStream != null) {
            // Initializing InputStreamReader to get char sequences from byte sequences
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            // Initializing BufferReader to get strings from char sequences
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line = bufferedReader.readLine();
            while (line != null) {
                sb.append(line);
                line = bufferedReader.readLine();
            }
        }
        return sb.toString();
    }

    /**
     * Method for extracting data from JSON response parsing it to new instances of class Book
     *
     * @param jsonResponse
     * @return instance of class Book with appropriate data
     */

    private static List<Book> extractData(String jsonResponse) {

        if (jsonResponse == null)
            return null;

        List<Book> books = new ArrayList<>();
        String title;
        String author;
        String category;
        String link;

        try {
            // Get JSON object from response
            JSONObject jsonObject = new JSONObject(jsonResponse);
            // Get JSON array from JSON object by key items
            JSONArray jsonArray = jsonObject.optJSONArray("items");

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObjectItem = jsonArray.getJSONObject(i);

                title = jsonObjectItem.getJSONObject("volumeInfo").optString("title");
                if (jsonObjectItem.getJSONObject("volumeInfo").has("authors")) {
                    author = jsonObjectItem.getJSONObject("volumeInfo").getJSONArray("authors").optString(0);
                } else
                    author = "No data";

                if (jsonObjectItem.getJSONObject("volumeInfo").has("categories")) {
                    category = jsonObjectItem.getJSONObject("volumeInfo").optJSONArray("categories").optString(0);
                } else
                    category = "No data";

                if (jsonObjectItem.getJSONObject("volumeInfo").has("infoLink")) {
                    link = jsonObjectItem.getJSONObject("volumeInfo").optString("infoLink");
                } else
                    link = "http//www.google.com";

                Book book = new Book(title, author, category, link);
                books.add(book);
            }
        } catch (JSONException e) {
            Log.i(LOG_TAG, "Error while extracting data", e);
        }
        return books;
    }
}
