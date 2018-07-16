package com.example.dim.tp14;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SelectedFragment extends Fragment {

    private static final String ARG_SELECTED = "ARG_SELECTED";
    private static final String ARG_TYPE = "ARG_TYPE";

    private String selected;
    private boolean definition;

    public SelectedFragment() {
        // Required empty public constructor
    }

    public static SelectedFragment newInstance(String selected, boolean definition) {
        SelectedFragment fragment = new SelectedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SELECTED, selected);
        args.putBoolean(ARG_TYPE, definition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selected = getArguments().getString(ARG_SELECTED);
            definition = getArguments().getBoolean(ARG_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selected, container, false);
        TextView tv = view.findViewById(R.id.selected_text);

        boolean valid = selected != null && !selected.isEmpty();
        tv.setText(valid ? selected : "");

        return view;
    }


}
