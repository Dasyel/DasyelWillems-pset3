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
import android.widget.Toast;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity
        implements SearchView.OnQueryTextListener, NameSearch{
    static final int BASE_REQUEST_ID = 2000;
    private ArrayList<Movie> movieList;
    private MovieListAdapter adapter;
    private ListView listView;
    private String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();
        query = intent.getStringExtra(getString(R.string.QUERY_KEY));
        movieList = new ArrayList<>();

        adapter = new MovieListAdapter(this, this, movieList);
        listView = (ListView) findViewById(R.id.movieListView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie m = (Movie) parent.getItemAtPosition(position);
                Intent movieIntent = new Intent(SearchActivity.this, MovieActivity.class);
                movieIntent.putExtra(getString(R.string.QUERY_KEY), m.imdbID);
                movieIntent.putExtra(getString(R.string.MAIN_BOOL_KEY), false);
                startActivity(movieIntent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Movie m = (Movie) parent.getItemAtPosition(position);
                if(SpWrapper.get_IDs(SearchActivity.this).contains(m.imdbID)){
                    SpWrapper.remove_ID(SearchActivity.this, m.imdbID);
                } else {
                    SpWrapper.add_ID(SearchActivity.this, m.imdbID);
                }
                listView.invalidateViews();
                return true;
            }
        });

        new OMDB_API().getMoviesByName(query, this, this, BASE_REQUEST_ID);
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
        this.movieList.clear();
        this.adapter.notifyDataSetChanged();
        this.query = query;
        new OMDB_API().getMoviesByName(query, this, this, BASE_REQUEST_ID);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void receiveMovieListObject(ArrayList<Movie> movieList, int requestId){
        if(movieList.size() == 0 && requestId == BASE_REQUEST_ID){
            Toast t = Toast.makeText(this, R.string.no_results_msg, Toast.LENGTH_SHORT);
            t.show();
            return;
        }
        if(requestId == BASE_REQUEST_ID) {
            this.movieList.clear();
        }
        if(movieList.size() != 0) {
            this.movieList.addAll(movieList);
            new OMDB_API().getMoviesByName(query, this, this, requestId + 1);
            this.adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
