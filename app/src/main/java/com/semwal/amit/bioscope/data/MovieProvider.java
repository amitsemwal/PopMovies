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

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class MovieProvider extends ContentProvider {

    static final int MOVIE = 100;
    static final int TRAILER = 300;
    static final int REVIEW = 200;
    static final int MOVIE_WITH_ID = 101;
    static final int TRAILER_WITH_ID = 301;
    static final int REVIEW_WITH_ID = 201;
    private static final String TAG = "MovieProvider";
    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final SQLiteQueryBuilder sFavouriteMoviesQueryBuilder;
    //static final int MOVIE_WITH_REVIEW = 102;
    //static final int MOVIE_WITH_REVIEW_TRAILER = 103;
    private static final String sMovieSelectionByTitle = MovieContract.MovieEntry.TABLE_NAME +
            MovieContract.MovieEntry.COLUMN_TITLE + " = ? ";
    private static final String sMovieSelectionByID = MovieContract.MovieEntry.TABLE_NAME +
            MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ";
    private static final String sTrailerSelectionByID = MovieContract.TrailerEntry.TABLE_NAME +
            MovieContract.TrailerEntry.COLUMN_MOVIE_ID + " = ? ";
    private static final String sReviewSelectionByID = MovieContract.ReviewsEntry.TABLE_NAME +
            MovieContract.ReviewsEntry.COLUMN_MOVIE_ID + " = ? ";

    static {
        sFavouriteMoviesQueryBuilder = new SQLiteQueryBuilder();

        sFavouriteMoviesQueryBuilder.setTables(
                MovieContract.MovieEntry.TABLE_NAME + ";");//" INNER JOIN " +
        //MovieContract.TrailerEntry.TABLE_NAME + " INNER JOIN " +
        //MovieContract.ReviewsEntry.TABLE_NAME +
//                        " ON (" + MovieContract.MovieEntry.TABLE_NAME +
//                        "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID +
//                        " = " + MovieContract.TrailerEntry.TABLE_NAME +
//                        "." + MovieContract.TrailerEntry.COLUMN_MOVIE_ID +
//                        ") AND (" + MovieContract.MovieEntry.TABLE_NAME +
//                        "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID +
//                        " = " + MovieContract.ReviewsEntry.TABLE_NAME +
//                        "." + MovieContract.ReviewsEntry.COLUMN_MOVIE_ID + "); ");
    }

    private MovieDbHelper mOpenHelper;

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, MovieContract.PATH_FAV_MOVIE, MOVIE);
        matcher.addURI(authority, MovieContract.PATH_FAV_MOVIE_REVIEW, REVIEW);
        matcher.addURI(authority, MovieContract.PATH_FAV_MOVIE_TRAILER, TRAILER);
        matcher.addURI(authority, MovieContract.PATH_FAV_MOVIE + "/*", MOVIE_WITH_ID);
        matcher.addURI(authority, MovieContract.PATH_FAV_MOVIE_REVIEW + "/*", REVIEW_WITH_ID);
        matcher.addURI(authority, MovieContract.PATH_FAV_MOVIE_TRAILER + "/*", TRAILER_WITH_ID);
        return matcher;
    }

    private Cursor getFavouriteMoviesWithIdOrTitle(Uri uri, String[] projection, String sortOrder) {
        String mMovieIdOrTitle = MovieContract.MovieEntry.getMovieIdOrTitleFromUri(uri);
        String[] selectionArgs = new String[]{mMovieIdOrTitle};
        String selection;

        try {
            Integer.parseInt(mMovieIdOrTitle);
            selection = sMovieSelectionByID;
        } catch (NumberFormatException e) {
            selection = sMovieSelectionByTitle;
        }
        return sFavouriteMoviesQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getFavouriteMovies(Uri uri, String[] projection, String sortOrder) {
        return sFavouriteMoviesQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }


    @Override
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIE:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case REVIEW:
                return MovieContract.ReviewsEntry.CONTENT_TYPE;
            case TRAILER:
                return MovieContract.TrailerEntry.CONTENT_TYPE;
            case MOVIE_WITH_ID:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            case REVIEW_WITH_ID:
                return MovieContract.ReviewsEntry.CONTENT_TYPE;
            case TRAILER_WITH_ID:
                return MovieContract.TrailerEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        Log.d(TAG, "query: uri " + uri.toString());
        Log.d(TAG, "query: matcher " + Integer.toString(sUriMatcher.match(uri)));

        switch (sUriMatcher.match(uri)) {
            case MOVIE:
            case TRAILER:
            case REVIEW: {

                retCursor = getFavouriteMovies(uri, projection, sortOrder);
                break;
            }
            case MOVIE_WITH_ID:
            case TRAILER_WITH_ID:
            case REVIEW_WITH_ID: {
                retCursor = getFavouriteMoviesWithIdOrTitle(uri, projection, sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIE: {
                //normalizeDate(values);
                long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MovieContract.MovieEntry.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case TRAILER: {
                long _id = db.insert(MovieContract.TrailerEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MovieContract.TrailerEntry.buildTrailerUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case REVIEW: {
                long _id = db.insert(MovieContract.ReviewsEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MovieContract.ReviewsEntry.buildReviewUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if (null == selection) selection = "1";
        switch (match) {
            case MOVIE:
                rowsDeleted = db.delete(
                        MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case TRAILER:
                rowsDeleted = db.delete(
                        MovieContract.TrailerEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case REVIEW:
                rowsDeleted = db.delete(
                        MovieContract.ReviewsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;

    }

    private void normalizeDate1(ContentValues values) {
        // normalize the date value
        if (values.containsKey(MovieContract.MovieEntry.COLUMN_RELEASE_DATE)) {
            long dateValue = values.getAsLong(MovieContract.MovieEntry.COLUMN_RELEASE_DATE);
            values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, MovieContract.normalizeDate(dateValue));
        }
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // Student: This is a lot like the delete function.  We return the number of rows impacted
        // by the update.
        return 0;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int returnCount;
        switch (match) {
            case MOVIE:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        // normalizeDate(value);
                        long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case REVIEW:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        //normalizeDate(value);
                        long _id = db.insert(MovieContract.ReviewsEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case TRAILER:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        //normalizeDate(value);
                        long _id = db.insert(MovieContract.TrailerEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}