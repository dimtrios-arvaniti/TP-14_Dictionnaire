package com.example.dim.tp14.utils;

import android.support.annotation.NonNull;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dim.tp14.R;

public class BasicRecyclerAdapter<T extends TabInterface> extends RecyclerView.Adapter<BasicRecyclerAdapter.ViewHolder> {

    private T parent;
    private ActionMode mActionMode;

    private View.OnLongClickListener onLongClickListener = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            if (mActionMode != null) {
                return false;
            }

            mActionMode = parent.startActionMode(mActionModeCallback);
            view.setSelected(true);
            parent.onLongItemClick(view.getTag().toString());
            return true;
        }
    };

    private View.OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (null == view.getTag()) {
                return;
            }

            String[] str = view.getTag().toString().split("_");
            if (str[0].isEmpty() || str[1].isEmpty()) {
                return;
            }

            parent.onItemSelected(view.getTag().toString());
        }
    };

    public BasicRecyclerAdapter(T parent) {
        this.parent = parent;
    }

    @NonNull
    @Override
    public BasicRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new BasicRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BasicRecyclerAdapter.ViewHolder holder, int position) {
        String item = parent.getItems().get(position);
        Log.i("TEST", "onBindViewHolder: " + item);
        holder.vhLabel.setText(item);
        holder.vhLayout.setTag(parent.getTitle()+"_" + item);
    }

    @Override
    public int getItemCount() {
        return parent.getItems().size();
    }



    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView vhLabel;
        final LinearLayout vhLayout;

        ViewHolder(View view) {
            super(view);
            vhLabel = view.findViewById(R.id.itemText);
            vhLayout = view.findViewById(R.id.itemLayout);
            vhLayout.setOnClickListener(onClickListener);
            vhLayout.setOnLongClickListener(onLongClickListener);
        }
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            //Log.i("TEST", "onActionItemClicked: "+mode.getTag().toString());
            switch (item.getItemId()) {
                case R.id.ctx_nav_detail:
                    parent.onLongItemActionClick(mode, false);
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                case R.id.ctx_nav_effacer:
                    parent.onLongItemActionClick(mode, true);
                    //mode.finish(); // Action picked, so close the CAB
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
        }
    };
}
