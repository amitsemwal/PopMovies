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

import android.content.ContentValues;
import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class InsertToDatabase {// extends AsyncTask<String, Void, String[]> {

    private final String LOG_TAG = InsertToDatabase.class.getSimpleName();

    private final Context mContext;

    public InsertToDatabase(Context context) {
        mContext = context;
    }


    /* The date/time conversion code is going to be moved outside the asynctask later,
     * so for convenience we're breaking it out into its own method now.
     */
    private String getReadableDateString(long time) {
        // Because the API returns a unix timestamp (measured in seconds),
        // it must be converted to milliseconds in order to be converted to valid date.
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("E, MMM d");
        return format.format(date).toString();
    }

    long addLocation(String locationSetting, String cityName, double lat, double lon) {
        // Students: First, check if the location with this city name exists in the db
        // If it exists, return the current ID
        // Otherwise, insert it using the content resolver and the base URI
        return -1;
    }

    Movie convertContentValuesToUXFormat(Vector<ContentValues> cvv) {
        Movie mMovie = new Movie();
        for (int i = 0; i < cvv.size(); i++) {
            ContentValues movieRecord = cvv.elementAt(i);
            mMovie = new Movie(movieRecord.getAsInteger(MovieContract.MovieEntry.COLUMN_MOVIE_ID),
                    movieRecord.getAsString(MovieContract.MovieEntry.COLUMN_TITLE),
                    movieRecord.getAsString(MovieContract.MovieEntry.COLUMN_POSTER_PATH),
                    movieRecord.getAsString(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH),
                    movieRecord.getAsString(MovieContract.MovieEntry.COLUMN_OVERVIEW),
                    movieRecord.getAsDouble(MovieContract.MovieEntry.COLUMN_AVG_VOTES),
                    movieRecord.getAsString(MovieContract.MovieEntry.COLUMN_RELEASE_DATE),
                    movieRecord.getAsDouble(MovieContract.MovieEntry.COLUMN_POPULARITY),
                    movieRecord.getAsInteger(MovieContract.MovieEntry.COLUMN_COUNT_VOTES));
        }
        return mMovie;
    }

}