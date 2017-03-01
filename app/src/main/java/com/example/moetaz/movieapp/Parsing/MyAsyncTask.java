package com.example.moetaz.movieapp.Parsing;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import com.example.moetaz.movieapp.Adapter.CustomAdapter;
import com.example.moetaz.movieapp.BuildConfig;
import com.example.moetaz.movieapp.Models.MovieModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MyAsyncTask extends AsyncTask<Void, Void, String> {


    public static ArrayList<MovieModel> modelList;
    public static RecyclerView recyclerView;
    public static CustomAdapter customAdapter;
    Context context;
    String url;

    public MyAsyncTask(Context context, RecyclerView recyclerView,String url) {
        this.recyclerView=recyclerView;
        this.context = context;
        this.url=url;
    }


    @Override
    protected String doInBackground(Void... voids) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonStr = null;

        try {

            String baseUrl = "http://api.themoviedb.org/3"+url+"?";
            String apiKey = "api_key=" + BuildConfig.MOVIE_APP_API_KEY;
            URL url = new URL(baseUrl.concat(apiKey));
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {

                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {

                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            jsonStr = buffer.toString();
            Log.d("JSON", jsonStr);
        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);

            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }
        return jsonStr;
    }

    @Override
    protected void onPostExecute(String mstring) {

        try {
            JSONObject object = new JSONObject(mstring);
            JSONArray jsonArray = object.getJSONArray("results");

         //---------------------------------------------------------//
           modelList=new ArrayList<>();

            for(int i=0;i<jsonArray.length();i++){
                JSONObject finalobject=jsonArray.getJSONObject(i);
                MovieModel model=new MovieModel();

                model.setOriginal_title(finalobject.getString("original_title"));
                model.setTitle (finalobject.getString("title"));
                model.setPoster_path(finalobject.getString("poster_path"));
                model.setOverview(finalobject.getString("overview"));
                model.setRelease_date(finalobject.getString("release_date"));
                model.setBackdrop_path(finalobject.getString("backdrop_path"));
                model.setVote_average(finalobject.getDouble("vote_average"));
                model.setId(finalobject.getString("id"));

                modelList.add(model);
            }
          //-------------------------------------------------------//


        } catch (JSONException e) {
            e.printStackTrace();
        }

        customAdapter=new CustomAdapter(context,modelList);
        recyclerView.setAdapter(customAdapter);
    }


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}

