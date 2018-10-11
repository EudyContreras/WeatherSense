package com.eudycontreras.weathersense.controllers;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.eudycontreras.weathersense.R;


/**
 * @author  Eudy Contreras
 * This class is the controller which handles which
 * fragments is currently displayed. This class allows
 * dynamic switching of fragments without removing any fragment
 */
public class FragmentViewController {

    private FragmentManager fragmentManager;
    private String currentTag;
    private String nothingTag;
    private int container;

    public FragmentViewController(FragmentManager fragmentManager, int container) {
        this.fragmentManager = fragmentManager;
        this.container = container;
        this.nothingTag = "nothing";
    }

    public void setCurrentTag(String currentTag) {
        this.currentTag = currentTag;
    }

    public String getCurrentTag() {
        return currentTag;
    }

    public void add(Fragment fragment, String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (!fragment.isAdded()) {
            transaction.add(container, fragment, tag);
        }

        transaction.hide(fragment);
        transaction.commit();
    }

    public String show() {
        return show(currentTag);
    }

    public String show(String tag) {

        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        Fragment currentFragment = fragmentManager.findFragmentByTag(currentTag);

        if (fragment != null) {

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            if (!tag.equals(currentTag)) {

                fragmentTransaction.setCustomAnimations(
                        R.anim.fragment_pop_up_animation,
                        R.anim.fragment_pop_down_animation,
                        R.anim.fragment_pop_up_animation,
                        R.anim.fragment_pop_down_animation);
            }
            if (currentFragment != null) {
                fragmentTransaction.hide(currentFragment);
            }

            fragmentTransaction.show(fragment);
            fragmentTransaction.commit();

            currentTag = tag;
        }
        return currentTag;
    }
    public String hide() {

        Fragment currentFragment = fragmentManager.findFragmentByTag(currentTag);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (currentFragment != null) {
            fragmentTransaction.setCustomAnimations(
                    R.anim.fragment_pop_up_animation,
                    R.anim.fragment_pop_down_animation,
                    R.anim.fragment_pop_up_animation,
                    R.anim.fragment_pop_down_animation);


            fragmentTransaction.hide(currentFragment);
            fragmentTransaction.commit();

        }
        currentTag = nothingTag;
        return currentTag;
    }


}
