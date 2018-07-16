package com.example.dim.tp14.utils;

import java.util.List;

public interface TabInterface {

    void onItemSelected(String item);
    List<String> getItems();
    String getTitle();
    void updateTab(String item, boolean delete);

}
