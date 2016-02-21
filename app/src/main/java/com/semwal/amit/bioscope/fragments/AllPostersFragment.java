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

import com.semwal.amit.bioscope.FetchAsyncTask;
import com.semwal.amit.bioscope.Movie;
import com.semwal.amit.bioscope.MovieDataAdapter;
import com.semwal.amit.bioscope.R;
import com.semwal.amit.bioscope.Utility;
import com.semwal.amit.bioscope.activities.DetailActivity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class AllPostersFragment extends Fragment {
    public static final String MOVIES_KEY = "moviesARRAYLIST";
    @Bind(R.id.gridview_movies)
    GridView mGridView;
    private MovieDataAdapter dataAdapter;
    private ArrayList<Movie> movies;
    private String TAG = AllPostersFragment.class.getSimpleName();
    private String mSortBy = Utility.SORT_POPULARITY_DESC;

    public AllPostersFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        ButterKnife.bind(this, view);
        dataAdapter = new MovieDataAdapter(getActivity());
        if (savedInstanceState != null && savedInstanceState.containsKey(MOVIES_KEY)) {
            mSortBy = savedInstanceState.getString(Utility.SORT_SETTING_KEY);
            movies = savedInstanceState.getParcelableArrayList(MOVIES_KEY);
        }
        updateMovies(mSortBy);
        mGridView.setAdapter(dataAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = dataAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Utility.DETAIL_MOVIE_KEY, movie);
                startActivity(intent);
            }
        });


        return view;
    }

    private void updateMovies(String sort_by) {
        if (movies != null && dataAdapter != null) {
            dataAdapter.clear();
            dataAdapter.addAll(movies);
            return;
        }
        FetchAsyncTask moviesTask = new FetchAsyncTask(new FetchAsyncTask.AsyncResponse() {
            @Override
            public void processFinish(ArrayList<Movie> output) {
                Log.i(TAG, "processFinish: s");
                if (output != null && dataAdapter != null) {
                    dataAdapter.clear();
                    movies = new ArrayList<>();
                    movies.addAll(output);
                    dataAdapter.addAll(movies);
                }
            }
        });
        moviesTask.execute(sort_by);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (movies != null) {
            outState.putParcelableArrayList(MOVIES_KEY, movies);

            outState.putString(Utility.SORT_SETTING_KEY, mSortBy);
            super.onSaveInstanceState(outState);

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
        MenuItem action_sort_by_popularity = menu.findItem(R.id.action_sort_by_popularity);
        MenuItem action_sort_by_rating = menu.findItem(R.id.action_sort_by_rating);
        if (mSortBy.contentEquals(Utility.SORT_POPULARITY_DESC)) {
            if (!action_sort_by_popularity.isChecked())
                action_sort_by_popularity.setChecked(true);
        } else {
            if (!action_sort_by_rating.isChecked())
                action_sort_by_rating.setChecked(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_sort_by_popularity:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                mSortBy = Utility.SORT_POPULARITY_DESC;
                movies = null;
                updateMovies(mSortBy);
                return true;
            case R.id.action_sort_by_rating:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                mSortBy = Utility.SORT_VOTE_AVERAGE_DESC;
                movies = null;
                updateMovies(mSortBy);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
