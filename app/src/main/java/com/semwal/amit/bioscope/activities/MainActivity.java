package com.semwal.amit.bioscope.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.semwal.amit.bioscope.R;
import com.semwal.amit.bioscope.data.DatabaseWrapper;
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

    public boolean mTwoPane;
    private String mSortMode = Constants.LocalKeys.MOST_POPULAR;
    private Movie mMovie;

    FloatingActionButton fav_btn;
    DatabaseWrapper db;
    boolean fav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(myToolbar);
        if (findViewById(R.id.movie_detail_container) != null) {
          mTwoPane = true;
            fav_btn = (FloatingActionButton) findViewById(R.id.fav_movie_button);

            db = new DatabaseWrapper(this);
             fav_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Favourite Movie", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    long id = db.addMovie(mMovie.getId(), mMovie.getTitle(), mMovie.getPosterpath(), mMovie.getBackground(), mMovie.getOverview(), mMovie.getRating(), mMovie.getDate(), mMovie.getPopularity(), mMovie.getVote_count());
                    if (fav) {
                        int i = (int) db.removeMovie(mMovie.getId());
                        if (i == 1)
                            Snackbar.make(view, String.format("%s removed from favourites", mMovie.getTitle()), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        fav_btn.setImageDrawable(ContextCompat.getDrawable(view.getContext(),R.drawable.unfav));
                    } else {
                        Snackbar.make(view, String.format("%s added to favourites", mMovie.getTitle()), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        fav_btn.setImageDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.fav));
                    }
                    fav = !fav;
                    //fav_btn.setImageDrawable("");
                }
            });
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
        mMovie=movie;
        if (mTwoPane) {

            if (db.isFavourite(mMovie.getId())) {

                fav_btn.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.fav));
                fav = true;
            }
            else
                fav_btn.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.unfav));



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

