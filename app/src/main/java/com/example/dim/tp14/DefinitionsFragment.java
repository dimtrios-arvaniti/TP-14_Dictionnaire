package com.example.dim.tp14;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.view.ActionMode;
import android.support.v7.view.ActionMode.Callback;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dim.tp14.utils.BasicRecyclerAdapter;
import com.example.dim.tp14.utils.TabInterface;
import com.example.dim.tp14.utils.TabListActionInterface;

import java.util.ArrayList;
import java.util.List;

public class DefinitionsFragment extends Fragment implements TabInterface {

    private static final String ARG_ITEM_ = "ARG_ITEM_";
    private static final String ARG_COUNT = "ARG_COUNT";
    private static final String ARG_DEFS_TITLE = "DEFS";

    private String title;
    private int count;
    private ArrayList<String> items;
    private TabListActionInterface listener;
    private BasicRecyclerAdapter adapter;
    private RecyclerView recyclerView;

    public DefinitionsFragment() {
        // Required empty public constructor
    }


    public static DefinitionsFragment newInstance(List<String> definitions,
                                                  String title) {
        DefinitionsFragment fragment = new DefinitionsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DEFS_TITLE, title);
        args.putInt(ARG_COUNT, definitions.size());

        int dcount = 0;
        for (String definition : definitions) {
            args.putString((ARG_ITEM_ + dcount), definition);
            dcount += 1;
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_definitions, container, false);

        recyclerView = view.findViewById(R.id.definitions_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        return view;
    }

    public void onItemSelected(String item) {
        listener.onItemClick(item);
    }

    @Override
    public void onLongItemClick(String item) {
        listener.onLongItemClick(item);
    }

    @Override
    public void onLongItemActionClick(ActionMode mode, boolean delete) {
        listener.onLongItemActionClick(mode, delete);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // init
        items = new ArrayList<>();
        adapter = new BasicRecyclerAdapter(this);

        title = getArguments().getString(ARG_DEFS_TITLE);
        count = getArguments().getInt(ARG_COUNT);

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
    public void updateTab(String item, boolean delete) {
        if (delete) {
            int pos = items.indexOf(item);
            items.remove(item);
            adapter.notifyItemRemoved(pos);

        } else {
            items.add(item);
            adapter.notifyItemInserted(getItems().size() - 1);
        }
    }

    @Override
    public boolean isAdminMode() {
        return PreferenceManager.getDefaultSharedPreferences(getContext())
                .getBoolean("adn", false);
    }

    @Override
    public ActionMode startActionMode(Callback callback) {
        return ((MainActivity) getActivity()).startSupportActionMode(callback);
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<String> getItems() {
        return items;
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
}
