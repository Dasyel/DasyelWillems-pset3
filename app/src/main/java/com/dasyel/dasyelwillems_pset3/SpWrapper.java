package com.dasyel.dasyelwillems_pset3;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;

// A wrapper class around the sharedPreferences for this app. This cleans up the activities' code.
public class SpWrapper {
    public static HashSet<String> get_IDs(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.SP_KEY), Context.MODE_PRIVATE);
        return new HashSet<>(sharedPref.
                getStringSet(context.getString(R.string.ID_LIST_KEY), new HashSet<String>()));
    }

    public static void add_ID(Context context, String ID){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.SP_KEY), Context.MODE_PRIVATE);
        HashSet<String> hs = new HashSet<>(sharedPref.
                getStringSet(context.getString(R.string.ID_LIST_KEY), new HashSet<String>()));
        hs.add(ID);
        sharedPref.edit().putStringSet(context.getString(R.string.ID_LIST_KEY), hs).apply();
    }

    public static void remove_ID(Context context, String ID){
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.SP_KEY), Context.MODE_PRIVATE);
        HashSet<String> hs = new HashSet<>(sharedPref.
                getStringSet(context.getString(R.string.ID_LIST_KEY), new HashSet<String>()));
        hs.remove(ID);
        sharedPref.edit().putStringSet(context.getString(R.string.ID_LIST_KEY), hs).apply();
    }
}
