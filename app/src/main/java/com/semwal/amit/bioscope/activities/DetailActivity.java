package com.semwal.amit.bioscope.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.semwal.amit.bioscope.R;
import com.semwal.amit.bioscope.Utility;
import com.semwal.amit.bioscope.fragments.DetailActivityFragment;

public class DetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(Utility.DETAIL_MOVIE_KEY,
                    getIntent().getParcelableExtra(Utility.DETAIL_MOVIE_KEY));

            DetailActivityFragment fragment = new DetailActivityFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();
        }
    }
}
