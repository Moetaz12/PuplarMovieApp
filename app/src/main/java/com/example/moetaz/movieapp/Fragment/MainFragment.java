package com.example.moetaz.movieapp.fragment;


import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.moetaz.movieapp.BuildConfig;
import com.example.moetaz.movieapp.R;
import com.example.moetaz.movieapp.activity.MainActivity;
import com.example.moetaz.movieapp.adapter.CustomAdapter;
import com.example.moetaz.movieapp.datastorage.DBAdadpter;
import com.example.moetaz.movieapp.models.MovieModel;
import com.example.moetaz.movieapp.parsing.Parse;
import com.example.moetaz.movieapp.utilities.MyUtilities;
import com.example.moetaz.movieapp.utilities.Mysingleton;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;

import static com.example.moetaz.movieapp.interfaces.MoviesProivderConstants.BACK_BATH;
import static com.example.moetaz.movieapp.interfaces.MoviesProivderConstants.CONTENT_URI_1;
import static com.example.moetaz.movieapp.interfaces.MoviesProivderConstants.MOVIE_ID;
import static com.example.moetaz.movieapp.interfaces.MoviesProivderConstants.ORIGINAL_TITLE;
import static com.example.moetaz.movieapp.interfaces.MoviesProivderConstants.OVERVIEW;
import static com.example.moetaz.movieapp.interfaces.MoviesProivderConstants.POSTER_PATH;
import static com.example.moetaz.movieapp.interfaces.MoviesProivderConstants.RELEASE_DATE;
import static com.example.moetaz.movieapp.interfaces.MoviesProivderConstants.TITLE;
import static com.example.moetaz.movieapp.interfaces.MoviesProivderConstants.VOTE;
import static com.example.moetaz.movieapp.utilities.MyUtilities.isNetworkConnected;


public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LIST_KEY = "list";
    private GridLayoutManager gridLayoutManager;
    public boolean FirstTimeLoad = false;
    private RecyclerView recyclerView;
    private ArrayList<MovieModel> movies = new ArrayList<>();
    public static String currentURL = "/movie/popular";
    private CustomAdapter customAdapter;
    private ProgressDialog progressDialog;
    private DBAdadpter dbAdadpter;
    public static int index = -1;
    public static int top = -1;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putSerializable("list",movies);
        outState.putParcelableArrayList(LIST_KEY, movies);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading ...");
        dbAdadpter = DBAdadpter.getDBAdadpterInstance(getActivity());
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
            ArrayList<MovieModel> m = savedInstanceState.getParcelableArrayList(LIST_KEY);
            setGridManager();
            recyclerView.setLayoutManager(gridLayoutManager);
            movies.clear();
            movies.addAll(0, m);
            customAdapter = new CustomAdapter(getActivity(), movies);
            recyclerView.setAdapter(customAdapter);
        } else {
            LoadMain(currentURL);
        }
        return rootView;
    }

    public void LoadMain(String url) {
        progressDialog.show();
        setGridManager();
        recyclerView.setLayoutManager(gridLayoutManager);

        if (isNetworkConnected(getActivity())) {
            customAdapter = new CustomAdapter(getActivity(), movies);
            recyclerView.setAdapter(customAdapter);
            LoadByVolley(url);

        } else {
            progressDialog.dismiss();
            customAdapter = new CustomAdapter(getActivity(), dbAdadpter.GetData());
            recyclerView.setAdapter(customAdapter);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        index = gridLayoutManager.findFirstVisibleItemPosition();
        View v = recyclerView.getChildAt(0);
        top = (v == null) ? 0 : (v.getTop() - recyclerView.getPaddingTop());

    }

    @Override
    public void onResume() {
        super.onResume();
        if (index != -1)
            gridLayoutManager.scrollToPositionWithOffset(index, top);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    private void DeleteData() {
        if (movies != null) {
            movies.clear();
            customAdapter.notifyDataSetChanged();
        }
    }

    private void LoadByVolley(String url) {
        String baseUrl = "http://api.themoviedb.org/3" + url + "?";
        String apiKey = "api_key=" + BuildConfig.MOVIE_APP_API_KEY;
        String lasturl = baseUrl.concat(apiKey);

        Cache cache = Mysingleton.getInstance(getActivity()).getRequestQueue().getCache();
        Cache.Entry entry = cache.get(lasturl);
        if (entry != null) {

            try {
                String data = new String(entry.data, "UTF-8");
                DeleteData();
                Iterator iterator = Parse.parseStringToJson(data).iterator();
                while (iterator.hasNext()) {
                    MovieModel movie = (MovieModel) iterator.next();
                    movies.add(movie);
                    customAdapter.notifyItemInserted(movies.size() - 1);
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {

            dbAdadpter.DeleteAllDate();


            StringRequest stringRequest = new StringRequest(Request.Method.GET, lasturl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    DeleteData();
                    Iterator iterator = Parse.parseStringToJson(response).iterator();
                    while (iterator.hasNext()) {
                        MovieModel movie = (MovieModel) iterator.next();

                        dbAdadpter.Insert(movie.getOriginal_title(), movie.getTitle(), movie.getPoster_path()
                                , movie.getOverview(), movie.getRelease_date(), movie.getBackdrop_path(),
                                String.valueOf(movie.getVote_average()), movie.getId());

                        movies.add(movie);
                        customAdapter.notifyItemInserted(movies.size() - 1);

                    }

                    progressDialog.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    MyUtilities.message(getActivity(), getString(R.string.error_msg));
                }
            });
            Mysingleton.getInstance(getActivity()).addToRequest(stringRequest);
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

        } else if (id == R.id.favourite) {
            if (!FirstTimeLoad) {
                FirstTimeLoad = true;
                getLoaderManager().initLoader(1, null, this);
            } else {
                getLoaderManager().restartLoader(1, null, this);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == 1) {
            return new CursorLoader(getContext(), CONTENT_URI_1, null, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        ArrayList<MovieModel> movieModels = new ArrayList<>();

        if (cursor != null && cursor.getCount() > 0) {

            while (cursor.moveToNext()) {
                MovieModel movieModel = new MovieModel();
                movieModel.setOriginal_title(cursor.getString(cursor.getColumnIndex(ORIGINAL_TITLE)));
                movieModel.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
                movieModel.setPoster_path(cursor.getString(cursor.getColumnIndex(POSTER_PATH)));
                movieModel.setOverview(cursor.getString(cursor.getColumnIndex(OVERVIEW)));
                movieModel.setRelease_date(cursor.getString(cursor.getColumnIndex(RELEASE_DATE)));
                movieModel.setBackdrop_path(cursor.getString(cursor.getColumnIndex(BACK_BATH)));
                movieModel.setVote_average(cursor.getString(cursor.getColumnIndex(VOTE)));
                movieModel.setId(cursor.getString(cursor.getColumnIndex(MOVIE_ID)));
                movieModels.add(movieModel);

            }
        }
        assert cursor != null;
        cursor.close();
        DeleteData();
        movies.addAll(0, movieModels);
        customAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void setGridManager() {
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);

        if (MainActivity.IsTowPane)
            gridLayoutManager = new GridLayoutManager(getActivity(), 3);

        recyclerView.setLayoutManager(gridLayoutManager);
    }

}
