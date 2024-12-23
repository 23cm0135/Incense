package jec.ac.jp.incense;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private final List<FavoriteItem> favoriteItems;
    private final OnItemRemoveListener removeListener;

    public interface OnItemRemoveListener {
        void onItemRemove(FavoriteItem item);
    }

    public FavoriteAdapter(List<FavoriteItem> favoriteItems, OnItemRemoveListener removeListener) {
        this.favoriteItems = favoriteItems;
        this.removeListener = removeListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavoriteItem item = favoriteItems.get(position);
        holder.bind(item, removeListener);
    }

    @Override
    public int getItemCount() {
        return favoriteItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView incenseImage;
        private final TextView incenseName;
        private final TextView incenseEffect;
        private final Button deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            incenseImage = itemView.findViewById(R.id.incense_image);
            incenseName = itemView.findViewById(R.id.incense_name);
            incenseEffect = itemView.findViewById(R.id.incense_effect);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }

        public void bind(FavoriteItem item, OnItemRemoveListener removeListener) {
            incenseName.setText(item.getName());
            incenseEffect.setText(item.getEffect());
            Glide.with(itemView.getContext()).load(item.getImageUrl()).into(incenseImage);

            deleteButton.setOnClickListener(v -> removeListener.onItemRemove(item));
        }
    }
}
