package com.example.dim.tp14.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dim.tp14.R;

public class BasicRecyclerAdapter<T extends TabInterface> extends RecyclerView.Adapter<BasicRecyclerAdapter.ViewHolder> {

    private T parent;

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
        }
    }
}
