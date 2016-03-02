package com.semwal.amit.bioscope.data;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.semwal.amit.bioscope.R;
import com.semwal.amit.bioscope.utils.Constants;
import com.squareup.picasso.Picasso;

public class MovieCursorAdapter extends CursorAdapter {
    public MovieCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.grid_item_poster, parent, false);
    }

    private String getMoviePosterFromCursor(Cursor cursor) {
        int poster_idx = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH);
        return cursor.getString(poster_idx);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView moviePoster = (ImageView) view.findViewById(R.id.grid_item_image);
        String posterURL = Constants.Api.IMAGE_URL_LOW_QUALITY + getMoviePosterFromCursor(cursor);
        Picasso.with(context).load(posterURL).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(moviePoster);
        return;
    }
}
