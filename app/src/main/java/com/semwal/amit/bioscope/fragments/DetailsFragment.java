package com.semwal.amit.bioscope.fragments;

import android.content.Intent;
import android.net.Uri;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.semwal.amit.bioscope.R;
import com.semwal.amit.bioscope.data.ReviewAdapter;
import com.semwal.amit.bioscope.data.TrailerAdapter;
import com.semwal.amit.bioscope.models.ApiResult;
import com.semwal.amit.bioscope.models.Movie;
import com.semwal.amit.bioscope.models.Review;
import com.semwal.amit.bioscope.models.Trailer;
import com.semwal.amit.bioscope.network.MovieClient;
import com.semwal.amit.bioscope.network.MovieService;
import com.semwal.amit.bioscope.utils.Constants;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private String mode;
    private String shareString;

    private ReviewAdapter reviewAdapter;

    private TrailerAdapter trailerAdapter;

    private MovieService movieService;

    public DetailsFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mMovie = arguments.getParcelable(Constants.LocalKeys.DETAIL_MOVIE_KEY);
            mode = arguments.getString(Constants.Api.SORT_KEY_PARAM);
            shareString = "Checkout this exciting movie. " + mMovie.getTitle();
        }

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, rootView);
        if (mMovie != null) {

            String image_url = Constants.Api.IMAGE_URL_HIGH_QUALITY + mMovie.getBackground();
            Picasso.with(getContext()).load(image_url).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(mImageView);
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

            mVoteAverageView.setText(Double.toString(mMovie.getRating()) + "(" + Long.toString(mMovie.getVote_count()) + " Votes )");

            final List<Review> reviews = new ArrayList<>();
            final List<Trailer> trailers = new ArrayList<>();

            reviewAdapter = new ReviewAdapter(getActivity(), reviews);
            trailerAdapter = new TrailerAdapter(getActivity(), trailers);

            ListView reviewList = (ListView) rootView.findViewById(R.id.review_list);
            reviewList.setAdapter(reviewAdapter);

            ListView trailerList = (ListView) rootView.findViewById(R.id.trailer_list);
            trailerList.setAdapter(trailerAdapter);

            trailerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String youtubeVideoId = trailers.get(position).getKey();
                    String videoURI = "vnd.youtube:" + youtubeVideoId;
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(videoURI));
                    startActivity(i);
                }
            });

            movieService = MovieClient.createService(MovieService.class);

            fetchReviews();
            fetchTrailers();

        }

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
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareString + Constants.LocalKeys.HASHTAG);
        return shareIntent;
    }

//
//    private void updateMovies(String mode) {
//        Call<ApiResult<Movie>> moviesCall = movieService.getMovies(mode);
//        moviesCall.enqueue(new Callback<ApiResult<Movie>>() {
//            @Override
//            public void onResponse(Call<ApiResult<Movie>> call, Response<ApiResult<Movie>> response) {
//                List<Movie> movieList = response.body().getResults();
//                dataAdapter.clear();
//                for (Movie m : movieList) {
//                    dataAdapter.add(m);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ApiResult<Movie>> call, Throwable t) {
//                Log.d(TAG, "onFailure: ");
//            }
//        });
//    }

    private void fetchReviews() {
        Call<ApiResult<Review>> reviewCall = movieService.getReviews(mMovie.getId());
        reviewCall.enqueue(new Callback<ApiResult<Review>>() {
            @Override
            public void onResponse(Call<ApiResult<Review>> call, Response<ApiResult<Review>> response) {
                Log.d(TAG, "onResponse: " + response.isSuccess());

                List<Review> reviewList = response.body().getResults();
                reviewAdapter.clear();
                for (Review r : reviewList) {
                    reviewAdapter.add(r);
                }
            }

            @Override
            public void onFailure(Call<ApiResult<Review>> call, Throwable t) {
                Log.d(TAG, "onFailure: ");
            }
        });
    }

    private void fetchTrailers() {
        Call<ApiResult<Trailer>> trailerCall = movieService.getTrailers(mMovie.getId());
        trailerCall.enqueue(new Callback<ApiResult<Trailer>>() {
            @Override
            public void onResponse(Call<ApiResult<Trailer>> call, Response<ApiResult<Trailer>> response) {
                List<Trailer> trailerList = response.body().getResults();
                trailerAdapter.clear();
                for (Trailer r : trailerList) {
                    trailerAdapter.add(r);
                }
            }

            @Override
            public void onFailure(Call<ApiResult<Trailer>> call, Throwable t) {
                Log.d(TAG, "onFailure: ");
            }
        });
    }
}
