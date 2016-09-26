package com.dasyel.dasyelwillems_pset3;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

// This class is a wrapper around the Volley requests, defined for the OMDB API
// It extends from an abstract class MovieAPI in case another API needs to be used.
public class OMDB_API extends MovieAPI {
    private static final String URL = "http://www.omdbapi.com/?<T>=<QUERY>&y=&plot=full&r=json";
    private static final String PAGE = "&page=";
    private static final String QUERY_KEYWORD = "<QUERY>";
    private static final String TYPE_KEYWORD = "<T>";
    private static final String ID_KEYWORD = "i";
    private static final String SEARCH_KEYWORD = "s";
    private static final String ERROR_MSG = "Something went wrong, please try again";

    @Override
    public void getMoviesByName(String query, final NameSearch ns, final Context context,
                                final int requestId) {
        MySingleton.getInstance(context).getRequestQueue().cancelAll(context);
        String url;
        if(requestId > SearchActivity.BASE_REQUEST_ID){
            url = queryFixer(query, SEARCH_KEYWORD, requestId - SearchActivity.BASE_REQUEST_ID);
        } else {
            url = queryFixer(query, SEARCH_KEYWORD);
        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        ArrayList<Movie> movieList = new ArrayList<>();
                        try {
                            if(!response.has("Search")) {
                                ns.receiveMovieListObject(movieList, requestId);
                                return;
                            }
                            JSONArray ja = response.getJSONArray("Search");
                            for(int i=0; i<ja.length(); i++){
                                JSONObject elem = ja.getJSONObject(i);
                                Movie m = new Movie(
                                        elem.getString("Title"),
                                        elem.getString("Year"),
                                        elem.getString("imdbID"),
                                        elem.getString("Type"),
                                        elem.getString("Poster")
                                );
                                if(m.type.equals("movie") || m.type.equals("series")) {
                                    movieList.add(m);
                                }
                            }
                        } catch (JSONException e){
                            Toast t = Toast.makeText(context, ERROR_MSG, Toast.LENGTH_SHORT);
                            t.show();
                            return;
                        }
                        ns.receiveMovieListObject(movieList, requestId);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast t = Toast.makeText(context, ERROR_MSG, Toast.LENGTH_SHORT);
                        t.show();
                    }
                });

        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    @Override
    public void getMovieByID(String query, final IdSearch is, final Context context,
                             final int requestId) {
        MySingleton.getInstance(context).getRequestQueue().cancelAll(context);
        String url = queryFixer(query, ID_KEYWORD);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Movie m = null;
                        try {
                            m = new Movie(
                                    response.getString("Title"),
                                    response.getString("Year"),
                                    response.getString("imdbID"),
                                    response.getString("Type"),
                                    response.getString("Poster"),
                                    response.getString("Actors"),
                                    response.getString("Plot")
                            );
                        } catch (JSONException e){
                            Toast t = Toast.makeText(context, ERROR_MSG, Toast.LENGTH_SHORT);
                            t.show();
                            return;
                        }
                        is.receiveMovieObject(m, requestId);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast t = Toast.makeText(context, ERROR_MSG, Toast.LENGTH_SHORT);
                        t.show();
                    }
                });

        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    private static String queryFixer(String query, String type, int page){
        StringBuilder sb = new StringBuilder(URL);
        String new_query = "";
        String[] queryArray = query.trim().split(" ");
        if(queryArray.length > 1) {
            for (String word : query.split(" ")) new_query += word + "+";
            new_query = new_query.substring(0, new_query.length()-1);
        } else {
            new_query = query.trim();
        }
        int start = sb.indexOf(QUERY_KEYWORD);
        int end = start + QUERY_KEYWORD.length();
        sb.replace(start, end, new_query);
        start = sb.indexOf(TYPE_KEYWORD);
        end = start + TYPE_KEYWORD.length();
        sb.replace(start, end, type);
        if(page > 0){
            sb.append(PAGE).append(page+1);
        }
        return sb.toString();
    }

    private static String queryFixer(String query, String type){
        return queryFixer(query, type, 0);
    }
}
