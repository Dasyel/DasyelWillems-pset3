package com.dasyel.dasyelwillems_pset3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, IdSearch {
    static final int BASE_REQUEST_ID = 1000;

    private ArrayList<Movie> movieList;
    private MovieListAdapter adapter;
    private ListView listView;
    private String[] idList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieList = new ArrayList<>();
        adapter = new MovieListAdapter(this, this, movieList);
        listView = (ListView) findViewById(R.id.movieListView_Watch);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie m = (Movie) parent.getItemAtPosition(position);
                Intent movieIntent = new Intent(MainActivity.this, MovieActivity.class);
                movieIntent.putExtra(getString(R.string.QUERY_KEY), m.imdbID);
                movieIntent.putExtra(getString(R.string.MAIN_BOOL_KEY), true);
                startActivity(movieIntent);
                MainActivity.this.finish();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Movie m = (Movie) parent.getItemAtPosition(position);
                SpWrapper.remove_ID(MainActivity.this, m.imdbID);
                movieList.remove(m);
                adapter.notifyDataSetChanged();
                listView.invalidateViews();
                return true;
            }
        });

        HashSet<String> hs = SpWrapper.get_IDs(this);
        idList = hs.toArray(new String[hs.size()]);
        if(idList.length > 0) {
            new OMDB_API().getMovieByID(idList[0], this, this, BASE_REQUEST_ID);
        }
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

    @Override
    public void receiveMovieObject(Movie movie, int requestId) {
        int index = requestId - BASE_REQUEST_ID + 1;
        // Clear the movielist if this is a new request
        if(index == 1){
            movieList.clear();
        }
        movieList.add(movie);
        // If more movies need to be loaded: load the next movie in the list
        // If all are loaded, reload the listView
        if(index < idList.length){
            new OMDB_API().getMovieByID(idList[index], this, this, requestId+1);
        } else {
            Collections.sort(movieList);
            adapter.notifyDataSetChanged();
        }
    }
}
