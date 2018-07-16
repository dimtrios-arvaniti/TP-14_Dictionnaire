package com.example.dim.tp14;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dim.tp14.utils.BasicRecyclerAdapter;
import com.example.dim.tp14.utils.TabInterface;
import com.example.dim.tp14.utils.TabListActionInterface;

import java.util.ArrayList;
import java.util.List;

public class WordsFragment extends Fragment implements TabInterface {

    private static final String ARG_COUNT = "ARG_COUNT";
    private static final String ARG_ITEM_ = "ARG_ITEM_";
    private static final String ARG_MOTS_TITLE = "MOTS";

    private String title;
    private int count;
    private ArrayList<String> items;
    private TabListActionInterface listener;
    private RecyclerView recyclerView;
    private BasicRecyclerAdapter adapter;


    public WordsFragment() {
        // Required empty public constructor
    }

    public static WordsFragment newInstance(List<String> mots, String title) {
        WordsFragment fragment = new WordsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MOTS_TITLE, title);
        Log.i("TEST", "newInstance: WORDS COUNT = " + mots.size());

        int dcount = 0;
        for (String mot : mots) {
            Log.i("TEST", "newInstance: " + mot);
            args.putString((ARG_ITEM_ + dcount), mot);
            dcount += 1;
        }
        args.putInt(ARG_COUNT, dcount);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_mots, container, false);
        //View view = super.onCreateView(inflater, container, savedInstanceState);

        recyclerView = view.findViewById(R.id.mots_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        return view;
    }

    public void onItemSelected(String item) {
        listener.onItemClick(item);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // init
        items = new ArrayList<>();
        adapter = new BasicRecyclerAdapter(this);

        title = getArguments().getString(ARG_MOTS_TITLE);
        count = getArguments().getInt(ARG_COUNT);
        //Log.i("TEST", "onCreate: COUNT = " + count);

        // make data from bundle
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                String string = getArguments().getString(ARG_ITEM_ + i);
                //Log.i("TEST", "onCreate: WORD ITEM " + string);
                items.add(string);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TabListActionInterface) {
            listener = (TabListActionInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement TabListActionInterface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<String> getItems() {
        return items;
    }

}