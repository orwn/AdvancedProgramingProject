package com.example.orwn.ex4.Manu;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.orwn.ex4.FriendList.FriendListFragment;
import com.example.orwn.ex4.R;


public class MenuActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manu);


        FragmentManager fm = getFragmentManager();
        MenuFragment mf = new MenuFragment();
        FriendListFragment fif = new FriendListFragment();


        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.manu_frame, mf);
        ft.addToBackStack("Menu");

        ft.commit();
        setActionBar();
    }

    private void setActionBar()
    {
       android.support.v7.app.ActionBar mActionBar = getSupportActionBar();

        //mActionBar.setDisplayShowTitleEnabled(false);
        if (mActionBar != null) {
            mActionBar.setDisplayShowHomeEnabled(false);
            mActionBar.setDisplayShowTitleEnabled(false);
        }
        else
        {
            Log.i("setActionBar","equals NULL");
        }




    }

}
