package rccorp.movies.Activities;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import rccorp.movies.ASyncTasks.ReviewSync;
import rccorp.movies.Adapters.ReviewAdapter;
import rccorp.movies.Lists.Reviews;
import rccorp.movies.MasterDetailFlow.MoviePosterDetailFragment;
import rccorp.movies.R;


public class ReviewsView extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Reviews>> {

    private ReviewAdapter mReviewAdapter;
    RecyclerView reviewview;
    private List<Reviews> mReviewList;
    protected RecyclerView.LayoutManager mLayoutManager;
    private static final int LOADER_ID = 4;
    public String rmovieid;

    private ProgressBar mprogressbar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews_view);

        mprogressbar = (ProgressBar)findViewById(R.id.loader_review);
        mprogressbar.setVisibility(View.VISIBLE);

        rmovieid=getIntent().getStringExtra(MoviePosterDetailFragment.INTENT_GET_ID);

        reviewview=(RecyclerView)findViewById(R.id.reviewsrecview);
        reviewview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        mReviewList=new ArrayList<>();
        mReviewAdapter=new ReviewAdapter(mReviewList,this);
        reviewview.setAdapter(mReviewAdapter);

        getLoaderManager().initLoader(LOADER_ID,null,this).forceLoad();
    }



    @Override
    public Loader<List<Reviews>> onCreateLoader(int i, Bundle bundle) {
        mprogressbar.setVisibility(View.VISIBLE);
        reviewview.setVisibility(View.GONE);
        return new ReviewSync(ReviewsView.this,rmovieid);
    }

    @Override
    public void onLoadFinished(Loader<List<Reviews>> loader, List<Reviews> data) {
        mReviewList=data;
        mprogressbar.setVisibility(View.GONE);
        reviewview.setVisibility(View.VISIBLE);
        if (data==null){
            mReviewList=new ArrayList<>();
        }
        mReviewAdapter.addItems(data);
        mReviewAdapter.notifyDataSetChanged();

    }

    @Override
    public void onLoaderReset(Loader<List<Reviews>> loader) {

    }
}
