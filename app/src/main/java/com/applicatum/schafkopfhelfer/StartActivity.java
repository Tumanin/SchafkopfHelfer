package com.applicatum.schafkopfhelfer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.applicatum.schafkopfhelfer.fragments.StartFragment;
import com.applicatum.schafkopfhelfer.models.Player;

/**
 * Created by Alexx on 07.11.2015.
 */
public class StartActivity extends AppCompatActivity {

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Player.findById(Player.class, (long)1);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

    public void setToolbarTitle(String title){
        toolbar.setTitle(title);
    }

    @Override
    protected void onResume() {
        super.onResume();
        StartFragment fragment = new StartFragment();
        startFragment(fragment, false);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void startFragment(Fragment fragment, boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);

        if(addToBackStack) {
            fragmentTransaction.addToBackStack("frame");
        }
        else {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        fragmentTransaction.commit();
    }
}
