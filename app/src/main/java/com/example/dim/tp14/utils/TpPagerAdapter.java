package com.example.dim.tp14.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.example.dim.tp14.MainActivity;

import java.util.HashMap;

public class TpPagerAdapter extends FragmentPagerAdapter {

    private static final String PAGE_MOTS = "PAGE_MOTS";
    private static final String PAGE_DEFS = "PAGE_DEFS";
    private static final String PAGE_SEL = "PAGE_SEL";

    private int currentPage;
    private HashMap<Integer, Fragment> fragments;

    public TpPagerAdapter(FragmentManager fm, MainActivity activity, HashMap<Integer, Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
        currentPage = 0;
    }


    /**
     * @see android.support.v4.view.PagerAdapter#getItemPosition(java.lang.Object)
     */
    @Override
    public int getItemPosition(Object object) {

        String objClass = object.getClass().getSimpleName();

        if (objClass.equalsIgnoreCase("WordsFragment")) {
            return 0;
        }
        if (objClass.equalsIgnoreCase("DefinitionsFragment")) {
            return 1;
        }
        if (objClass.equalsIgnoreCase("SelectedFragment")) {
            return 2;
        }
        Log.i("TEST", "getItemPosition: POSITION_NONE !!!!!!!!");
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        currentPage = position;
        return fragments.get(position);

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return position == 0 ? "Mots" :
                position == 1 ? "Definition" :
                        "Selection";
    }

}

