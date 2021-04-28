package com.example.booklistingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class AdapterActivity extends AppCompatActivity {

    private static final String TAG = AdapterActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items_list);

        // Gets intent from Mainactivity
        Intent intent = getIntent();
        // Get ArrayList of Book objects from intent
        ArrayList<Book> bookList = intent.getParcelableArrayListExtra("list");

        // Find reference to listview by list
        ListView listView = (ListView) findViewById(R.id.list);

        // Set new instance of book adapter
        BookAdapter bookAdapter = new BookAdapter(getApplicationContext(), R.layout.list_item, bookList);

        // Set adapter on list view
        listView.setAdapter(bookAdapter);

        // On item click open info link from book
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = bookList.get(position);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(book.getInfoLink()));
                startActivity(intent);
                Log.d(TAG, "onItemClick: ");

            }
        });
    }
}