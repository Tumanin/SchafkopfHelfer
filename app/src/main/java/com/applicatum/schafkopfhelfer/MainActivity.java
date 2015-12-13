package com.applicatum.schafkopfhelfer;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.applicatum.schafkopfhelfer.fragments.GameMainFragment;
import com.applicatum.schafkopfhelfer.fragments.GameTableFragment;
import com.applicatum.schafkopfhelfer.fragments.StartFragment;
import com.applicatum.schafkopfhelfer.fragments.StatisticsFragment;
import com.viewpagerindicator.CirclePageIndicator;

public class MainActivity extends AppCompatActivity {

    SectionsPagerAdapter mSectionsPagerAdapter;
    private CirclePageIndicator circlePageIndicator;
    ViewPager mViewPager;
    Toolbar toolbar;
    MainActivity mActivity;
    GameTableFragment tableFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.title_game_main_fragment));
        setSupportActionBar(toolbar);
        mActivity = this;
        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        /*
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        circlePageIndicator = (CirclePageIndicator) findViewById(R.id.indicatorGame);
        circlePageIndicator.setViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mViewPager.getParent().requestDisallowInterceptTouchEvent(true);
                circlePageIndicator.onPageScrolled(mViewPager.getCurrentItem(), 0, 0);
            }


            @Override
            public void onPageSelected(int index) {

                //updateChannelNames(index);
                toolbar.setTitle(mSectionsPagerAdapter.getPageTitle(index));
                //mSectionsPagerAdapter.getPageTitle(index);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d("CustomScrollView", "PAGER ON TOUCH CATCH");
                return false;
            }
        });
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */

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

    public void updateTable(){
        if(tableFragment!=null){
            tableFragment.updateTable();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this.getBaseContext(), StartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        this.startActivity(intent);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            Log.d("SectionsPagerAdapter", "getItem: " + position);
            Fragment fragment;
            switch (position){
                case 0:
                    fragment = new GameMainFragment();
                    break;
                case 1:
                    fragment = new GameTableFragment();
                    tableFragment = (GameTableFragment)fragment;
                    break;
                case 2:
                    fragment = new StatisticsFragment();
                    break;
                default:
                    fragment = new GameMainFragment();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {

            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            String title = "";
            switch (position){
                case 0:
                    title = getResources().getString(R.string.title_game_main_fragment);
                    break;
                case 1:
                    title = getResources().getString(R.string.title_game_table_fragment);
                    break;
                case 2:
                    title = getResources().getString(R.string.title_statistic_fragment);
                    break;
                default:
                    title = getResources().getString(R.string.title_game_main_fragment);
                    break;
            }

            return title;
        }

        @Override
        public long getItemId(int position) {
            Log.d("SectionsPagerAdapter", "getItemId: "+position);
            return super.getItemId(position);
        }

        @Override
        public int getItemPosition(Object object) {
            if(object instanceof GameMainFragment) return 0;
            if(object instanceof GameTableFragment) return 1;
            if (object instanceof StatisticsFragment) return 2;
            else return POSITION_NONE;
        }
    }

    public void setTitle(String title){
        toolbar.setTitle(title);
    }
}
