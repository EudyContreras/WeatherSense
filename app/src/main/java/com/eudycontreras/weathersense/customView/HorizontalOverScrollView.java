package com.eudycontreras.weathersense.customView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.HorizontalScrollView;

/**
 * Created by Eudy on 1/29/2017.
 */

public class HorizontalOverScrollView extends HorizontalScrollView {
    private ScrollCallbacks mCallbacks;

    private static final int MAX_X_OVER_SCROLL_DISTANCE = 100;

    private Context mContext;
    private int maxOverScroll;

    public HorizontalOverScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initBounceScrollView();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mCallbacks != null) {
            mCallbacks.onScrollChanged(l, t, oldl, oldt);
        }
    }

    @Override
    public int computeVerticalScrollRange() {
        return super.computeVerticalScrollRange();
    }

    public void setCallbacks(ScrollCallbacks listener) {
        mCallbacks = listener;
    }

    private void initBounceScrollView() {
        final DisplayMetrics metrics = mContext.getResources()
                .getDisplayMetrics();
        final float density = metrics.density;

        maxOverScroll = (int) (density * MAX_X_OVER_SCROLL_DISTANCE);

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    private interface ScrollCallbacks {
        void onScrollChanged(int l, int t, int oldl, int oldt);
    }

    @SuppressLint("NewApi")
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
                                   int scrollY, int scrollRangeX, int scrollRangeY,
                                   int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        // This is where the magic happens, we have replaced the incoming
        // maxOverScrollY with our own custom variable maxOverScroll;
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY,
                scrollRangeX, scrollRangeY, maxOverScrollX,
                maxOverScroll, isTouchEvent);
    }

}