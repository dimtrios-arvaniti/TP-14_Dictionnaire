package com.example.dim.tp14.utils;

import android.support.v7.view.ActionMode;

import java.util.List;

public interface TabInterface {

    // on list item click
    void onItemSelected(String item);
    // update activity on list long click
    void onLongItemClick(String item);
    // update on context menu item click
    void onLongItemActionClick(ActionMode mode, boolean delete);
    // get list
    List<String> getItems();
    // title aka tag
    String getTitle();

    boolean isAdminMode();
    // start context menu
    ActionMode startActionMode(ActionMode.Callback callback);
    // update fragment
    void updateTab(String item, boolean delete);

}
