package com.dasyel.dasyelwillems_pset3;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;


public class MovieListAdapter extends BaseAdapter {
    private List<Movie> movieItems;
    private Activity activity;
    private Context context;
    private LayoutInflater inflater;
    private ImageLoader imageLoader;

    public MovieListAdapter(Activity activity, Context context, List<Movie> movieItems){
        this.activity = activity;
        this.context = context;
        this.movieItems = movieItems;
        this.imageLoader = MySingleton.getInstance(context).getImageLoader();
    }

    @Override
    public int getCount() {
        return movieItems.size();
    }

    @Override
    public Movie getItem(int position) {
        return movieItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);

        if (imageLoader == null)
            imageLoader = MySingleton.getInstance(context).getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView year = (TextView) convertView.findViewById(R.id.year);
        CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox);

        Movie m = movieItems.get(position);
        thumbNail.setDefaultImageResId(R.drawable.def_img);
        thumbNail.setImageUrl(m.poster, imageLoader);
        thumbNail.setAdjustViewBounds(true);
        title.setText(m.title);
        year.setText(m.year);

        if(SpWrapper.get_IDs(this.context).contains(m.imdbID)) {
            cb.setChecked(true);
        } else {
            cb.setChecked(false);
        }
        return convertView;
    }
}
