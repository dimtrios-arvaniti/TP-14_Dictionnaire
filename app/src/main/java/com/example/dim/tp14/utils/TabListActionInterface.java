package com.example.dim.tp14.utils;

import android.support.v7.view.ActionMode;

public interface TabListActionInterface {

    void onItemClick(String item);
    void onLongItemClick(String item);
    void onLongItemActionClick(ActionMode mode, boolean delete);
}
