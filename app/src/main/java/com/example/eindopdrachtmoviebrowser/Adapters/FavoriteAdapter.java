package com.example.eindopdrachtmoviebrowser.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eindopdrachtmoviebrowser.Activities.MediaDetails;
import com.example.eindopdrachtmoviebrowser.Movie;
import com.example.eindopdrachtmoviebrowser.API.OmdbAPIListener;
import com.example.eindopdrachtmoviebrowser.API.OmdbAPIManager;
import com.example.eindopdrachtmoviebrowser.R;
import com.squareup.picasso.Picasso;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder>  {
    Context context;
    private String[] values;




    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView titleView;
        private ImageView posterView;
        private RecyclerView recyclerView;
        private Context context;


        public ViewHolder(View view, Context context) {
            super(view);
            // Define click listener for the ViewHolder's View
            view.setOnClickListener(this);
            this.context = context;
            titleView = (TextView) view.findViewById(R.id.favo_text_title);
            posterView = (ImageView) view.findViewById(R.id.favo_image_poster);
            recyclerView = (RecyclerView) view.findViewById(R.id.favo_recycler_cast);

        }

        @Override
        public void onClick(View v) {

            startDetailActivity(context, titleView.getText().toString() ,titleView.getContentDescription().toString());
        }

        public TextView getTitleView() {
            return titleView;
        }

        public ImageView getPosterView() {
            return posterView;
        }

        public RecyclerView getRecyclerView() {
            return recyclerView;
        }

        public Context getContext() {
            return context;
        }
    }


    public FavoriteAdapter(Context context, String[] names) {
        this.context = context;
        this.values = names;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {


        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View posterView = inflater.inflate(R.layout.favorite_item, viewGroup, false);

        // Return a new holder instance
        FavoriteAdapter.ViewHolder viewHolder = new FavoriteAdapter.ViewHolder(posterView, context);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(FavoriteAdapter.ViewHolder viewHolder, final int position) {



        OmdbAPIManager omdbAPIManager = new OmdbAPIManager(context, new OmdbAPIListener() {
            @Override
            public void onMovieAvailable(Movie movie) {

                viewHolder.getTitleView().setText(movie.getTitle());

                Picasso.get().load(movie.getPoster()).into(viewHolder.getPosterView());

                LinearLayoutManager castLayoutManager = new LinearLayoutManager(viewHolder.getContext(), LinearLayoutManager.HORIZONTAL, false);
                RecyclerView castRecyclerView = (RecyclerView) viewHolder.getRecyclerView();
                castRecyclerView.setLayoutManager(castLayoutManager);
                castRecyclerView.setAdapter(new CastAdapter(viewHolder.getContext(), movie.getDirector(), movie.getWriter(false), movie.getActors()));

                DividerItemDecoration castDecoration = new DividerItemDecoration(castRecyclerView.getContext(), DividerItemDecoration.HORIZONTAL);
                castRecyclerView.addItemDecoration(castDecoration);

                viewHolder.getTitleView().setContentDescription(movie.getImdbID());
            }
            @Override
            public void onMovieError(Error error) {
                Log.d(this.getClass().getSimpleName(), "could not find movie");
            }
        });
        omdbAPIManager.getMovie(values[position], true);
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
                Log.d(this.getClass().getSimpleName(), "could not find movie, creating new empty detail view");
                intent.putExtra("MOVIE", new Movie(name));
                context.startActivity(intent);

            }
        });


        omdbAPIManager.getMovie(id, true);
    }

    public void dataSetChanged(String[] newData){
        values = newData;
    }
}
