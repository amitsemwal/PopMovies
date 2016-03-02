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
import com.semwal.amit.bioscope.data.MovieAdapter;
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
public class AllPostersFragment extends Fragment {

    @Bind(R.id.gridview_movies)
    GridView mGridView;
    private MovieAdapter dataAdapter;
    private MovieService movieService;
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
        movieService = MovieClient.createService(MovieService.class);
        ButterKnife.bind(this, view);
        dataAdapter = new MovieAdapter(getActivity());
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
        Call<ApiResult<Movie>> moviesCall = movieService.getMovies(mode);
        moviesCall.enqueue(new Callback<ApiResult<Movie>>() {
            @Override
            public void onResponse(Call<ApiResult<Movie>> call, Response<ApiResult<Movie>> response) {
                List<Movie> movieList = response.body().getResults();
                dataAdapter.clear();
                for (Movie m : movieList) {
                    dataAdapter.add(m);
                }
            }

            @Override
            public void onFailure(Call<ApiResult<Movie>> call, Throwable t) {
                Log.d(TAG, "onFailure: ");
            }
        });
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
