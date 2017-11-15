package com.example.moetaz.movieapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.moetaz.movieapp.activity.DetailActivity;
import com.example.moetaz.movieapp.activity.MainActivity;
import com.example.moetaz.movieapp.utilities.MyUtilities;
import com.example.moetaz.movieapp.fragment.DetailFragment;
import com.example.moetaz.movieapp.models.MovieModel;
import com.example.moetaz.movieapp.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by Moetaz on 10/29/2016.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    private final String BaseUrl ="http://image.tmdb.org/t/p/w185/";
    private Context context;
    private ArrayList<MovieModel> data;

    public CustomAdapter(Context context, ArrayList<MovieModel> data) {
        this.context=context;
        this.data=data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_row, parent, false);

        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final MovieModel moviemodel = data.get(position);

        if(MyUtilities.isNetworkConnected(context)){
            Picasso.with(context).load(BaseUrl+moviemodel.getPoster_path()).into(holder.imgview);
        }
        else
        Picasso.with(context).load(BaseUrl+moviemodel.getPoster_path())
                .networkPolicy(NetworkPolicy.OFFLINE).into(holder.imgview, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show();
            }
        });

        holder.imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!MainActivity.IsTowPane){
                    Intent intent= new Intent(context,DetailActivity.class);
                    intent.putExtra("modelPass",moviemodel);
                    context.startActivity(intent);
                }
                else {
                    DetailFragment detailFragment=new DetailFragment();
                    Bundle b=new Bundle();
                    b.putSerializable("modelPass",moviemodel);
                    detailFragment.setArguments(b);
                    ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fdetail,detailFragment)
                            .commit();
                }

            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imgview;

        public MyViewHolder(View itemView){
            super(itemView);
            imgview= (ImageView) itemView.findViewById(R.id.img_row);

        }

    }
}

