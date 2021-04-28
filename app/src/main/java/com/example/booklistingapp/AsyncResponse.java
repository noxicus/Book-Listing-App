package com.example.booklistingapp;

import java.util.List;

public interface AsyncResponse {
    void processFinish(List<Book> bookList);
}
