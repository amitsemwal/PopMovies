package com.semwal.amit.bioscope.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Amit on 16-Feb-16.
 */
public class Movie implements Parcelable {
    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    private int id;//id of movie
    private String title; // original_title
    private String poster; // poster_path
    private String background; // backdrop_path
    private String overview; // overview
    private int rating; // vote_average
    private String date; // release_date

    private Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        poster = in.readString();
        background = in.readString();
        overview = in.readString();
        rating = in.readInt();
        date = in.readString();
    }

    public Movie() {

    }

    public Movie(int id, String title, String image, String image2, String overview, int rating, String date) {
        this.id = id;
        this.title = title;
        this.poster = image;
        this.background = image2;
        this.overview = overview;
        this.rating = rating;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPoster() {
        return poster;
    }

    public String getBackground() {
        return background;
    }

    public String getOverview() {
        return overview;
    }

    public int getRating() {
        return rating;
    }

    public String getDate() {
        return date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(poster);
        dest.writeString(background);
        dest.writeString(overview);
        dest.writeInt(rating);
        dest.writeString(date);
    }


}