package com.semwal.amit.bioscope.network;

import com.semwal.amit.bioscope.models.ApiResult;
import com.semwal.amit.bioscope.models.Movie;
import com.semwal.amit.bioscope.models.Review;
import com.semwal.amit.bioscope.models.Trailer;
import com.semwal.amit.bioscope.utils.Constants;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 *
 */
public interface MovieService {

    @GET("{mode}" + Constants.Api.API_KEY_PARAM)
    Call<ApiResult<Movie>> getMovies(@Path("mode") String mode);

    @GET("{id}/reviews" + Constants.Api.API_KEY_PARAM)
    Call<ApiResult<Review>> getReviews(@Path("id") long id);

    @GET("{id}/videos" + Constants.Api.API_KEY_PARAM)
    Call<ApiResult<Trailer>> getTrailers(@Path("id") long id);

}
