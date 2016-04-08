package com.semwal.amit.bioscope.data;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
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
    private Cursor mCursor;
    private boolean fromDB;

    public MovieAdapter(Context context, List<Movie> movieDataList, OnItemClickListener listener) {
        movieList = movieDataList;
        mContext = context;
        mlistener = listener;
    }
    public MovieAdapter(Context context, Cursor cursor, OnItemClickListener listener) {
        mCursor = cursor;
        mContext = context;
        mlistener = listener;
        fromDB = true;
    }
    public Cursor swapCursor(Cursor cursor) {
        if (mCursor == cursor) {
            return null;
        }
        Cursor oldCursor = mCursor;
        mCursor = cursor;
        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return oldCursor;
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
        if (fromDB) {
            if (mCursor != null) {
                mCursor.moveToPosition(position);
                int id = mCursor.getInt(1);//id of movie
                String title = mCursor.getString(2); // original_title
                String poster = mCursor.getString(4); // poster_path
                String background = mCursor.getString(5); // backdrop_path
                String overview = mCursor.getString(3); // overview
                double rating = mCursor.getDouble(6); // vote_average
                double popularity = mCursor.getDouble(8); // vote_average
                String date = mCursor.getString(9); // release_date
                int vote_count = mCursor.getInt(7);
                Movie movie = new Movie(id, title, poster, background, overview, rating, date, popularity, vote_count);
                holder.bind(movie, mlistener);
            }
        }
        else
        holder.bind(movieList.get(position), mlistener);

    }




    @Override
    public int getItemCount() {
        if (fromDB) {
            return (mCursor == null) ? 0 : mCursor.getCount();

        }
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
