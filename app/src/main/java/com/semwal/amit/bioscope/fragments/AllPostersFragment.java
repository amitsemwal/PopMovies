package com.semwal.amit.bioscope.fragments;

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
import android.view.View;
import android.view.ViewGroup;

import com.semwal.amit.bioscope.R;
import com.semwal.amit.bioscope.data.MovieAdapter;
import com.semwal.amit.bioscope.data.MovieContract;
import com.semwal.amit.bioscope.models.ApiResult;
import com.semwal.amit.bioscope.models.Movie;
import com.semwal.amit.bioscope.network.MovieClient;
import com.semwal.amit.bioscope.network.MovieService;
import com.semwal.amit.bioscope.utils.Constants;

import java.util.ArrayList;

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
    private MovieAdapter mFavouriteAdapter;
    private MovieService mMovieService;
    private Cursor mCursor;
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

      mFavouriteAdapter = new MovieAdapter(getActivity(), mCursor, this);
        onViewStateRestored(savedInstanceState);
        updateMovies(mode);
        return view;
    }

    public void updateMovies(final String mode) {
        if (mode == Constants.LocalKeys.FAVOURITES) {
            mRecycleGridView.setAdapter(mFavouriteAdapter);
            mRecycleGridView.setLayoutManager(new GridLayoutManager(getContext(),getResources().getInteger(R.integer.movie_columns)));



        } else {
            mRecycleGridView.setAdapter(mMovieAdapter);
            mRecycleGridView.setLayoutManager(new GridLayoutManager(getContext(),getResources().getInteger(R.integer.movie_columns)));

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
        mFavouriteAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
      mFavouriteAdapter.swapCursor(null);
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
