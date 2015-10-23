package rccorp.movies.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rccorp.movies.R;
import rccorp.movies.Lists.Reviews;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.Viewholder> {

    private Context context;
    private List<Reviews> reviewsdata;


    public ReviewAdapter(List<Reviews> reviewsdata, Context context) {
        this.reviewsdata = reviewsdata;
        this.context = context;
    }

    @Override
    public ReviewAdapter.Viewholder onCreateViewHolder(ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.review, null);
        return new Viewholder(itemView);
    }


    @Override
    public void onBindViewHolder(ReviewAdapter.Viewholder holder, int i) {
        Reviews currReview = reviewsdata.get(i);

        holder.authorname.setText(currReview.getAuthor());
        holder.content.setText(currReview.getHisreview());


    }

    public static class Viewholder extends RecyclerView.ViewHolder {


        TextView authorname;
        TextView content;

        public Viewholder(View itemView) {
            super(itemView);
            authorname=(TextView)itemView.findViewById(R.id.authorname);
            content=(TextView)itemView.findViewById(R.id.actualreview);

        }
    }

    @Override
    public int getItemCount() {
        return reviewsdata.size();
    }

    public void addItems(List<Reviews> reviewsList) {
        if (reviewsList == null) {
            return;
        }
        reviewsdata = new ArrayList<>();
        reviewsdata.addAll(reviewsList);
    }

}