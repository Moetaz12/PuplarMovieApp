package com.example.moetaz.movieapp.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.moetaz.movieapp.Activity.MainActivity;
import com.example.moetaz.movieapp.Models.MovieModel;
import com.example.moetaz.movieapp.Parsing.MyAsyncTask;
import com.example.moetaz.movieapp.R;
import com.example.moetaz.movieapp.DataStorage.FavouriteData;

import java.util.ArrayList;
import java.util.List;


public class MainFragment extends Fragment {
    RecyclerView recyclerView;
    public static String currentURL = "/movie/popular";

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);

            LoadMain(currentURL);

        setHasOptionsMenu(true);
        return rootView;
    }

    public void LoadMain(String url) {
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(), 2);

         if(MainActivity.IsTowPane)
         gridLayoutManager = new GridLayoutManager(getActivity(), 3);

        recyclerView.setLayoutManager(gridLayoutManager);

        MyAsyncTask task = new MyAsyncTask(getActivity(), recyclerView, url);
                 task.execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    private void LoadFavourite()
    {
        List<MovieModel> FavData = new ArrayList<>();

        for (String id : new FavouriteData(getActivity()).FindAllData()) {
            for (int j = 0; j < MyAsyncTask.modelList.size(); j++) {
                if (MyAsyncTask.modelList.get(j).getId().equalsIgnoreCase(id))
                    FavData.add(MyAsyncTask.modelList.get(j));
            }

        }

        DeleteData();
        MyAsyncTask.modelList.addAll(0, FavData);
        MyAsyncTask.customAdapter.notifyDataSetChanged();

    }


    private void DeleteData()
    {
        if (MyAsyncTask.modelList != null){
            MyAsyncTask.modelList.clear();
            MyAsyncTask.customAdapter.notifyDataSetChanged();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.topratedmovie) {

            currentURL = "/movie/top_rated";
            LoadMain(currentURL);
            return true;
        } else if (id == R.id.Popularmovie) {

            currentURL = "/movie/popular";
            LoadMain(currentURL);
        } else if (id == R.id.refresh) {
                LoadMain(currentURL);

        }
        else if(id==R.id.favourite){
          LoadFavourite();
        }

        return super.onOptionsItemSelected(item);
    }
}
