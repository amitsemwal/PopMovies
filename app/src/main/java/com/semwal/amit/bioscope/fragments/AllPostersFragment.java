package com.semwal.amit.bioscope.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.semwal.amit.bioscope.data.Movie;
import com.semwal.amit.bioscope.data.MovieDataAdapter;
import com.semwal.amit.bioscope.network.FetchAsyncTask;
import com.semwal.amit.bioscope.utils.Constants;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class AllPostersFragment extends Fragment {

    @Bind(R.id.gridview_movies)
    GridView mGridView;
    private MovieDataAdapter dataAdapter;
    private ArrayList<Movie> PopularMovies;
    private ArrayList<Movie> TopRatedMovies;
    private ArrayList<Movie> FavoritesMovies;
    private String TAG = AllPostersFragment.class.getSimpleName();
    private String mode = Constants.LocalKeys.MOST_POPULAR;

    public AllPostersFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        Log.d(TAG, "onCreateView: ");


        ButterKnife.bind(this, view);
        dataAdapter = new MovieDataAdapter(getActivity());
        onViewStateRestored(savedInstanceState);
        updateMovies(mode);
        mGridView.setAdapter(dataAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = dataAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Constants.LocalKeys.DETAIL_MOVIE_KEY, movie);
                startActivity(intent);
            }
        });


        return view;
    }

    private void updateMovies(String mode) {
        dataAdapter.clear();
        switch (this.mode) {
            case Constants.LocalKeys.HIGHEST_RATED: {
                if (TopRatedMovies == null) {
                    FetchAsyncTask moviesTask = new FetchAsyncTask(new FetchAsyncTask.AsyncResponse() {
                        @Override
                        public void processFinish(ArrayList<Movie> output) {
                            TopRatedMovies = new ArrayList<>();
                            TopRatedMovies.addAll(output);
                            dataAdapter.addAll(output);

                        }
                    }, getContext());
                    moviesTask.execute(mode);
                } else dataAdapter.addAll(TopRatedMovies);
                break;
            }
            case Constants.LocalKeys.FAVOURITES: {
                if (FavoritesMovies == null) {
                    FetchAsyncTask moviesTask = new FetchAsyncTask(new FetchAsyncTask.AsyncResponse() {
                        @Override
                        public void processFinish(ArrayList<Movie> output) {
                            FavoritesMovies = new ArrayList<>();
                            FavoritesMovies.addAll(output);
                            dataAdapter.addAll(output);

                        }
                    }, getContext());
                    moviesTask.execute(mode);
                } else dataAdapter.addAll(FavoritesMovies);
                break;
            }
            case Constants.LocalKeys.MOST_POPULAR: {
                if (PopularMovies == null) {
                    FetchAsyncTask moviesTask = new FetchAsyncTask(new FetchAsyncTask.AsyncResponse() {
                        @Override
                        public void processFinish(ArrayList<Movie> output) {
                            PopularMovies = new ArrayList<>();
                            PopularMovies.addAll(output);
                            dataAdapter.addAll(output);

                        }
                    }, getContext());
                    moviesTask.execute(mode);
                } else dataAdapter.addAll(PopularMovies);
                break;
            }
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: Saving " + mode);
        switch (mode) {
            case Constants.LocalKeys.HIGHEST_RATED: {
                if (TopRatedMovies != null)
                    outState.putParcelableArrayList(mode, TopRatedMovies);
                break;
            }
            case Constants.LocalKeys.FAVOURITES: {
                if (FavoritesMovies != null)
                    outState.putParcelableArrayList(mode, FavoritesMovies);
                break;
            }
            case Constants.LocalKeys.MOST_POPULAR: {
                if (PopularMovies != null)
                    outState.putParcelableArrayList(mode, PopularMovies);
                break;
            }
        }
        outState.putString(Constants.LocalKeys.VIEW_MODE_KEY, mode);
        super.onSaveInstanceState(outState);


    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d(TAG, "onViewStateRestored: Restoring ");
        if (savedInstanceState != null) {
            mode = savedInstanceState.getString(Constants.LocalKeys.VIEW_MODE_KEY);
            Log.d(TAG, "onViewStateRestored: " + mode);
            switch (mode) {
                case Constants.LocalKeys.MOST_POPULAR:
                    PopularMovies = savedInstanceState.getParcelableArrayList(mode);
                    break;
                case Constants.LocalKeys.HIGHEST_RATED:
                    TopRatedMovies = savedInstanceState.getParcelableArrayList(mode);
                    break;
                case Constants.LocalKeys.FAVOURITES:
                    FavoritesMovies = savedInstanceState.getParcelableArrayList(mode);
                    break;
            }
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem viewMostPopularMenuItem = menu.findItem(R.id.action_view_most_popular);
        MenuItem viewTopRatedMenuItem = menu.findItem(R.id.action_view_highest_rated);
        MenuItem viewFavouritesMenuItem = menu.findItem(R.id.action_view_favourites);
        if (mode.contentEquals(Constants.LocalKeys.MOST_POPULAR)) {
            if (!viewMostPopularMenuItem.isChecked())
                viewMostPopularMenuItem.setChecked(true);
        } else if (mode.contentEquals(Constants.LocalKeys.HIGHEST_RATED)) {
            if (!viewTopRatedMenuItem.isChecked())
                viewTopRatedMenuItem.setChecked(true);
            else {
                if (!viewFavouritesMenuItem.isChecked())
                    viewFavouritesMenuItem.setChecked(true);
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_view_most_popular:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                mode = Constants.LocalKeys.MOST_POPULAR;
                updateMovies(mode);
                return true;
            case R.id.action_view_highest_rated:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                mode = Constants.LocalKeys.HIGHEST_RATED;
                updateMovies(mode);
                return true;
            case R.id.action_view_favourites:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                mode = Constants.LocalKeys.FAVOURITES;
                updateMovies(mode);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
