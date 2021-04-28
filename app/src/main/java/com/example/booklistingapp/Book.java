package com.example.booklistingapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
    private String mTitle;
    private String mAuthor;
    private String mCategory;
    private String mInfoLink;

    public Book(String mTitle, String mAuthor, String mCategory, String mInfoLink) {
        this.mTitle = mTitle;
        this.mAuthor = mAuthor;
        this.mCategory = mCategory;
        this.mInfoLink = mInfoLink;
    }

    protected Book(Parcel in) {
        mTitle = in.readString();
        mAuthor = in.readString();
        mCategory = in.readString();
        mInfoLink = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String mCategory) {
        this.mCategory = mCategory;
    }

    public String getInfoLink() {
        return mInfoLink;
    }

    public void setInfoLink(String mInfoLink) {
        this.mInfoLink = mInfoLink;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mAuthor);
        dest.writeString(mCategory);
        dest.writeString(mInfoLink);
    }
}
