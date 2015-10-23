package rccorp.movies.ASyncTasks;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

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
import rccorp.movies.MasterDetailFlow.MoviePosterDetailFragment;

public class FavouriteSync extends AsyncTaskLoader<List<Movie>>{
    private static final String TAG = "FavoriteSync";
    private List<String> mFavsList;
    public FavouriteSync(Context context) {
        super(context);
        mFavsList = MoviePosterDetailFragment.getFavs(context);
        for(String movieId : mFavsList) {
            Log.d(TAG, "movieId " + movieId);
        }
    }

    @Override
    public List<Movie> loadInBackground() {
        List<Movie> favsData = new ArrayList<>();
        if(mFavsList==null) {
            return null;
        }
        for(String movieId : mFavsList) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesdb = null;

            try {
                // Construct the URL for the Moviesdb query

//            URL url = new URL("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=496b4a41cb11baf342d1a13bb3e6f6f5");
                URL url = new URL("http://api.themoviedb.org/3/movie/"+movieId+"?&api_key=496b4a41cb11baf342d1a13bb3e6f6f5");
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
                favsData.add(getfavouritedatafromJSON(moviesdb));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return favsData;
    }

    @Override
    public void deliverResult(List<Movie> data) {
        super.deliverResult(data);
    }

    private Movie getfavouritedatafromJSON(String moviesdbStr)
        throws JSONException{

        JSONObject TMDBJson = new JSONObject(moviesdbStr);
        if (TMDBJson!=null){
            Movie currentMovie = new Movie();
            currentMovie.setPosterUrl(TMDBJson.getString("poster_path"));
            currentMovie.setName(TMDBJson.getString("original_title"));
            currentMovie.setOverview(TMDBJson.getString("overview"));
            currentMovie.setUserrating(TMDBJson.getString("vote_average"));
            currentMovie.setReleasedate(TMDBJson.getString("release_date"));
            currentMovie.setId(TMDBJson.getString("id"));
            return currentMovie;
        }
        return null;
    }

}
