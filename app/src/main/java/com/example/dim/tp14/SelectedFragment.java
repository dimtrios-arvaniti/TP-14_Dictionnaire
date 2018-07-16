package com.example.dim.tp14;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SelectedFragment extends Fragment {

    private TextView selected;
    private TextView title;

    public SelectedFragment() {
        // Required empty public constructor
    }

    public static SelectedFragment newInstance() {
        SelectedFragment fragment = new SelectedFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selected, container, false);
        selected = view.findViewById(R.id.selected_text);
        title = view.findViewById(R.id.selected_title);

        return view;
    }

    public void updateFragment(String item, boolean word) {

            title.setText(word ? "Word" : "Definition");
            selected.setText(item);

    }

}
