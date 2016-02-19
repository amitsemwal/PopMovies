package com.semwal.amit.bioscope;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Amit on 16-Feb-16.
 */
public class MovieData implements Parcelable {
    private int id;
    private String title; // original_title
    private String poster; // poster_path
    private String background; // backdrop_path
    private String overview;
    private int rating; // vote_average
    private String date; // release_date

    private MovieData(Parcel in) {
        id = in.readInt();
        title = in.readString();
        poster = in.readString();
        background = in.readString();
        overview = in.readString();
        rating = in.readInt();
        date = in.readString();
    }

    public MovieData() {

    }

    public MovieData(int id, String title, String image, String image2, String overview, int rating, String date) {
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

    public static final Parcelable.Creator<MovieData> CREATOR
            = new Parcelable.Creator<MovieData>() {
        public MovieData createFromParcel(Parcel in) {
            return new MovieData(in);
        }

        public MovieData[] newArray(int size) {
            return new MovieData[size];
        }
    };


}
