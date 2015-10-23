package rccorp.movies.Activities;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import rccorp.movies.ASyncTasks.TrailerSync;
import rccorp.movies.Adapters.TrailerAdapter;
import rccorp.movies.Lists.Trailers;
import rccorp.movies.MasterDetailFlow.MoviePosterDetailFragment;
import rccorp.movies.R;
import rccorp.movies.RecyclerItemClickListener;


public class TrailerView extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Trailers>> {

    private TrailerAdapter mTrailerAdapter;
    private Context mcontext;
    RecyclerView trailerview;
    private List<Trailers> mtrailersList;
    protected RecyclerView.LayoutManager mLayoutManager;
    private static final int LOADER_ID = 2;
    public String mmovieid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailers);

        Context c=mcontext;

        mmovieid=getIntent().getStringExtra(MoviePosterDetailFragment.INTENT_GET_ID);

        trailerview=(RecyclerView)findViewById(R.id.trailerrecview);
        trailerview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mtrailersList=new ArrayList<>();
        mTrailerAdapter=new TrailerAdapter(mtrailersList,this);
        trailerview.setAdapter(mTrailerAdapter);

        trailerview.addOnItemTouchListener(
                new RecyclerItemClickListener(mcontext, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Trailers currTrailer=mtrailersList.get(position);
                        Intent trailerclick=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v="+ currTrailer.getTrailerkey()));
                        startActivity(trailerclick);
                    }
                }));

        getLoaderManager().initLoader(LOADER_ID, null, this).forceLoad();

    }



    @Override
    public Loader<List<Trailers>> onCreateLoader(int i, Bundle bundle) {
        return new TrailerSync(TrailerView.this, mmovieid);
    }

    @Override
    public void onLoadFinished(Loader<List<Trailers>> loader, List<Trailers> data) {
        mtrailersList=data;
        if(data==null) {
            mtrailersList = new ArrayList<>();
        }
        mTrailerAdapter.addItems(data);
        mTrailerAdapter.notifyDataSetChanged();

    }

    @Override
    public void onLoaderReset(Loader<List<Trailers>> loader) {

    }
}