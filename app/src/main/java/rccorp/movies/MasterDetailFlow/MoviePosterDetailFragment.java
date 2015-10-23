package rccorp.movies.MasterDetailFlow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rccorp.movies.Activities.ReviewsView;
import rccorp.movies.Activities.TrailerView;
import rccorp.movies.R;

/**
 * A fragment representing a single MoviePoster detail screen.
 * This fragment is either contained in a {@link MoviePosterListActivity}
 * in two-pane mode (on tablets) or a {@link MoviePosterDetailActivity}
 * on handsets.
 */
public class MoviePosterDetailFragment extends Fragment {

    public static final String MY_PREFS = "MyPrefsFav";
    public static final String PREF_KEY_FAVORITES = "pref_key_favs";
    public static String INTENT_GET_ID="intent_get_id";
    public static final String mMoviename="movie_name";
    public static final String mPosterart="poster_art";
    public static final String mMovieoverview="overview";
    public static final String mMovierelease="release_date";
    public static final String mMovierating="rating";
    public static final String ARG_KEY_MOVIEW_ID ="id";

    private Context mContext;
    private String dmoviename;
    private String dposterart;
    private String doverview;
    private String drelease;
    private String drating;

    TextView title;
    TextView releasedate;
    TextView userrating;
    TextView overview;
    ImageView posterart;
    Button trailerbutton;
    Button reviewbutton;
    String mMovieId;
    ImageButton favb;


    public MoviePosterDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context c = mContext;
        dmoviename=getArguments().getString(mMoviename);
        doverview=getArguments().getString(mMovieoverview);
        drating=getArguments().getString(mMovierating);
        drelease=getArguments().getString(mMovierelease);
        dposterart=getArguments().getString(mPosterart);
        mMovieId=getArguments().getString(ARG_KEY_MOVIEW_ID);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movieposter_detail, container, false);

        title = (TextView)rootView.findViewById(R.id.title);
        releasedate = (TextView)rootView.findViewById(R.id.releasedate);
        userrating = (TextView)rootView.findViewById(R.id.rating);
        overview = (TextView)rootView.findViewById(R.id.overview);
        posterart = (ImageView)rootView.findViewById(R.id.posterthmb);

        title.setText(dmoviename);
        overview.setText(doverview);
        releasedate.setText(drelease);
        userrating.setText(drating);
        Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185/" + dposterart).into(posterart);

        trailerbutton = (Button)rootView.findViewById(R.id.trailerbutton);
        reviewbutton = (Button)rootView.findViewById(R.id.reviewsbutton);

        trailerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent trailerd = new Intent(getActivity(), TrailerView.class);
                trailerd.putExtra(INTENT_GET_ID, mMovieId);
                startActivity(trailerd);

            }
        });

        reviewbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reviewd = new Intent(getActivity(), ReviewsView.class);
                reviewd.putExtra(INTENT_GET_ID, mMovieId);
                startActivity(reviewd);
            }
        });

        favb=(ImageButton)rootView.findViewById(R.id.favouritebutton);
        favb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appendToFavorites(mMovieId, view.getContext());
                Toast.makeText(view.getContext(),"Added to Favourites",Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;


    }

    public static void appendToFavorites(String movieId, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String favsString = prefs.getString(PREF_KEY_FAVORITES, null);
        if(favsString==null) {
            editor.putString(PREF_KEY_FAVORITES, movieId);
            editor.apply();
            return;
        } else {
            String[] favs = favsString.split(",");
            List<String> favsList = convertStringArrayToList(favs);
            favsList.add(movieId);
            editor.putString(PREF_KEY_FAVORITES, TextUtils.join(",", favsList));
            editor.apply();
            return;
        }
    }

    public static List<String> getFavs(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS, Context.MODE_PRIVATE);
        String favsString = prefs.getString(PREF_KEY_FAVORITES, null);
        if(favsString==null) {
            return null;
        } else {
            String[] favs = favsString.split(",");
            return Arrays.asList(favs);
        }
    }

    public static List<String> convertStringArrayToList(String[] input) {
        List<String> list = new ArrayList<>();
        for(String item : input) {
            list.add(item);
        }
        return list;
    }




}
