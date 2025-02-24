package jec.ac.jp.incense;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private ArrayList<FavoriteItem> favoriteList;
    private OnDeleteClickListener onDeleteClickListener;
    private Context context;

    public interface OnDeleteClickListener {
        void onDeleteClick(FavoriteItem item);
    }

    public FavoriteAdapter(Context context, ArrayList<FavoriteItem> favoriteList, OnDeleteClickListener listener) {
        this.context = context;
        this.favoriteList = favoriteList;
        this.onDeleteClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavoriteItem item = favoriteList.get(position);
        holder.nameTextView.setText(item.getName());

        // 使用 Glide 加载网络图片
        if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(item.getImageUrl())
                    .placeholder(R.drawable.default_image)
                    .error(R.drawable.default_image)
                    .into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.default_image);
        }

        // 删除按钮点击事件
        holder.deleteButton.setOnClickListener(v -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDeleteClick(item);
            }
        });

        // 点击商品，统一跳转到 MinuteActivity 并传递网络图片 URL
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MinuteActivity.class);
            intent.putExtra("EXTRA_TEXT", item.getDescription());
            intent.putExtra("EXTRA_IMAGE_URL", item.getImageUrl());
            intent.putExtra("EXTRA_URL", item.getUrl());
            intent.putExtra("INCENSE_ID", item.getName());
            intent.putExtra("INCENSE_NAME", item.getName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        ImageView imageView;
        Button deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.incense_name);
            imageView = itemView.findViewById(R.id.incense_image);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}
