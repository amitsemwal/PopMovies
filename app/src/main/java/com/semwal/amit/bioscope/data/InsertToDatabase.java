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

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class InsertToDatabase {// extends AsyncTask<String, Void, String[]> {

    private final String TAG = InsertToDatabase.class.getSimpleName();

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

    public long addMovie(int id, String title, String image, String image2, String overview, double rating, String date, double popularity, int vote_count) {
        long movieId;
        Cursor movieCursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                new String[]{MovieContract.MovieEntry.TABLE_NAME +
                        "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID},
                MovieContract.MovieEntry.TABLE_NAME +
                        "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{Integer.toString(id)},
                null);
        Log.d(TAG, "addMovie: " + movieCursor.getColumnNames()[0].toString());

        if (movieCursor.moveToFirst()) {
            int movieIdIndex = movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
            movieId = movieCursor.getLong(movieIdIndex);
            Log.d(TAG, "addMovie: " + movieIdIndex);
            return movieId;
        } else {
            // Now that the content provider is set up, inserting rows of data is pretty simple.
            // First create a ContentValues object to hold the data you want to insert.
            ContentValues movieValues = new ContentValues();

            // Then add the data, along with the corresponding name of the data type,
            // so the content provider knows what kind of value is being inserted.
            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, id);
            movieValues.put(MovieContract.MovieEntry.COLUMN_COUNT_VOTES, vote_count);
            movieValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, date);
            movieValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, image);
            movieValues.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, image2);
            movieValues.put(MovieContract.MovieEntry.COLUMN_AVG_VOTES, rating);
            movieValues.put(MovieContract.MovieEntry.COLUMN_POPULARITY, popularity);
            movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, title);
            movieValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, overview);

            // Finally, insert location data into the database.
            Uri insertedUri = mContext.getContentResolver().insert(
                    MovieContract.MovieEntry.CONTENT_URI,
                    movieValues
            );

            // The resulting URI contains the ID for the row.  Extract the locationId from the Uri.
            movieId = ContentUris.parseId(insertedUri);
        }

        movieCursor.close();
        // Wait, that worked?  Yes!
        return movieId;
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