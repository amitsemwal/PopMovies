package com.semwal.amit.bioscope.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.semwal.amit.bioscope.R;
import com.semwal.amit.bioscope.data.InsertToDatabase;
import com.semwal.amit.bioscope.data.Movie;
import com.semwal.amit.bioscope.fragments.DetailsFragment;
import com.semwal.amit.bioscope.utils.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {
    @Bind(R.id.fav_movie_button)
    FloatingActionButton fav_btn;
    InsertToDatabase db;
    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = new InsertToDatabase(this);
        fav_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Favourite Movie", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                long id = db.addMovie(mMovie.getId(), mMovie.getTitle(), mMovie.getPoster(), mMovie.getBackground(), mMovie.getOverview(), mMovie.getRating(), mMovie.getDate(), mMovie.getPopularity(), mMovie.getVote_count());

                Snackbar.make(view, "Favourite Movie" + id, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                //fav_btn.setImageDrawable("");
            }
        });


        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            mMovie = getIntent().getParcelableExtra(Constants.LocalKeys.DETAIL_MOVIE_KEY);
            arguments.putParcelable(Constants.LocalKeys.DETAIL_MOVIE_KEY,
                    mMovie);

            DetailsFragment fragment = new DetailsFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();
        }
    }
}
