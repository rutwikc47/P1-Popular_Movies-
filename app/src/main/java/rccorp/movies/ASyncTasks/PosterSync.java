package rccorp.movies.ASyncTasks;


import android.support.v4.content.AsyncTaskLoader;

import android.content.Context;
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
import java.util.List;

import rccorp.movies.Lists.Movie;

public class PosterSync extends AsyncTaskLoader<List<Movie>> {
    private static final String TAG = "PosterSync";

    private String mUrl;
    public PosterSync(Context context, String url) {
        super(context);
        this.mUrl = url;
    }

    @Override
    public List<Movie> loadInBackground() {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String moviesdb = null;

        try {
            // Construct the URL for the Moviesdb query

            URL url = new URL(mUrl);
            // Create the request to TMDB, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
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
                return null;
            }
            moviesdb = buffer.toString();
        } catch (IOException e) {
            Log.e("PosterSync", "Error ", e);
            // If the code didn't successfully get the Movies data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PosterSync", "Error closing stream", e);
                }
            }
        }

        try {
            return getMoviesDatafromJSON(moviesdb);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void deliverResult(List<Movie> data) {
        super.deliverResult(data);
    }

    private List<Movie> getMoviesDatafromJSON(String moviesdbStr)
            throws JSONException {
        List<Movie> moviesList = new ArrayList<>();

        JSONObject TMDBJson = new JSONObject(moviesdbStr);
        JSONArray resultarr = TMDBJson.getJSONArray("results");
        if(resultarr != null) {

            for (int i = 0; i < resultarr.length(); i++) {
                Movie currentMovie = new Movie();
                JSONObject result = resultarr.getJSONObject(i);
                currentMovie.setPosterUrl(result.getString("poster_path"));
                currentMovie.setName(result.getString("original_title"));
                currentMovie.setOverview(result.getString("overview"));
                currentMovie.setUserrating(result.getString("vote_average"));
                currentMovie.setReleasedate(result.getString("release_date"));
                currentMovie.setId(result.getString("id"));
                moviesList.add(currentMovie);
            }
        }

        return moviesList;

    }




}

