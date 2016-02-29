package com.semwal.amit.bioscope.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.semwal.amit.bioscope.R;
import com.semwal.amit.bioscope.models.Movie;
import com.semwal.amit.bioscope.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Amit on 18-Feb-16.
 */
public class MovieDataAdapter extends ArrayAdapter<Movie> {
    public MovieDataAdapter(Context context, List<Movie> movieDataList) {
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

        final Movie movie = getItem(position);
        String image_url = Constants.Api.IMAGE_URL_LOW_QUALITY + movie.getPosterpath();

        viewHolder = (ViewHolder) view.getTag();
        Picasso.with(getContext())
                .load(image_url)
                .into(viewHolder.posterImage);


        return view;
    }

    static class ViewHolder {
        @Bind(R.id.grid_item_image)
        ImageView posterImage;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
