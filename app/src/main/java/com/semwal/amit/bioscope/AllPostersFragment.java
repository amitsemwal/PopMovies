package com.semwal.amit.bioscope;

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

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class AllPostersFragment extends Fragment {
    private static final String SORT_SETTING = "sort_setting";
    private static final String POPULARITY_DESC = "popularity.desc";
    private static final String RATING_DESC = "vote_average.desc";
    private GridView mGridView;
    private MovieDataAdapter dataAdapter;
    private ArrayList<MovieData> movies;
    private String TAG = AllPostersFragment.class.getSimpleName();
    private String mSortBy = POPULARITY_DESC;

    public AllPostersFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: inside");
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mGridView = (GridView) view.findViewById(R.id.gridview_movies);

        dataAdapter = new MovieDataAdapter(getActivity());
        if (savedInstanceState != null && savedInstanceState.containsKey("movies")) {
            mSortBy = savedInstanceState.getString(SORT_SETTING);

            movies = savedInstanceState.getParcelableArrayList("movies");
        }
        updateMovies(mSortBy);
        mGridView.setAdapter(dataAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieData movie = dataAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra("MOVIE", movie);
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
        FetchMovieData moviesTask = new FetchMovieData(new FetchMovieData.AsyncResponse() {
            @Override
            public void processFinish(ArrayList<MovieData> output) {
                Log.i(TAG, "processFinish: s");
                if (output != null && dataAdapter != null) {
                    dataAdapter.clear();
                    movies = new ArrayList<>();
                    movies.addAll(output);
                    Log.i(TAG, "processFinish: " + movies.size());
                    dataAdapter.addAll(movies);
                }
            }
        });
        moviesTask.execute(sort_by);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (movies != null) {
            outState.putParcelableArrayList("movies", movies);

            outState.putString(SORT_SETTING, mSortBy);
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
        if (mSortBy.contentEquals(POPULARITY_DESC)) {
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
                mSortBy = POPULARITY_DESC;
                movies = null;
                updateMovies(mSortBy);
                return true;
            case R.id.action_sort_by_rating:
                if (item.isChecked())
                    item.setChecked(false);
                else
                    item.setChecked(true);
                mSortBy = RATING_DESC;
                movies = null;
                updateMovies(mSortBy);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
