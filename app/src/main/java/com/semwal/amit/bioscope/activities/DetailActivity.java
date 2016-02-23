package com.semwal.amit.bioscope.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.semwal.amit.bioscope.R;
import com.semwal.amit.bioscope.fragments.DetailsFragment;
import com.semwal.amit.bioscope.utils.Constants;

public class DetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final FloatingActionButton fav_btn = (FloatingActionButton) findViewById(R.id.fav_movie_button);
        fav_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Favourite Movie", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                //fav_btn.setImageDrawable("");
            }
        });




        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(Constants.LocalKeys.DETAIL_MOVIE_KEY,
                    getIntent().getParcelableExtra(Constants.LocalKeys.DETAIL_MOVIE_KEY));

            DetailsFragment fragment = new DetailsFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();
        }
    }
}
