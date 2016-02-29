package com.semwal.amit.bioscope.network;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.semwal.amit.bioscope.BuildConfig;
import com.semwal.amit.bioscope.data.DatabaseWrapper;
import com.semwal.amit.bioscope.data.Movie;
import com.semwal.amit.bioscope.utils.Constants;

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
    DatabaseWrapper databaseWrapper;
    Context mContext;

    public FetchAsyncTask(AsyncResponse delegate, Context context) {
        this.delegate = delegate;
        mContext = context;
    }

    private ArrayList<Movie> getMoviesDataFromJson(String jsonStr) throws JSONException {
        JSONObject movieJson = new JSONObject(jsonStr);
        JSONArray movieArray = movieJson.getJSONArray(Constants.JsonTags.RESULTS);

        ArrayList<Movie> results = new ArrayList<>();

        for (int i = 0; i < movieArray.length(); i++) {
            JSONObject movie = movieArray.getJSONObject(i);
            Movie movieModel = new Movie(
                    movie.getInt(Constants.JsonTags.Summary.MOVIE_ID),
                    movie.getString(Constants.JsonTags.Summary.ORIGINAL_TITLE),
                    movie.getString(Constants.JsonTags.Summary.POSTER_PATH),
                    movie.getString(Constants.JsonTags.Summary.BACKDROP_PATH),
                    movie.getString(Constants.JsonTags.Summary.OVERVIEW),
                    movie.getDouble(Constants.JsonTags.Summary.VOTE_AVERAGE),
                    movie.getString(Constants.JsonTags.Summary.RELEASE_DATE),
                    movie.getDouble(Constants.JsonTags.Summary.POPULARITY),
                    movie.getInt(Constants.JsonTags.Summary.VOTE_COUNT)

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

        if (params[0].equals(Constants.LocalKeys.FAVOURITES)) {
            databaseWrapper = new DatabaseWrapper(mContext);
            return databaseWrapper.getAllMoviesFromDb();
        }
        try {

            Uri builtUri = Uri.parse(Constants.Api.BASE_URL).buildUpon()
                    .appendPath(params[0])
                    .appendQueryParameter(Constants.Api.API_KEY_PARAM, BuildConfig.MOVIEDB_API_KEY)
                    .build();
            Log.d(TAG, "doInBackground: URI " + builtUri.toString());
            URL url = new URL(builtUri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer;
            buffer = new StringBuffer();
            if (inputStream == null) {
                movieJsonStr = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream != null ? inputStream : null));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line).append("\n");
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
