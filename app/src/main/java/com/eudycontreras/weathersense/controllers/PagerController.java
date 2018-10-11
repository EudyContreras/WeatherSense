package com.eudycontreras.weathersense.controllers;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eudycontreras.weathersense.R;
import com.eudycontreras.weathersense.weather.WeatherActivity;


/**
 * Activity which uses ViewPager to show a specified number of slides
 * using transitions. The welcome slide presentation will be shown the first time the
 * user logs into the application. The slides can be reset through a options menu
 * so they can be seen once again
 * @author Eudy Contreras
 */
public class PagerController {

    private ViewPager viewPager;
    private MyViewPagerAdapter viewPagerAdapter;
    private ViewPager.OnPageChangeListener changeListener;
    private WeatherActivity activity;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private ImageView[] images;
    private Drawable[] icons;
    private int[] layouts;
    private Button btnSkip, btnNext;

    public PagerController(WeatherActivity activity){
        this.activity = activity;
        initComponents();
        addBottomDots(0);
    }

    /**
     * Method which loads the visual and non visual components
     * used by this class
     */
    private void initComponents(){
        layouts = new int[]{
                R.layout.fragment_details};

        viewPager = (ViewPager) activity.findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) activity.findViewById(R.id.layoutDots);

//        images = new ImageView[4];
//        icons = new Drawable[4];
//
//        images[0] = (ImageView) findViewById(R.id.welcome_slides_picture_one);
//        images[1] = (ImageView) findViewById(R.id.welcome_slides_picture_two);
//        images[2] = (ImageView) findViewById(R.id.welcome_slides_picture_three);
//        images[3] = (ImageView) findViewById(R.id.welcome_slides_picture_four);
//
//        icons[0] = ContextCompat.getDrawable(this,R.drawable.tile_leisure_icon);
//        icons[1] = ContextCompat.getDrawable(this,R.drawable.tile_clothing_icon);
//        icons[2] = ContextCompat.getDrawable(this,R.drawable.tile_housing_icon);
//        icons[3] = ContextCompat.getDrawable(this,R.drawable.tile_transportation_icon);
////        for(int i = 0; i< images.length; i++){
////            icons[i].setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
////            images[i].setImageDrawable(icons[i]);
////        }
        viewPagerAdapter = new MyViewPagerAdapter();

        viewPager.setAdapter(viewPagerAdapter);
        changeListener = new viewPagerListener();
        viewPager.addOnPageChangeListener(changeListener);
    }


    /**
     * Add bottom dots to the slides which represent the current slide and
     * the amount of slide the welcome presentation has.The method is called
     * every time the a slide happens. The dots colors are matched to the color
     * of the current slide. The bottom dots have two states: active and inactive.
     * The state is determine by current page.
     * @param currentPage Current page of the welcome slide presentation
     */
    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int colorsActive = ContextCompat.getColor(activity,R.color.white);
        int colorsInactive = ContextCompat.getColor(activity,R.color.pale);

        dotsLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(activity);
            dots[i].setText(fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive);
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    /**
     * Method which returns the index of current item being shown
     * in the view pager.
     * @return index of the currently shown item
     */
    private int getItem() {
        return viewPager.getCurrentItem() + 1;
    }


    /**
     * Class which implements a ViewPager On page change listener.
     * The class listens for slide events and binds specific events
     * to slide events.
     */
    private class viewPagerListener implements ViewPager.OnPageChangeListener{
        /**
         * Method called on page selection events. The method
         * determines what is shown on the under controls of the
         * welcome activity.
         * @param position
         */
        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
        }
        @Override
        public void onPageScrollStateChanged(int state) {}
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
    }

    /**
     * View pager adapter class which extends PagerAdapter.
     * The class is used for managing the views/Slides used by the
     * Welcome slide presentation activity
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        /**
         * Instantiates the currently view slide.
         * @param container The View pager
         * @param position The current slide index
         * @return returns a view.
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);
            return view;
        }

        /**
         * Returns the amount of slides
         * @return
         */
        @Override
        public int getCount() {
            return layouts.length;
        }

        /**
         * Checks if a given object equals a given view
         * and returns the result.
         * @param view View to check
         * @param obj Object to check
         * @return result
         */
        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        /**
         * Method which destroys/removes a specified item or view
         * from a specified  group container or parent.
         * @param container Parent/Group container
         * @param position Index of the view
         * @param object View to be removed
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}