package com.example.moetaz.movieapp.Activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moetaz.movieapp.Fragment.DetailFragment;
import com.example.moetaz.movieapp.Fragment.MainFragment;
import com.example.moetaz.movieapp.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Moetaz on 11/4/2016.
 */

public class DetailActivity extends AppCompatActivity {
      @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fdetail, new DetailFragment())
                    .commit();
        }

    }


}
