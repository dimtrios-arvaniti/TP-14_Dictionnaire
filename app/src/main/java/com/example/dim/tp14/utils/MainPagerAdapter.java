package com.example.dim.tp14.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.HashMap;

public class MainPagerAdapter extends FragmentPagerAdapter {

    private static final String PAGE_MOTS = "PAGE_MOTS";
    private static final String PAGE_DEFS = "PAGE_DEFS";
    private static final String PAGE_SEL = "PAGE_SEL";

    private boolean delMod;
    private HashMap<Integer, Fragment> fragments;

    public MainPagerAdapter(FragmentManager fm, HashMap<Integer, Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
        delMod = false;
    }


    /**
     * @see android.support.v4.view.PagerAdapter#getItemPosition(java.lang.Object)
     */
    @Override
    public int getItemPosition(Object object) {

        String objClass = object.getClass().getSimpleName();

        if (delMod) {
            if (objClass.equalsIgnoreCase("WordsFragment")) {
                return 0;
            } else {
                return POSITION_NONE;
            }
        }

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
        return fragments.get(position);

    }

    /**
     * Returns 3 pages or 1 only if deletion Mode is active
     * @return
     */
    @Override
    public int getCount() {
        return delMod ? 1 : 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return position == 0 ? "Mots" :
                position == 1 ? "Definition" :
                        "Selection";
    }

    public boolean isDelMod() {
        return delMod;
    }

    public void setDelMod(boolean delMod) {
        this.delMod = delMod;
    }
}

