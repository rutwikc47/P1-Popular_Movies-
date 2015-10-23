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

import rccorp.movies.Lists.Trailers;

public class TrailerSync extends AsyncTaskLoader<List<Trailers>>{

    private String tUrl;

    public TrailerSync(Context context, String itemid) {
        super(context);
        tUrl = "http://api.themoviedb.org/3/movie/" +itemid + "/videos?&api_key=496b4a41cb11baf342d1a13bb3e6f6f5";
    }

    @Override
    public List<Trailers> loadInBackground() {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String moviesdb = null;

        try {
            URL url=new URL(tUrl);

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
            return getTrailerdatafromJSON(moviesdb);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public void deliverResult(List<Trailers> data) {
        super.deliverResult(data);
    }

    public List<Trailers> getTrailerdatafromJSON(String moviesdbstring)
        throws JSONException {
        List<Trailers> trailersList=new ArrayList<>();

        JSONObject TMDBTJson=new JSONObject(moviesdbstring);
        JSONArray resultsarr=TMDBTJson.getJSONArray("results");

        if (resultsarr!=null){

            for (int i=0; i < resultsarr.length(); i++){
                Trailers currTrailer=new Trailers();
                JSONObject results=resultsarr.getJSONObject(i);
                currTrailer.setTrailerkey(results.getString("key"));
                currTrailer.setTrailername(results.getString("name"));
                currTrailer.setTrailerid(results.getString("id"));
                trailersList.add(currTrailer);

            }

        }
        return trailersList;
    }

}



