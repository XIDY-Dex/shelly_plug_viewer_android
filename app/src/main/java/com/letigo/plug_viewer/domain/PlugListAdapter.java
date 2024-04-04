package com.letigo.plug_viewer.domain;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alexk.schooltodo.R;
import com.letigo.plug_viewer.data.db.PlugEntity;

import java.util.ArrayList;

public class PlugListAdapter extends RecyclerView.Adapter<PlugListAdapter.ViewHolder> {
    public ArrayList<PlugEntity> plugs;
    private PlugListAdapter.OnPlugClick onPlugClick;
    private PlugListAdapter.onPlugLongClick onPlugLongClick;
    private MenuInflater menuInflater;


    public interface OnPlugClick {
        void onPlugClick(PlugEntity plug, int position);
    }

    public interface onPlugLongClick {
        void onPlugLongClick(PlugEntity plug, int position);
    }

    public PlugListAdapter(ArrayList<PlugEntity> plugs, OnPlugClick onPlugClick, Context context, onPlugLongClick long_listener) {
        this.plugs = plugs;
        this.onPlugClick = onPlugClick;
        this.menuInflater = new MenuInflater(context);
        this.onPlugLongClick = long_listener;
    }

    @NonNull
    @Override
    public PlugListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.plug_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlugListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.plugName.setText(plugs.get(position).plugName);
        holder.itemView.setOnClickListener(view -> onPlugClick.onPlugClick(plugs.get(position), position));
        holder.itemView.setOnLongClickListener(view -> {
            onPlugLongClick.onPlugLongClick(plugs.get(position), position);
            return true;
        });
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        holder.itemView.setOnClickListener(null);
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return plugs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView plugName;
        ImageView plugImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.plugName = itemView.findViewById(R.id.plugName);
            this.plugImage = itemView.findViewById(R.id.plugImage);
        }
    }

    public void updatePlugs(ArrayList<PlugEntity> new_plugs) {
        this.plugs = new_plugs;
        notifyDataSetChanged();
    }
}
