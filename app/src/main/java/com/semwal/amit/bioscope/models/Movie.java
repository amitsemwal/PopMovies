package com.semwal.amit.bioscope.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Amit on 16-Feb-16.
 */
public class Movie implements Parcelable {
    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return createMovie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("original_title")
    @Expose
    private String originalTitle;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("backdrop_path")
    @Expose
    private String backdropPath;
    @SerializedName("popularity")
    @Expose
    private Double popularity;
    @SerializedName("vote_count")
    @Expose
    private Long voteCount;
    @SerializedName("vote_average")
    @Expose
    private Double voteAverage;
    private Movie(Parcel in) {
        id = in.readLong();
        title = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
        overview = in.readString();
        voteAverage = in.readDouble();
        releaseDate = in.readString();
        popularity = in.readDouble();
        voteCount = in.readLong();
    }

    public Movie() {

    }

    public Movie(long id, String title, String image, String image2, String overview, double rating, String releasedate, double popularity, long vote_count) {
        this.id = id;
        this.title = title;
        this.posterPath = image;
        this.backdropPath = image2;
        this.overview = overview;
        this.voteAverage = rating;
        this.releaseDate = releasedate;
        this.popularity = popularity;
        this.voteCount = vote_count;
    }

    private static Movie createMovie(Parcel in) {
        return new Movie(in);
    }

    public double getPopularity() {
        return popularity;
    }

    public long getVote_count() {
        return voteCount;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterpath() {
        return posterPath;
    }

    public String getBackground() {
        return backdropPath;
    }

    public String getOverview() {
        return overview;
    }

    public double getRating() {
        return voteAverage;
    }

    public String getDate() {
        return releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(posterPath);
        dest.writeString(backdropPath);
        dest.writeString(overview);
        dest.writeDouble(voteAverage);
        dest.writeString(releaseDate);
        dest.writeDouble(popularity);
        dest.writeLong(voteCount);
    }


}
