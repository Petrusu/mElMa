package com.example.melma.more;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.melma.R;

import java.util.List;

import models.Favorite;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private Context context;
    private List<Favorite> favorites;
    private OnItemClickListener onItemClickListener;

    public FavoriteAdapter(Context context, List<Favorite> favorites, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.favorites = favorites;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Favorite favorite = favorites.get(position);
        holder.tvItem.setText(favorite.getTitle());
        holder.bind(favorite, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Favorite book);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(R.id.tvItem);
        }

        public void bind(final Favorite favorite, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(favorite);
                }
            });
        }
    }
}
