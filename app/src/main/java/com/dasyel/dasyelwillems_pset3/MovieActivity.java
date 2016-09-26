package com.dasyel.dasyelwillems_pset3;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.HashSet;

public class MovieActivity extends AppCompatActivity implements
        SearchView.OnQueryTextListener, IdSearch{
    private Movie movie;
    private boolean onList = false;
    static final int BASE_REQUEST_ID = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        Intent intent = getIntent();
        String query = intent.getStringExtra(getString(R.string.QUERY_KEY));
        new OMDB_API().getMovieByID(query, this, this, BASE_REQUEST_ID);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra(getString(R.string.QUERY_KEY), query);
        startActivity(intent);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public void setTextViews() {
        TextView title_tv = (TextView) findViewById(R.id.title_tv);
        TextView actors_tv = (TextView) findViewById(R.id.actor_tv);
        TextView description_tv = (TextView) findViewById(R.id.description_tv);

        title_tv.setText(movie.title);
        actors_tv.setText(movie.actors);
        description_tv.setText(movie.plot);
    }

    public void setPicture() {
        final NetworkImageView iv = (NetworkImageView) findViewById(R.id.poster_iv);
        String url = movie.poster;
        ImageLoader il = MySingleton.getInstance(this).getImageLoader();
        iv.setImageUrl(url, il);
    }

    public void watchListButton(View view){
        Button button = (Button) view;
        if(onList){
            // Remove from list
            button.setText(R.string.to_list_button);
            button.setBackgroundColor(Color.GRAY);
            SpWrapper.remove_ID(MovieActivity.this, movie.imdbID);
            onList = false;
        } else {
            // Place on list
            button.setText(R.string.on_list_button);
            button.setBackgroundColor(Color.GREEN);
            SpWrapper.add_ID(MovieActivity.this, movie.imdbID);
            onList = true;
        }
    }

    @Override
    public void receiveMovieObject(Movie movie, int RequestId) {
        this.movie = movie;
        setTextViews();
        setPicture();
        HashSet<String> hs = SpWrapper.get_IDs(this);
        onList = hs.contains(movie.imdbID);
        Button button = (Button) findViewById(R.id.to_list_button);
        if(onList) {
            button.setText(R.string.on_list_button);
            button.setBackgroundColor(Color.GREEN);
        }
    }

    @Override
    public void onBackPressed() {
        Intent thisIntent = getIntent();
        boolean mainActivity = thisIntent.getBooleanExtra(getString(R.string.MAIN_BOOL_KEY), false);
        Intent intent;
        if(mainActivity){
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            return;
        }
        super.onBackPressed();
        this.finish();
    }
}
