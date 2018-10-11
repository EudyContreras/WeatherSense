package com.eudycontreras.weathersense.utilities.animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * @author  Eudy Contreras
 * This class helps with resizing animation
 */
public class ResizeAnimation extends Animation {
    private final double startDimension;
    private final double targetDimension;
    private ResizeAxis resizeAxis;
    private View view;

    public ResizeAnimation(View view, double startDimension, double targetDimension, ResizeAxis resizeAxis) {
        this.view = view;
        this.resizeAxis = resizeAxis;
        this.targetDimension = targetDimension;
        this.startDimension = startDimension;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        switch (resizeAxis){
            case RESIZE_WIDTH:
                int newWidth = (int) (startDimension+(targetDimension - startDimension) * interpolatedTime);
                view.getLayoutParams().width = newWidth;
                break;
            case RESIZE_HEIGHT:
                int newHeight = (int) (startDimension+(targetDimension - startDimension) * interpolatedTime);
                view.getLayoutParams().height = newHeight;
                break;
        }
        view.requestLayout();
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }

    public enum ResizeAxis{
        RESIZE_HEIGHT,
        RESIZE_WIDTH,
    }
}