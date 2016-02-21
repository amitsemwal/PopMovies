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
public class FetchAsyncTask extends AsyncTask<String, Void, ArrayList<Movie>> {

    private final String TAG = FetchAsyncTask.class.getSimpleName();
    public AsyncResponse delegate = null;

    public FetchAsyncTask(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    private ArrayList<Movie> getMoviesDataFromJson(String jsonStr) throws JSONException {
        JSONObject movieJson = new JSONObject(jsonStr);
        JSONArray movieArray = movieJson.getJSONArray(Utility.JSON_RESULT_TAG);

        ArrayList<Movie> results = new ArrayList<>();

        for (int i = 0; i < movieArray.length(); i++) {
            JSONObject movie = movieArray.getJSONObject(i);
            Movie movieModel = new Movie(
                    movie.getInt(Utility.MOVIE_ID_TAG),
                    movie.getString(Utility.MOVIE_TITLE_TAG),
                    movie.getString(Utility.MOVIE_POSTER_TAG),
                    movie.getString(Utility.MOVIE_BACKGROUND_TAG),
                    movie.getString(Utility.MOVIE_OVERVIEW_TAG),
                    movie.getInt(Utility.MOVIE_VOTE_AVERAGE_TAG),
                    movie.getString(Utility.MOVIE_RELEASE_DATE_TAG)
            );
            results.add(movieModel);
        }

        return results;
    }

    @Override
    protected ArrayList<Movie> doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String movieJsonStr = null;

        String MOVIEDB_API_KEY = BuildConfig.MOVIEDB_API_KEY;
        try {
            Uri builtUri = Uri.parse(Utility.BASE_URL).buildUpon()
                    .appendQueryParameter(Utility.SORT_KEY_PARAM, params[0])
                    .appendQueryParameter(Utility.API_KEY_PARAM, MOVIEDB_API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                movieJsonStr = null;
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
            return getMoviesDataFromJson(movieJsonStr);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        // This will only happen if there was an error getting or parsing the forecast.
        return null;

    }

    @Override
    protected void onPostExecute(ArrayList<Movie> result) {
        delegate.processFinish(result);
    }

    public interface AsyncResponse {
        void processFinish(ArrayList<Movie> output);
    }
}
