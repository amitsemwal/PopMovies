/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.semwal.amit.bioscope.data;

import android.provider.BaseColumns;
import android.text.format.Time;

import com.semwal.amit.bioscope.utils.Constants;

/**
 * Defines table and column names for the Movie database.
 */
public class MovieContract {

    // To make it easy to query for the exact date, we normalize all dates that go into
    // the database to the start of the the Julian day at UTC.
    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    /*
        Inner class that defines the contents of the favorite_movie table
     */
    public static final class MovieEntry implements BaseColumns {

        public static final String TABLE_NAME = "fav_movies";

        public static final String COLUMN_MOVIE_ID = Constants.JsonTags.Summary.MOVIE_ID;
        public static final String COLUMN_BACKDROP_PATH = Constants.JsonTags.Summary.BACKDROP_PATH;
        public static final String COLUMN_TITLE = Constants.JsonTags.Summary.ORIGINAL_TITLE;
        public static final String COLUMN_OVERVIEW = Constants.JsonTags.Summary.OVERVIEW;
        public static final String COLUMN_POPULARITY = Constants.JsonTags.Summary.POPULARITY;
        public static final String COLUMN_POSTER_PATH = Constants.JsonTags.Summary.POSTER_PATH;
        public static final String COLUMN_RELEASE_DATE = Constants.JsonTags.Summary.RELEASE_DATE;
        public static final String COLUMN_AVG_VOTES = Constants.JsonTags.Summary.VOTE_AVERAGE;
        public static final String COLUMN_COUNT_VOTES = Constants.JsonTags.Summary.VOTE_COUNT;
    }

    /* Inner class that defines the contents of the trailer table */
    public static final class TrailerEntry implements BaseColumns {

        public static final String TABLE_NAME = "trailer";
        public static final String COLUMN_MOVIE_ID = Constants.JsonTags.Trailer.ID;
        public static final String COLUMN_TRAILER_KEY = Constants.JsonTags.Trailer.KEY;
        public static final String COLUMN_TRAILER_NAME = Constants.JsonTags.Trailer.NAME;
        public static final String COLUMN_THUMBNAIL_PATH = Constants.JsonTags.Trailer.THUMB;
        public static final String COLUMN_TRAILER_URL = Constants.JsonTags.Trailer.URL;
    }

    /* Inner class that defines the contents of the reviews table */
    public static final class ReviewsEntry implements BaseColumns {

        public static final String TABLE_NAME = "reviews";
        public static final String COLUMN_MOVIE_ID = Constants.JsonTags.Review.ID;
        public static final String COLUMN_AUTHOR_NAME = Constants.JsonTags.Review.NAME;
        public static final String COLUMN_REVIEW_CONTENT = Constants.JsonTags.Review.REVIEW;
    }
}
