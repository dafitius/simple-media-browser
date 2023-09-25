package com.dafitius.simplemoviebrowser.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dafitius.simplemoviebrowser.Activities.MediaDetails;
import com.dafitius.simplemoviebrowser.Models.Movie;
import com.dafitius.simplemoviebrowser.API.OmdbAPIListener;
import com.dafitius.simplemoviebrowser.API.OmdbAPIManager;
import com.dafitius.simplemoviebrowser.R;
import com.squareup.picasso.Picasso;


public class MoviePosterAdapter extends RecyclerView.Adapter<MoviePosterAdapter.ViewHolder> {

    Context context;
    private final String[] values;
    private final String[] images;
    private final String[] ids;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView titleView;
        private final ImageView posterView;
        private final Context context;


        public ViewHolder(View view, Context context) {
            super(view);
            // Define click listener for the ViewHolder's View

            view.setOnClickListener(this);
            this.context = context;
            titleView = (TextView) view.findViewById(R.id.posterName);
            posterView = (ImageView) view.findViewById(R.id.poster);
        }

        public TextView getTitleView() {
            return titleView;
        }

        public ImageView getPosterView() {
            return posterView;
        }

        @Override
        public void onClick(View v) {
            startDetailActivity(context, titleView.getText().toString(), titleView.getContentDescription().toString());
        }
    }


    public MoviePosterAdapter(Context context, String[] values, String[] images, String[] ids) {
        this.context = context;
        this.values = values;
        this.images = images;
        this.ids = ids;
    }


    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {


        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View posterView = inflater.inflate(R.layout.movie_poster, viewGroup, false);

        // Return a new holder instance
        return new ViewHolder(posterView, context);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

       // layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


            Picasso.get().load(images[position]).into(viewHolder.getPosterView());
            viewHolder.getTitleView().setText(values[position]);
            viewHolder.getTitleView().setContentDescription(ids[position]);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return values.length;

    }


    public static void startDetailActivity(Context context, String name, String id){
        Intent intent = new Intent(context, MediaDetails.class);
        OmdbAPIManager omdbAPIManager = new OmdbAPIManager(context, new OmdbAPIListener() {
            @Override
            public void onMovieAvailable(Movie movie) {

                intent.putExtra("MOVIE", movie);
                context.startActivity(intent);

            }

            @Override
            public void onMovieError(Error error) {

                intent.putExtra("MOVIE", new Movie(name));
                context.startActivity(intent);

            }
        });


        omdbAPIManager.getMovie(id, true);
    }
}

