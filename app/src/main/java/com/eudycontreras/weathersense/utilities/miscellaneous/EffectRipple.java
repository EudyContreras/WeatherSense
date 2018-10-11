package com.eudycontreras.weathersense.utilities.miscellaneous;

import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;

import java.util.Arrays;

/**
 * Created by Eudy on 9/18/2016.
 */
public class EffectRipple {

    private StateListDrawable states;
    private RoundRectShape roundRectShape;
    private ShapeDrawable shapeDrawable;
    private float[] outerRadii = new float[8];

    public EffectRipple(){
        initComponents();
    }
    private void initComponents(){
        Arrays.fill(outerRadii, 3);
        roundRectShape = new RoundRectShape(outerRadii, null, null);
        shapeDrawable = new ShapeDrawable(roundRectShape);
        states = new StateListDrawable();
    }

    public Drawable getAdaptiveRippleDrawable(int normalColor, int pressedColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return new RippleDrawable(ColorStateList.valueOf(pressedColor), null, getRippleMask(normalColor));
        } else {
            return getStateListDrawable(normalColor, pressedColor);
        }
    }

    private Drawable getRippleMask(int color) {
        shapeDrawable.getPaint().setColor(color);
        return shapeDrawable;
    }

    public StateListDrawable getStateListDrawable(int normalColor, int pressedColor) {
        states.clearColorFilter();
        states.addState(new int[]{android.R.attr.state_pressed},
                new ColorDrawable(pressedColor));
        states.addState(new int[]{android.R.attr.state_focused},
                new ColorDrawable(pressedColor));
        states.addState(new int[]{android.R.attr.state_activated},
                new ColorDrawable(pressedColor));
        states.addState(new int[]{},
                new ColorDrawable(normalColor));
        return states;
    }

}
