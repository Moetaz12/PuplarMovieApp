package com.example.moetaz.movieapp.DataStorage;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class FavouriteData {
    private Context context;
    private SharedPreferences sharedPreferences;

    public FavouriteData(Context context) {
        this.context = context;

        sharedPreferences = this.context.getSharedPreferences(
                "favoritePref", Context.MODE_APPEND
        );
    }

    public List<String> FindAllData(){

        Map<String , ?> iDsMap = this.sharedPreferences.getAll();

        List<String> ItemId = new ArrayList<>();
        for (String key: iDsMap.keySet()
             ) {

            ItemId.add(this.sharedPreferences.getString(key, ""));
        }

        return ItemId;
    }

    public void SaveItem(String Item){
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString(Item, Item);
        editor.apply();
    }

    public void RemoveItem(String Item){
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.remove(Item);
        editor.apply();
    }

    public String GetItem(String item){
        return this.sharedPreferences.getString(item, "");
    }
}
