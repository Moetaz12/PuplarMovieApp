package com.example.moetaz.movieapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.moetaz.movieapp.fragment.DetailFragment;
import com.example.moetaz.movieapp.R;

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
