package com.semwal.amit.bioscope.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.semwal.amit.bioscope.R;
import com.semwal.amit.bioscope.data.DatabaseWrapper;
import com.semwal.amit.bioscope.fragments.DetailsFragment;
import com.semwal.amit.bioscope.models.Movie;
import com.semwal.amit.bioscope.utils.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = "DetailActivity";
    @Bind(R.id.fav_movie_button)
    FloatingActionButton fav_btn;
    DatabaseWrapper db;
    boolean fav;
    private Movie mMovie;
    private String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mMovie = getIntent().getParcelableExtra(Constants.LocalKeys.DETAIL_MOVIE_KEY);
        setTitle(mMovie.getTitle());
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            mode =  getIntent().getStringExtra(Constants.Api.SORT_KEY_PARAM);
            arguments.putParcelable(Constants.LocalKeys.DETAIL_MOVIE_KEY,
                    mMovie);
            arguments.putString(Constants.Api.SORT_KEY_PARAM,mode);
            DetailsFragment fragment = new DetailsFragment();

            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();
        }

        db = new DatabaseWrapper(this);

        if (db.isFavourite(mMovie.getId())) {

            fav_btn.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.fav));
            fav = true;
        }
        else
            fav_btn.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.unfav));


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
                    Snackbar.make(view, String.format("%s added to" +
                            " favourites", mMovie.getTitle()), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    fav_btn.setImageDrawable(ContextCompat.getDrawable(view.getContext(), R.drawable.fav));
                }
                fav = !fav;
                //fav_btn.setImageDrawable("");
            }
        });
    }
}
