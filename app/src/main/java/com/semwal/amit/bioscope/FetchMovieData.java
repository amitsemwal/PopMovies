package com.semwal.amit.bioscope;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Amit on 28-Jan-16.
 */
public class FetchMovieData extends AsyncTask <String, Void, ArrayList<MovieData>>{


    private final String TAG = FetchMovieData.class.getSimpleName();

    private ArrayList<MovieData> getMoviesDataFromJson(String jsonStr) throws JSONException {
        JSONObject movieJson = new JSONObject(jsonStr);
        JSONArray movieArray = movieJson.getJSONArray("results");

        ArrayList<MovieData> results = new ArrayList<>();

        for(int i = 0; i < movieArray.length(); i++) {
            JSONObject movie = movieArray.getJSONObject(i);
            MovieData movieModel = new MovieData(
                    movie.getInt("id"),
                    movie.getString("original_title"),
                    movie.getString("poster_path"),
                    movie.getString("backdrop_path"),
                    movie.getString("overview"),
                    movie.getInt("vote_average"),
                    movie.getString("release_date")
            );
          //  Log.i(TAG, "getMoviesDataFromJson: "+ movieModel.getTitle());
            results.add(movieModel);
        }

        return results;
    }
    @Override
    protected ArrayList<MovieData> doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String movieJsonStr = null;

        String BASE_URL = "http://api.themoviedb.org/3/discover/movie";
        String MOVIEDB_API_KEY = BuildConfig.MOVIEDB_API_KEY;
        try {
            Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter("sort_by", params[0])
                    .appendQueryParameter("api_key", MOVIEDB_API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                movieJsonStr= null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                movieJsonStr = null;
            }
            movieJsonStr = buffer.toString();
        } catch (IOException e) {
            movieJsonStr = null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(TAG, "Error closing stream", e);
                }
            }

        }
        try {
            Log.i(TAG, "doInBackground: JSON " +movieJsonStr);
            return getMoviesDataFromJson(movieJsonStr);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        // This will only happen if there was an error getting or parsing the forecast.
        return null;

    }

    public interface AsyncResponse {
        void processFinish(ArrayList<MovieData> output);
    }

    public AsyncResponse delegate = null;

    public FetchMovieData(AsyncResponse delegate){
        this.delegate = delegate;
    }

    @Override
    protected void onPostExecute(ArrayList<MovieData> result) {
        delegate.processFinish(result);
    }
}
