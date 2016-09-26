package com.dasyel.dasyelwillems_pset3;

import android.content.Context;

public abstract class MovieAPI {
    public abstract void getMoviesByName(String query, NameSearch ns, Context context, int RequestId);
    public abstract void getMovieByID(String query, IdSearch is, Context context, int RequestId);
}
