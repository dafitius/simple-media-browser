package com.example.eindopdrachtmoviebrowser.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eindopdrachtmoviebrowser.R;

import java.util.ArrayList;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.ViewHolder> {


    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> titles = new ArrayList<>();

    public CastAdapter(Context context, String[] directors, String[] writers, String[] actors) {
        for (String director : directors) {
            names.add(director);
            titles.add(context.getString(R.string.director));
        }

        for (String writer : writers) {
            names.add(writer);
            titles.add(context.getString(R.string.writer));
        }

        for (String actor : actors) {
            names.add(actor);
            titles.add(context.getString(R.string.actor));
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {


        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Inflate the custom layout
        View castView = inflater.inflate(R.layout.cast_item, parent, false);

        // Return a new holder instance
        CastAdapter.ViewHolder viewHolder = new CastAdapter.ViewHolder(castView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CastAdapter.ViewHolder holder, int position) {

        TextView title = holder.getTitleView();
        TextView name = holder.getNameView();


        title.setText(titles.get(position));
        name.setText(" " + names.get(position) + " ");
    }

    @Override
    public int getItemCount() {
        return names.size() - 1;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView titleView;
        private TextView nameView;
        private Context context;


        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View


            view.setOnClickListener(this);
            this.context = context;
            titleView = (TextView) view.findViewById(R.id.castItem_text_title);
            nameView = (TextView) view.findViewById(R.id.castItem_text_name);
        }

        public TextView getTitleView() {
            return titleView;
        }

        public TextView getNameView() {
            return nameView;
        }

        @Override
        public void onClick(View v) {
            if(!nameView.getText().toString().equalsIgnoreCase(" N/A ")) {
                openWebURL("https://www.google.nl/search?q=" + nameView.getText().toString().replaceAll(" ", "+"), this.getNameView().getContext());
            }

        }

        public void openWebURL( String inURL, Context context) {
            Intent browse = new Intent( Intent.ACTION_VIEW , Uri.parse( inURL ) );

            context.startActivity(browse);
        }
    }



}
