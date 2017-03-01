package com.example.moetaz.movieapp.Fragment;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.moetaz.movieapp.BuildConfig;
import com.example.moetaz.movieapp.Models.MovieModel;
import com.example.moetaz.movieapp.Models.ReviewModel;
import com.example.moetaz.movieapp.Models.TrailerModel;
import com.example.moetaz.movieapp.R;
import com.example.moetaz.movieapp.DataStorage.FavouriteData;
import com.squareup.picasso.Picasso;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    ImageView imageView;
    String imgurl;
    String id;

    MovieModel model;

    CardView cardView1;
    CardView cardView2;
    CardView cardView3;

    TextView textViewtitle;
    TextView textViewOverviwe;
    TextView textViewreleaseDate;
    TextView textViewVoteAverage;

    TextView textViewReview1;
    TextView textViewReview2;
    TextView textViewReview3;

    TextView textViewAuther1;
    TextView textViewAuther2;
    TextView textViewAuther3;

    ImageView imageViewauther1;
    ImageView imageViewauther2;
    ImageView imageViewauther3;


    TextView textViewurl1;
    TextView textViewurl2;
    TextView textViewurl3;

    ImageButton imageButton;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootview=inflater.inflate(R.layout.fragment_detail, container, false);

        Intent intent = getActivity().getIntent();

        model= (MovieModel) intent.getSerializableExtra("modelPass");
        if(model == null)
            model = (MovieModel) getArguments().getSerializable("modelPass");
        imgurl=model.getBackdrop_path();
        id=model.getId();

        imageView= (ImageView)rootview.findViewById(R.id.image_detail);
        textViewtitle= (TextView) rootview.findViewById(R.id.textTitle);
        textViewOverviwe= (TextView) rootview.findViewById(R.id.textOverview);
        textViewreleaseDate= (TextView) rootview.findViewById(R.id.textrelaesDate);
        textViewVoteAverage= (TextView) rootview.findViewById(R.id.textVoteAverage);

        //cardviews For Trailers---------------------------------------------------------
        cardView1= (CardView) rootview.findViewById(R.id.cardview1);
        cardView2= (CardView) rootview.findViewById(R.id.cardview2);
        cardView3= (CardView) rootview.findViewById(R.id.cardview3);
        //--------

        //Reviews-----------------------------------------------
        textViewReview1= (TextView) rootview.findViewById(R.id.textreviwe1);
        textViewReview2= (TextView) rootview.findViewById(R.id.textreviwe2);
        textViewReview3= (TextView) rootview.findViewById(R.id.textreviwe3);

        textViewAuther1= (TextView) rootview.findViewById(R.id.auther1);
        textViewAuther2= (TextView) rootview.findViewById(R.id.auther2);
        textViewAuther3= (TextView) rootview.findViewById(R.id.auther3);

        imageViewauther1= (ImageView) rootview.findViewById(R.id.imageAuther1);
        imageViewauther2= (ImageView) rootview.findViewById(R.id.imageAuther2);
        imageViewauther3= (ImageView) rootview.findViewById(R.id.imageAuther3);

        textViewurl1= (TextView) rootview.findViewById(R.id.texturl1);
        textViewurl2= (TextView) rootview.findViewById(R.id.texturl2);
        textViewurl3= (TextView) rootview.findViewById(R.id.texturl3);
         //----------------------------------------------------
        imageButton= (ImageButton) rootview.findViewById(R.id.favoriteButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    if(!new FavouriteData(getActivity()).GetItem(id).equalsIgnoreCase(id)){
                         imageButton.setImageResource(R.drawable.button_pressed);
                        new FavouriteData(getActivity()).SaveItem(id);

                    }
                else{
                        imageButton.setImageResource(R.drawable.button_normal);
                        new FavouriteData(getActivity()).RemoveItem(id);
                }
            }
        });

        Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185/"+imgurl).into(imageView);
        textViewtitle.setText(model.getOriginal_title());
        textViewOverviwe.setText(model.getOverview());
        textViewreleaseDate.setText(model.getRelease_date());
        textViewVoteAverage.setText(String.valueOf(model.getVote_average()));

        new AsyncReview().execute();
        new AsyncTrailer().execute();

        return rootview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(new FavouriteData(getActivity()).GetItem(id).equalsIgnoreCase(id)){
            imageButton.setImageResource(R.drawable.button_pressed);

        }
        else{
            imageButton.setImageResource(R.drawable.button_normal);

        }
    }

    public class AsyncReview extends AsyncTask<Void, Void, String> {
        ArrayList<ReviewModel> Reviwelist;



        @Override
        protected String doInBackground(Void... voids) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;


            String jsonStr = null;

            try {

                String baseUrl = "http://api.themoviedb.org/3/movie/"+id+"/reviews?";
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
                JSONArray array = object.getJSONArray("results");

                //---------------------------------------------------------//
                Reviwelist=new ArrayList<>();

                for(int i=0;i<array.length();i++){
                    JSONObject finalObject=array.getJSONObject(i);
                    ReviewModel model=new ReviewModel();

                    model.setAuthor(finalObject.getString("author"));
                    model.setContent(finalObject.getString("content"));
                    model.setUrl(finalObject.getString("url"));


                    Reviwelist.add(model);
                }

                //-------------------------------------------------------//


            } catch (JSONException e) {
                e.printStackTrace();
            }
            int size=Reviwelist.size();
                      if(size>=1){
                          textViewReview1.setText(Reviwelist.get(0).getContent());
                          textViewAuther1.setText(Reviwelist.get(0).getAuthor());
                          textViewurl1.setText(Reviwelist.get(0).getUrl());
                          textViewurl1.setTextColor(getResources().getColor(R.color.colorPrimary));
                          textViewurl1.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View v) {
                                  GoToLink(Reviwelist.get(0).getUrl());
                              }
                          });
                      }
                      else {
                          imageViewauther1.setVisibility(View.GONE);
                          textViewReview1.setVisibility(View.GONE);
                          textViewurl1.setVisibility(View.GONE);

                      }

                    if(size>=2){
                        textViewReview2.setText(Reviwelist.get(1).getContent());
                        textViewAuther2.setText(Reviwelist.get(1).getAuthor());
                        textViewurl2.setText(Reviwelist.get(1).getUrl());
                        textViewurl2.setTextColor(getResources().getColor(R.color.colorPrimary));
                        textViewurl2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                GoToLink(Reviwelist.get(1).getUrl());
                            }
                        });
                    }
                    else {
                        imageViewauther2.setVisibility(View.GONE);
                        textViewReview2.setVisibility(View.GONE);
                        textViewurl2.setVisibility(View.GONE);
                    }


                     if(size>=3) {
                         textViewReview3.setText(Reviwelist.get(2).getContent());
                         textViewAuther3.setText(Reviwelist.get(2).getAuthor());
                         textViewurl3.setText(Reviwelist.get(2).getUrl());
                         textViewurl3.setTextColor(getResources().getColor(R.color.colorPrimary));
                         textViewurl3.setOnClickListener(new View.OnClickListener() {
                             @Override
                             public void onClick(View v) {
                                 GoToLink(Reviwelist.get(2).getUrl());
                             }
                         });
                     }
                     else{
                         imageViewauther3.setVisibility(View.GONE);
                         textViewReview3.setVisibility(View.GONE);
                         textViewurl3.setVisibility(View.GONE);
                     }


        }
          public void GoToLink(String Rurl){
              Intent intent=null, chooser=null;

                      intent=new Intent(Intent.ACTION_VIEW);
                      intent.setData(Uri.parse(Rurl));

                      chooser=Intent.createChooser(intent,"open...");
                      if(intent.resolveActivity(getContext().getPackageManager())!=null){
                      startActivity(chooser);

              }
          }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    public class AsyncTrailer  extends AsyncTask<Void, Void, String> {

        ArrayList<TrailerModel> Trailerlist;


        @Override
        protected String doInBackground(Void... voids) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String jsonStr = null;

            try {

                String baseUrl = "http://api.themoviedb.org/3/movie/"+id+"/videos?";
                String apiKey = "api_key=" + BuildConfig.MOVIE_APP_API_KEY;
                URL url = new URL(baseUrl.concat(apiKey));
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
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
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
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
                JSONArray array = object.getJSONArray("results");

                //---------------------------------------------------------//
                Trailerlist=new ArrayList<>();

                for(int i=0;i<array.length();i++){
                    JSONObject FinalObject=array.getJSONObject(i);
                    TrailerModel model=new TrailerModel();

                    model.setKey(FinalObject.getString("key"));

                    Trailerlist.add(model);
                }
                 int size=Trailerlist.size();
                //-------------------------------------------------------//
                     if(size>=1){
                         cardView1.setOnClickListener(new View.OnClickListener() {
                             @Override
                             public void onClick(View v) {
                                 WatchTrailers(Trailerlist.get(0).getKey());
                             }
                         });
                     }else {
                       cardView1.setVisibility(View.GONE);
                     }
                if(size>=2){
                    cardView2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            WatchTrailers(Trailerlist.get(1).getKey());
                        }
                    });

                }else {
                    cardView2.setVisibility(View.GONE);
                }

                if (size>=3){
                    cardView3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            WatchTrailers(Trailerlist.get(2).getKey());
                        }
                    });
                }else {
                    cardView3.setVisibility(View.GONE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        public void WatchTrailers(String id){
            Intent appintent=new Intent(Intent.ACTION_VIEW,Uri.parse("vnd.youtube:"+id));
            Intent webintent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.youtube.com/watch?v="+id));
            try {
           startActivity(appintent);
            }catch (ActivityNotFoundException e){
                 startActivity(webintent);
            }

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }


}
