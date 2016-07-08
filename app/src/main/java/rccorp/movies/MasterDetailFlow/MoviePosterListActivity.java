package rccorp.movies.MasterDetailFlow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import rccorp.movies.R;


public class MoviePosterListActivity extends AppCompatActivity implements MoviePosterListFragment.Callbacks
        {

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movieposter_list);


        if (findViewById(R.id.movieposter_detail_container) != null) {
            mTwoPane = true;
        }

    }

    @Override
    public void onItemSelected(int position) {
        }
    }
