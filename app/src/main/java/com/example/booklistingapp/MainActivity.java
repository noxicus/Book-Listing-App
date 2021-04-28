package com.example.booklistingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    private String searchInput = "";
    private static String finalRequestUrl = "";
    private static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get view that represent search button
        TextView searchButton = (TextView) findViewById(R.id.search_button);

        // Set OnClickListener on search button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get view that represent input text
                TextView searchText = findViewById(R.id.text_input);
                searchInput = searchText.getText().toString();
                // Concatenate url and search input
                finalRequestUrl = REQUEST_URL + searchInput;
                //Crate instance of asyncTaskBooks
                AsyncTaskBooks asyncTaskBooks = new AsyncTaskBooks(new AsyncResponse() {
                    @Override
                    public void processFinish(List<Book> bookList) {
                        Log.d(TAG, "processFinish: ");

                        Intent intent = new Intent(getApplicationContext(), AdapterActivity.class);
                        intent.putParcelableArrayListExtra("list", (ArrayList<? extends Parcelable>) bookList);
                        startActivity(intent);
                    }
                });
                asyncTaskBooks.execute(finalRequestUrl);
            }
        });

        //Get empty state textview and set empty view on listview
//        TextView emptyStateView = findViewById(R.id.empty_state);
//        emptyStateView.setText("No server response !");
//        listView.setEmptyView(emptyStateView);
    }

    private class AsyncTaskBooks extends AsyncTask<String, Void, List<Book>> {

        public AsyncResponse delegate = null;

        public AsyncTaskBooks(AsyncResponse asyncResponse) {
            delegate = asyncResponse;
        }

        @Override
        protected List<Book> doInBackground(String... strings) {
            if (strings.length < 0 || strings[0] == null)
                return null;
            // Get list of Books from Google API
            List<Book> bookList = QueryUtils.fetchBookData(strings[0]);

            Log.d(TAG, "doInBackground: ");

            return bookList;
        }

        @Override
        protected void onPostExecute(List<Book> books) {

            if (!books.isEmpty() && books != null) {
                delegate.processFinish(books);
            }
        }
    }
}