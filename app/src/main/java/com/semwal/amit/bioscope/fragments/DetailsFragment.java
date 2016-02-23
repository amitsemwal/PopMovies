package com.semwal.amit.bioscope.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.semwal.amit.bioscope.R;
import com.semwal.amit.bioscope.data.Movie;
import com.semwal.amit.bioscope.utils.Constants;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsFragment extends Fragment {

    private static final String TAG = DetailsFragment.class.getSimpleName();
    @Bind(R.id.detail_image)
    ImageView mImageView;
    @Bind(R.id.detail_title)
    TextView mTitleView;
    @Bind(R.id.detail_overview)
    TextView mOverviewView;
    @Bind(R.id.detail_date)
    TextView mDateView;
    @Bind(R.id.detail_vote_average)
    TextView mVoteAverageView;
    private Movie mMovie;
    private String shareString;

    public DetailsFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mMovie = arguments.getParcelable(Constants.LocalKeys.DETAIL_MOVIE_KEY);
            shareString = "Checkout this exciting movie. " + mMovie.getTitle();
        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, rootView);

        String image_url = Constants.Api.IMAGE_URL_HIGH_QUALITY + mMovie.getBackground();
        Picasso.with(getContext()).load(image_url).into(mImageView);

        mTitleView.setText(mMovie.getTitle());
        mOverviewView.setText(mMovie.getOverview());

        String movie_date = mMovie.getDate();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String date = DateUtils.formatDateTime(getActivity(),
                    formatter.parse(movie_date).getTime(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);
            mDateView.setText(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mVoteAverageView.setText(Double.toString(mMovie.getRating()) + "(" + Integer.toString(mMovie.getVote_count()) + " Votes )");

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_detail, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        ShareActionProvider mShareActionProvider =
                (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // Attach an intent to this ShareActionProvider.  You can update this at any time,
        // like when the user selects a new piece of data they might like to share.
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        } else {
            Log.d(TAG, "Share Action Provider is null?");
        }
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        Intent intent = shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,
                shareString + Constants.LocalKeys.HASHTAG);
        return shareIntent;
    }

}
