package com.applicatum.schafkopfhelfer.utils;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by Alexx on 13.12.2015.
 */
public class CustomScrollView extends ScrollView {

    private static final String TAG = "CustomScrollView";

    float touchX = 0;
    float touchY = 0;

    ViewPager parentPager;

    public void setParentPager(ViewPager parentPager) {
        this.parentPager = parentPager;
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomScrollView(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //return super.onTouchEvent(ev);


        switch (ev.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "ACTION_DOWN");
                touchX = ev.getX();
                touchY = ev.getY();
                return super.onTouchEvent(ev);
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "ACTION_MOVE: "+ Math.abs(touchX-ev.getX()));
                if(Math.abs(touchX-ev.getX())<40){
                    Log.d(TAG, "ACTION_MOVE<40");
                    return super.onTouchEvent(ev);
                }else{
                    Log.d(TAG, "ACTION_MOVE to pager");
                    if (parentPager==null) {
                        return false;
                    } else {
                        return parentPager.onTouchEvent(ev);
                    }
                }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "ACTION_UP");
                touchX=0;
                touchY=0;
                break;
        }
        //return false;
        return super.onTouchEvent(ev);
    }
}
