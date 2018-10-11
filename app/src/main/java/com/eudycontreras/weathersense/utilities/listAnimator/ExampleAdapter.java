package com.eudycontreras.weathersense.utilities.listAnimator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Eudy on 9/14/2016.
 * Example adapter which can be used for a custom list
 * item that can be deleted with an animation. This class is
 * just a basic reference showing the many ways a list item can
 * be animated and remove in order to create a better visual experience.
 * The
 */
public class ExampleAdapter extends ArrayAdapter<ExampleData>{

    private Activity context;
    private ArrayList<ExampleData> rowData;

    public ExampleAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder viewHolder;
        View layout = null; //The layout of the list item.

        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        addAnimationHandler(viewHolder.wrapper,layout,viewHolder.deleteButton,position);

        return convertView;
    }

    /**
     * Method which adds and animation to a listView cell based on input.
     * Params can be refined for control.
     * @param wrapper Assuming that the view ListView is wrapped around a layout.
     * @param listItemLayout The actual View inflated form the list cell layout
     * @param possibleDeleteButton Possible delete button that will trigger the delete animation
     * @param viewIndex The index of the clicked cell.
     */
    private void addAnimationHandler(View wrapper, View listItemLayout, View possibleDeleteButton, int viewIndex){
        wrapper.setOnClickListener(v -> {
            wrapper.animate().translationX(-300);
            new android.os.Handler().postDelayed(
                    () -> wrapper.animate().translationX(0), 2500);
        });
        possibleDeleteButton.setOnClickListener((v) ->{
            listItemLayout.animate().alpha(0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    deleteRow(rowData,ExampleAdapter.this,listItemLayout,viewIndex);
                }
            });
        });
    }

    /**
     * Deletes the selected row
     * @param list The list used for this adapter
     * @param adapter The instance of the array adapter
     * @param view The view to be animated
     * @param index The index of the selected item.
     */
    private void deleteRow(final ArrayList list, final ArrayAdapter adapter, final View view, final int index) {

        Animation.AnimationListener animationListener = new Animation.AnimationListener() {

            @Override
            public void onAnimationEnd(Animation arg0) {
                list.remove(index);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationStart(Animation animation) {}
        };

        collapse(view, animationListener);
    }

    /**
     * Collapse animation used to delete the item from the list view.
     * @param view
     * @param listener
     */
    private void collapse(final View view, Animation.AnimationListener listener) {
        final int initialHeight = view.getMeasuredHeight();

        Animation anim = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation transformation) {
                if (interpolatedTime == 1) {
                    view.setVisibility(View.GONE);
                }
                else {
                    view.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    view.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        if (listener!=null) {
            anim.setAnimationListener(listener);
        }
        anim.setDuration(250);
        view.startAnimation(anim);
    }


    private class ViewHolder{
        LinearLayout wrapper;
        TextView content;
        ImageView deleteButton;

    }


}