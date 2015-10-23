package rccorp.movies.Adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rccorp.movies.R;
import rccorp.movies.Lists.Trailers;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.Viewholder> {

    private Context context;
    private List<Trailers> trailersdata;


    public TrailerAdapter(List<Trailers> trailersdata, Context context) {
        this.trailersdata = trailersdata;
        this.context = context;
    }

    @Override
    public TrailerAdapter.Viewholder onCreateViewHolder(ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer, null);
        return new Viewholder(itemView);
    }



    @Override
    public void onBindViewHolder(TrailerAdapter.Viewholder holder, int i) {
        Trailers curTrailer= trailersdata.get(i);
        holder.trailname.setText(curTrailer.getTrailername());

    }

    public static class Viewholder extends RecyclerView.ViewHolder{


        ImageButton playb;
        TextView trailname;

        public Viewholder(View itemView) {
            super(itemView);
            playb=(ImageButton)itemView.findViewById(R.id.trailerplayb);
            trailname=(TextView)itemView.findViewById(R.id.trailername);

        }
    }

    @Override
    public int getItemCount() {
        return trailersdata.size();
    }

    public void addItems(List<Trailers> trailerslist) {
        if(trailerslist==null) {
            return;
        }
        trailersdata = new ArrayList<>();
        trailersdata.addAll(trailerslist);
    }

}