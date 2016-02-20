package com.semwal.amit.bioscope;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Amit on 18-Feb-16.
 */
public class MovieDataAdapter extends ArrayAdapter<MovieData> {
    public MovieDataAdapter(Context context, List<MovieData> movieDataList) {
        super(context, 0, movieDataList);
    }
    public MovieDataAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_poster, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }

        final MovieData movie = getItem(position);
        String image_url = "http://image.tmdb.org/t/p/w185" + movie.getPoster();

        viewHolder = (ViewHolder) view.getTag();
        Picasso.with(getContext())
                .load(image_url)
                .into(viewHolder.posterImage);


        return view;
    }

    static class ViewHolder {
        ImageView posterImage;

        public ViewHolder(View view) {
            posterImage = (ImageView) view.findViewById(R.id.grid_item_image);
        }
    }

}
