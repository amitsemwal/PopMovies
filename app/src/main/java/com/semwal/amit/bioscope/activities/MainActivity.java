package com.semwal.amit.bioscope.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.semwal.amit.bioscope.R;
import com.semwal.amit.bioscope.fragments.AllPostersFragment;
import com.semwal.amit.bioscope.fragments.DetailsFragment;
import com.semwal.amit.bioscope.models.Movie;
import com.semwal.amit.bioscope.utils.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements AllPostersFragment.Communication {
    private String TAG = MainActivity.class.getSimpleName();
    private static final String DETAILFRAGMENT_TAG = "DFTAG";

    @Bind(R.id.my_toolbar)
    public Toolbar myToolbar;

    private boolean mTwoPane;
    private String mSortMode = Constants.LocalKeys.MOST_POPULAR;
    private Movie mSelectedMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(myToolbar);
        if (findViewById(R.id.movie_detail_container) != null) {
          mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()

                        .replace(R.id.movie_detail_container, new DetailsFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem viewMostPopularMenuItem = menu.findItem(R.id.action_view_most_popular);
        MenuItem viewTopRatedMenuItem = menu.findItem(R.id.action_view_highest_rated);
        MenuItem viewFavouritesMenuItem = menu.findItem(R.id.action_view_favourites);
        if (mSortMode.contentEquals(Constants.LocalKeys.MOST_POPULAR)) {
            if (!viewMostPopularMenuItem.isChecked()) viewMostPopularMenuItem.setChecked(true);
        } else if (mSortMode.contentEquals(Constants.LocalKeys.HIGHEST_RATED)) {
            if (!viewTopRatedMenuItem.isChecked()) viewTopRatedMenuItem.setChecked(true);
            else {
                if (!viewFavouritesMenuItem.isChecked()) viewFavouritesMenuItem.setChecked(true);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_view_most_popular:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                mSortMode = Constants.LocalKeys.MOST_POPULAR;

                updateMovies(mSortMode);
                              return true;
            case R.id.action_view_highest_rated:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                mSortMode = Constants.LocalKeys.HIGHEST_RATED;
                updateMovies(mSortMode);
                return true;
            case R.id.action_view_favourites:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);
                mSortMode = Constants.LocalKeys.FAVOURITES;
                updateMovies(mSortMode);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateMovies(String sortMode) {
        AllPostersFragment ff = (AllPostersFragment)getSupportFragmentManager().findFragmentById(R.id.movies_fragment);
        if ( null != ff ) {
            ff.updateMovies(sortMode);
        }
    }

    @Override
    public void onItemSelected(String mode, Movie movie) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(Constants.LocalKeys.DETAIL_MOVIE_KEY,
                    movie);
            arguments.putString(Constants.Api.SORT_KEY_PARAM, mSortMode);

            DetailsFragment fragment = new DetailsFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment,DETAILFRAGMENT_TAG)
                    .commit();

        } else {
            Intent intent = new Intent(this, DetailActivity.class)
                    .putExtra(Constants.Api.SORT_KEY_PARAM,mSortMode)
                    .putExtra(Constants.LocalKeys.DETAIL_MOVIE_KEY,
                            movie);
            startActivity(intent);
        }
    }

    }

