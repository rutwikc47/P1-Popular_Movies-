package rccorp.movies.ASyncTasks;


import android.content.AsyncTaskLoader;
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

import rccorp.movies.Lists.Reviews;

public class ReviewSync extends AsyncTaskLoader<List<Reviews>> {


    private String rurl;

    public ReviewSync(Context context, String itemid) {
        super(context);
        rurl = "http://api.themoviedb.org/3/movie/" + itemid + "/reviews?&api_key="""""""Your API KEY """""""""""""""""";
    }


    @Override
    public List<Reviews> loadInBackground() {


        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String moviesdb = null;

        try {
            URL url = new URL(rurl);

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
            Log.e("TrailerSync", "Error ", e);
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
                    Log.e("TrailerSync", "Error closing stream", e);
                }
            }
        }
        try {
            return getReviewdatafromJSON(moviesdb);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void deliverResult(List<Reviews> data) {
        super.deliverResult(data);
    }

    public List<Reviews> getReviewdatafromJSON(String moviesdbst)
            throws JSONException {
        List<Reviews> reviewsList = new ArrayList<>();

        JSONObject TMDBTJson = new JSONObject(moviesdbst);
        JSONArray resultsarr = TMDBTJson.getJSONArray("results");

        if (resultsarr != null) {

            for (int i = 0; i < resultsarr.length(); i++) {
                Reviews currReview = new Reviews();
                JSONObject results = resultsarr.getJSONObject(i);
                currReview.setAuthor(results.getString("author"));
                currReview.setHisreview(results.getString("content"));
                reviewsList.add(currReview);

            }
        }
        return reviewsList;
    }
}