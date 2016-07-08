package rccorp.movies.Adapters;


import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import rccorp.movies.Lists.Movie;
import rccorp.movies.R;

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.Viewholder> {

    private Context context;
    private List<Movie> moviedata;


    public PosterAdapter(List<Movie> moviedata, Context context) {
        this.moviedata = moviedata;
        this.context = context;
    }

    @Override
    public PosterAdapter.Viewholder onCreateViewHolder(ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.moviesview, null);
        return new Viewholder(itemView);
    }



    @Override
    public void onBindViewHolder(PosterAdapter.Viewholder holder, int i) {
        Movie currentMovie = moviedata.get(i);
        Picasso.with(context).load("http://image.tmdb.org/t/p/w500/" + currentMovie.getPosterUrl()).into(holder.posterimg);
        holder.movietext.setText(currentMovie.getName());
        
    }

    public static class Viewholder extends RecyclerView.ViewHolder{

        CardView cv;
        ImageView posterimg;
        TextView movietext;


        public Viewholder(View itemView) {
            super(itemView);
            cv=(CardView)itemView.findViewById(R.id.moviecard);
            movietext=(TextView)itemView.findViewById(R.id.moviename);
            posterimg=(ImageView)itemView.findViewById(R.id.posterimgv);

        }
    }

    @Override
    public int getItemCount() {
        return moviedata.size();
    }

    public void addItems(List<Movie> moviesList) {
        moviedata = new ArrayList<>();
        moviedata.addAll(moviesList);
    }

}
