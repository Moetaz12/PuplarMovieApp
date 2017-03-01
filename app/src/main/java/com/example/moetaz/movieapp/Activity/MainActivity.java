package com.example.moetaz.movieapp.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.moetaz.movieapp.Adapter.CustomAdapter;
import com.example.moetaz.movieapp.Fragment.MainFragment;
import com.example.moetaz.movieapp.R;

public class MainActivity extends AppCompatActivity {
      public static boolean IsTowPane=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fmain, new  MainFragment())
                    .commit();
        }
            if(null !=findViewById(R.id.fdetail)){
                IsTowPane=true;
            }

    }





}
