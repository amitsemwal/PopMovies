package com.semwal.amit.bioscope.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.semwal.amit.bioscope.R;
import com.semwal.amit.bioscope.activities.DetailActivity;
import com.semwal.amit.bioscope.data.MovieAdapter;
import com.semwal.amit.bioscope.data.MovieContract;
import com.semwal.amit.bioscope.data.MovieCursorAdapter;
import com.semwal.amit.bioscope.models.ApiResult;
import com.semwal.amit.bioscope.models.Movie;
import com.semwal.amit.bioscope.network.MovieClient;
import com.semwal.amit.bioscope.network.MovieService;
import com.semwal.amit.bioscope.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class AllPostersFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, MovieAdapter.OnItemClickListener {

    private static final int FAVOURITE_LOADER = 0;
    private final String TAG = AllPostersFragment.class.getSimpleName();
    @Bind(R.id.movies_recycler_view)
    RecyclerView mRecycleGridView;
    private MovieAdapter mMovieAdapter;
    //private MovieCursorAdapter mFavouriteAdapter;
    private MovieService mMovieService;
 ///   private ArrayList<Movie> FavoritesMovies;
    private ArrayList<Movie> mMovies = new ArrayList<>();
    private String mode = Constants.LocalKeys.MOST_POPULAR;

    public AllPostersFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(FAVOURITE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        mMovieService = MovieClient.createService(MovieService.class);
        ButterKnife.bind(this, view);
        mMovieAdapter = new MovieAdapter(getActivity(), mMovies,this);

      //  mFavouriteAdapter = new MovieCursorAdapter(getActivity(), null, 0);
        onViewStateRestored(savedInstanceState);
        updateMovies(mode);
        return view;
    }

    public void updateMovies(final String mode) {
        if (mode == Constants.LocalKeys.FAVOURITES) {
//            mGridView.setAdapter(mFavouriteAdapter);
//            mGridView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                    // CursorAdapter returns a cursor at the correct position for getItem(), or null
//                    // if it cannot seek to that position.
//                    Cursor mCursor = (Cursor) adapterView.getItemAtPosition(position);
//                    if (mCursor != null) {
//                        int id = mCursor.getInt(1);//id of movie
//                        String title = mCursor.getString(2); // original_title
//                        String poster = mCursor.getString(4); // poster_path
//                        String background = mCursor.getString(5); // backdrop_path
//                        String overview = mCursor.getString(3); // overview
//                        double rating = mCursor.getDouble(6); // vote_average
//                        double popularity = mCursor.getDouble(8); // vote_average
//                        String date = mCursor.getString(9); // release_date
//                        int vote_count = mCursor.getInt(7);
//                        Movie movie = new Movie(id, title, poster, background, overview, rating, date, popularity, vote_count);
//                        ((Communication) getActivity())
//                                .onItemSelected(mode,movie);
//                    }
//                }
//
//            });

        } else {
            mRecycleGridView.setAdapter(mMovieAdapter);
            mRecycleGridView.setLayoutManager(new GridLayoutManager(getContext(),2));

            Call<ApiResult<Movie>> moviesCall = mMovieService.getMovies(mode);
            moviesCall.enqueue(new Callback<ApiResult<Movie>>() {
                @Override
                public void onResponse(Call<ApiResult<Movie>> call, Response<ApiResult<Movie>> response) {
                    if(!mMovies.equals(response.body().getResults())) {
                        mMovies.clear();
                        mMovies.addAll(response.body().getResults());
                        mMovieAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<ApiResult<Movie>> call, Throwable t) {
                    Log.d(TAG, "onFailure: ");
                }
            });

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(Constants.LocalKeys.VIEW_MODE_KEY, mode);
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mode = savedInstanceState.getString(Constants.LocalKeys.VIEW_MODE_KEY);
            }

        }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), MovieContract.MovieEntry.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        //mFavouriteAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
     //   mFavouriteAdapter.swapCursor(null);
    }

    @Override
    public void onItemClick(Movie movie) {
        ((Communication) getActivity())
                .onItemSelected(mode, movie);

    }

    public interface Communication {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(String mode, Movie movie);
    }


}
