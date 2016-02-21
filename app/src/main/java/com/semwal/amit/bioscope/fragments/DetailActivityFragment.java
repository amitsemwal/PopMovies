package com.semwal.amit.bioscope.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.semwal.amit.bioscope.Movie;
import com.semwal.amit.bioscope.R;
import com.semwal.amit.bioscope.Utility;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

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

    public DetailActivityFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mMovie = arguments.getParcelable(Utility.DETAIL_MOVIE_KEY);
        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, rootView);

        String image_url = Utility.IMAGE_URL_HIGH_QUALITY + mMovie.getBackground();
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

        mVoteAverageView.setText(Integer.toString(mMovie.getRating()));

        return rootView;
    }

}
