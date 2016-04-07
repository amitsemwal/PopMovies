package com.semwal.amit.bioscope.data;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
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
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Movie movie);
    }
    private List<Movie> movieList;
    private  Context mContext;
    private OnItemClickListener mlistener;

    public MovieAdapter(Context context, List<Movie> movieDataList, OnItemClickListener listener) {
        movieList = movieDataList;
        mContext = context;
        mlistener = listener;
    }

 @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View posterView = inflater.inflate(R.layout.grid_item_poster, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(posterView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(movieList.get(position), mlistener);
    }




    @Override
    public int getItemCount() {
        return movieList.size();
    }




    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.grid_item_image)
        ImageView posterImage;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(final Movie item, final MovieAdapter.OnItemClickListener listener) {
            String image_url = Constants.Api.IMAGE_URL_LOW_QUALITY + item.getPosterpath();
            Picasso.with(itemView.getContext())
                    .load(image_url)
                    .into(posterImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }


    }

}
