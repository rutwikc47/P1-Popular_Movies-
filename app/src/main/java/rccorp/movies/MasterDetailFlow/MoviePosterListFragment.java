package rccorp.movies.MasterDetailFlow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import rccorp.movies.ASyncTasks.FavouriteSync;
import rccorp.movies.ASyncTasks.PosterSync;
import rccorp.movies.Adapters.PosterAdapter;
import rccorp.movies.Lists.Movie;
import rccorp.movies.R;
import rccorp.movies.RecyclerItemClickListener;

/**
 * A list fragment representing a list of MoviePosters. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link MoviePosterDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class MoviePosterListFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Movie>> {

    private Context mcontext;
    RecyclerView posterview;
    private List<Movie> mMoviesList;
    private boolean mTwoPane;
    private static final int LOADER_ID = 42;
    private static final int LOADER_ID_POPULAR = 43;
    private static final int LOADER_ID_HIGHEST = 44;
    private static final int LOADER_ID_FAVOURITE=45;
    public static final String INTENT_KEY_MOVIE_NAME = "intent_key_movie_name";
    public static final String INTENT_KEY_POSTER_ART="intent_key_poster_art";
    public static final String INTENT_KEY_MOVIE_RATING = "intent_key_movie_rating";
    public static final String INTENT_KEY_MOVIE_OVERVIEW="intent_key_movie_overview";
    public static final String INTENT_KEY_MOVIE_RELEASE = "intent_key_movie_release";
    public static final String INTENT_KEY_MOVIE_ID="intent_key_movie_id";

    private static final String URL_POPULAR = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=496b4a41cb11baf342d1a13bb3e6f6f5";
    private static final String URL_HIGHEST_RATED = "http://api.themoviedb.org/3/discover/movie?certification_country=US&vote_count.gte=1000&sort_by=vote_average.desc&api_key=496b4a41cb11baf342d1a13bb3e6f6f5";

    private PosterAdapter mPosterAdapter;
    private Activity mActivity;



    private static final String STATE_ACTIVATED_POSITION = "activated_position";


    private Callbacks mCallbacks = sDummyCallbacks;

    private int mActivatedPosition = ListView.INVALID_POSITION;

    public interface Callbacks {

        public void onItemSelected(int position);
    }


    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(int position) {

        }

    };


    public MoviePosterListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View posterlist = inflater.inflate(R.layout.fragment_movie_list, container, false);
        mcontext =  posterlist.getContext();


        posterview = (RecyclerView) posterlist.findViewById(R.id.recyclerView);
        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            posterview.setLayoutManager(new GridLayoutManager(mcontext, 2));
        }
        else{
            posterview.setLayoutManager(new GridLayoutManager(mcontext, 4));
        }

        if (savedInstanceState==null || !savedInstanceState.containsKey("key")){
            mMoviesList = new ArrayList<>();
            mPosterAdapter = new PosterAdapter(mMoviesList, mcontext);
            getLoaderManager().initLoader(LOADER_ID, null, this).forceLoad();
        } else {
            mMoviesList=savedInstanceState.getParcelableArrayList("key");
            mPosterAdapter=new PosterAdapter(mMoviesList,mcontext);
            posterview.setVisibility(View.VISIBLE);
        }

        posterview.setAdapter(mPosterAdapter);

        posterview.addOnItemTouchListener(
                new RecyclerItemClickListener(mcontext, new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {

                        if (getActivity().findViewById(R.id.movieposter_detail_container)!=null){
                            mTwoPane=true;
                        }

                        if (mTwoPane){
                            Movie currMovie = mMoviesList.get(position);
                            Bundle arguments = new Bundle();
                            arguments.putString(MoviePosterDetailFragment.ARG_KEY_MOVIEW_ID, currMovie.getId());
                            arguments.putString(MoviePosterDetailFragment.mMovieoverview,currMovie.getOverview());
                            arguments.putString(MoviePosterDetailFragment.mPosterart,currMovie.getPosterUrl());
                            arguments.putString(MoviePosterDetailFragment.mMovierelease,currMovie.getReleasedate());
                            arguments.putString(MoviePosterDetailFragment.mMoviename,currMovie.getName());
                            arguments.putString(MoviePosterDetailFragment.mMovierating,currMovie.getUserrating());
                            MoviePosterDetailFragment fragment = new MoviePosterDetailFragment();
                            fragment.setArguments(arguments);
                            getFragmentManager().beginTransaction()
                                    .replace(R.id.movieposter_detail_container, fragment)
                                    .commit();
                        } else {

                            Intent i = new Intent(getActivity(), MoviePosterDetailActivity.class);
                            Movie currMovie = mMoviesList.get(position);
                            i.putExtra(INTENT_KEY_MOVIE_NAME, currMovie.getName());
                            i.putExtra(INTENT_KEY_MOVIE_RATING, currMovie.getUserrating());
                            i.putExtra(INTENT_KEY_MOVIE_RELEASE, currMovie.getReleasedate());
                            i.putExtra(INTENT_KEY_MOVIE_OVERVIEW, currMovie.getOverview());
                            i.putExtra(INTENT_KEY_POSTER_ART, currMovie.getPosterUrl());
                            i.putExtra(INTENT_KEY_MOVIE_ID, currMovie.getId());
                            startActivity(i);
                        }
                    }
                })
        );


        return posterlist;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mcontext =  view.getContext();
        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity=activity;

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("key", (ArrayList<? extends Parcelable>) mMoviesList);
        super.onSaveInstanceState(outState);

    }


    @Override
    public Loader<List<Movie>> onCreateLoader(int loaderId, Bundle args) {
        posterview.setVisibility(View.GONE);
        switch (loaderId) {
            case LOADER_ID_POPULAR:
                return new PosterSync(mActivity, URL_POPULAR);
            case LOADER_ID_HIGHEST:
                return new PosterSync(mActivity, URL_HIGHEST_RATED);
            case LOADER_ID_FAVOURITE:
                return new FavouriteSync(mActivity);
            default:
                return new PosterSync(mActivity, URL_POPULAR);
        }
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        mMoviesList = data;
        if (data==null){
            Toast.makeText(mActivity,"Sorry No Internet Connection",Toast.LENGTH_SHORT).show();
        }else {
            posterview.setVisibility(View.VISIBLE);
            mPosterAdapter.addItems(data);
            mPosterAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_popular:
                getLoaderManager().restartLoader(LOADER_ID_POPULAR, null, this).forceLoad();
                break;
            case R.id.action_highest:
                getLoaderManager().restartLoader(LOADER_ID_HIGHEST, null, this).forceLoad();
                break;
            case R.id.action_favourite:
                getLoaderManager().restartLoader(LOADER_ID_FAVOURITE,null, this).forceLoad();
            default:
                break;
        }
        return true;
    }



}
