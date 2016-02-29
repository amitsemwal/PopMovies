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

import java.util.ArrayList;

public class DatabaseWrapper {// extends AsyncTask<String, Void, String[]> {

    private final String TAG = DatabaseWrapper.class.getSimpleName();

    private final Context mContext;

    public DatabaseWrapper(Context context) {
        mContext = context;
    }

    public boolean movieIdExistsInFav(long id) {
        long movieId;
        Cursor movieCursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                new String[]{MovieContract.MovieEntry.TABLE_NAME +
                        "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID},
                MovieContract.MovieEntry.TABLE_NAME +
                        "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{Long.toString(id)},
                null);
        if (movieCursor.moveToFirst()) {
            int movieIdIndex = movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
            movieId = movieCursor.getLong(movieIdIndex);
            movieCursor.close();
            return true;
        } else {
            movieCursor.close();
            return false;
        }
    }

    public ArrayList<Movie> getAllMoviesFromDb() {
        Cursor mCursor = mContext.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI, null, null, null, null);
        ArrayList<Movie> moviesList = new ArrayList<>();
        if (mCursor == null) {
            return null;
        }
        while (mCursor.moveToNext()) {
            int id = mCursor.getInt(0);//id of movie
            String title = mCursor.getString(1); // original_title
            String poster = mCursor.getString(3); // poster_path
            String background = mCursor.getString(4); // backdrop_path
            String overview = mCursor.getString(2); // overview
            double rating = mCursor.getDouble(5); // vote_average
            double popularity = mCursor.getDouble(7); // vote_average
            String date = mCursor.getString(8); // release_date
            int vote_count = mCursor.getInt(6);
            moviesList.add(new Movie(id, title, poster, background, overview, rating, date, popularity, vote_count));
        }
        mCursor.close();
        return moviesList;

    }

    public long addMovie(int id, String title, String image, String image2, String overview, double rating, String date, double popularity, int vote_count) {
        long movieId;
        if (movieIdExistsInFav(id)) {
            movieId = id;
            return movieId;
        } else {
            ContentValues movieValues = new ContentValues();

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
        // Wait, that worked?  Yes!
        return movieId;
    }

    public long removeMovie(long id) {

        if (movieIdExistsInFav(id)) {
            final int delete = mContext.getContentResolver().delete(
                    MovieContract.MovieEntry.CONTENT_URI,
                    MovieContract.MovieEntry.TABLE_NAME +
                            "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                    new String[]{Long.toString(id)}
            );
            Log.d(TAG, "removeMovie: Deleted " + delete);
            return delete;
        }
        return -1;
    }

}