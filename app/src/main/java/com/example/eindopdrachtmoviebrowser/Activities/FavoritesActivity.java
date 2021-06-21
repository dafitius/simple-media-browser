package com.example.eindopdrachtmoviebrowser.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.eindopdrachtmoviebrowser.Adapters.FavoriteAdapter;
import com.example.eindopdrachtmoviebrowser.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.HashSet;
import java.util.Set;

import static com.example.eindopdrachtmoviebrowser.Activities.MediaDetails.MyPREFERENCES;

public class FavoritesActivity extends AppCompatActivity {

    Set<String> favorites;
    FavoriteAdapter adapter;
    FloatingActionButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        backButton = findViewById(R.id.favorites_button_back);
        backButton.setOnClickListener(v->finish());

        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Set<String> set = sharedpreferences.getStringSet("favo", null);
        if(set != null){
            favorites = set;
        } else {
            Log.i(this.getClass().getSimpleName(), "New save was made to store favorite media.");
            favorites = new HashSet<>();
        }


        LinearLayoutManager LayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView RecyclerView = (RecyclerView) findViewById(R.id.favorites_recycler_favos);
        RecyclerView.setLayoutManager(LayoutManager);
        adapter = new FavoriteAdapter(this, setToArray(favorites));
        RecyclerView.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter != null){
            adapter.dataSetChanged(setToArray(favorites));
            adapter.notifyDataSetChanged();
        }
    }

    private String[] setToArray(Set<String> set){

        String[] array = set.toArray(new String[set.size()]);
        return array;
    }

}