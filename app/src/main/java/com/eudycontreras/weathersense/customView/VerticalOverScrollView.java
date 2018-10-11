package com.eudycontreras.weathersense.customView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Eudy on 1/29/2017.
 */
public class VerticalOverScrollView extends ScrollView {
    private OnScrollChange scrollListener;

    private boolean mIsFling;
    private OnEndScrollListener onEndScrollListener;

    public VerticalOverScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int computeVerticalScrollRange() {
        return super.computeVerticalScrollRange();
    }

    public void setOnEndScrollListener(OnEndScrollListener mOnEndScrollListener) {
        this.onEndScrollListener = mOnEndScrollListener;
    }

    public void setScrollListener(OnScrollChange listener) {
        scrollListener = listener;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    public void fling(int velocityY) {
        super.fling(velocityY);
        mIsFling = true;
    }


    @Override
    protected void onScrollChanged(int x, int y, int oldX, int oldY) {
        super.onScrollChanged(x, y, oldX, oldY);
        if (scrollListener != null) {
            scrollListener.onScrollChanged(x, y, oldX, oldY);
        }
        if (mIsFling) {
            if (Math.abs(y - oldY) < 0.5 || y >= getMeasuredHeight() || y == 0) {
                if (onEndScrollListener != null) {
                    onEndScrollListener.onEndScroll();
                }
                mIsFling = false;
            }
        }
    }

    public OnEndScrollListener getOnEndScrollListener() {
        return onEndScrollListener;
    }

    public interface OnEndScrollListener {
        void onEndScroll();
    }

    public interface OnScrollChange {
        void onScrollChanged( int newVerticalScroll, int newHorizontalScroll,int oldVerticalScroll, int oldHorizontalScroll);
        void onOverScroll(int scrollX, int scrollY, boolean clampedX, boolean clampedY);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        if(scrollListener !=null){
            scrollListener.onOverScroll(scrollX,scrollY,clampedX,clampedY);
        }
    }

    @SuppressLint("NewApi")
    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
                                   int scrollY, int scrollRangeX, int scrollRangeY,
                                   int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY,
                scrollRangeX, scrollRangeY, maxOverScrollX,
                maxOverScrollY, isTouchEvent);
    }
}