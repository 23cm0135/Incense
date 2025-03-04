package jec.ac.jp.incense;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private ArrayList<FavoriteItem> favoriteList;
    private OnDetailsClickListener onDetailsClickListener;
    private Context context;

    public interface OnDetailsClickListener {
        void onDetailsClick(FavoriteItem item);
    }

    public FavoriteAdapter(Context context, ArrayList<FavoriteItem> favoriteList, OnDetailsClickListener listener) {
        this.context = context;
        this.favoriteList = favoriteList;
        this.onDetailsClickListener = listener;
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
        // 顯示香的名稱
        holder.nameTextView.setText(item.getName());

        // 使用 Glide 加载图片
        if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(item.getImageUrl())
                    .placeholder(R.drawable.default_image)
                    .error(R.drawable.default_image)
                    .into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.default_image);
        }

        // 详细信息按钮事件
        holder.detailsButton.setOnClickListener(v -> {
            if (onDetailsClickListener != null) {
                onDetailsClickListener.onDetailsClick(item);
            }
        });

//        // 讓用戶名稱可點擊：點擊後跳轉到收藏該香的用戶 FavoritesActivity
//        holder.nameTextView.setOnClickListener(v -> {
//            if (item.getUserId() == null || item.getUserId().isEmpty()) {
//                Toast.makeText(context, "ユーザー情報が取得できません", Toast.LENGTH_SHORT).show();
//            } else {
//                Intent intent = new Intent(context, FavoritesActivity.class);
//                // 傳遞目標用戶ID（這裡用 "TARGET_USER_ID" 作為鍵）
//                intent.putExtra("TARGET_USER_ID", item.getUserId());
//                context.startActivity(intent);
//            }
//        });

//        // 整個項目點擊事件：跳轉到商品詳細頁面（例如 MinuteActivity）
//        holder.itemView.setOnClickListener(v -> {
//            Intent intent = new Intent(context, MinuteActivity.class);
//            intent.putExtra("EXTRA_TEXT", item.getDescription());
//            intent.putExtra("EXTRA_IMAGE_URL", item.getImageUrl());
//            intent.putExtra("EXTRA_URL", item.getUrl());
//            intent.putExtra("INCENSE_ID", item.getName());
//            intent.putExtra("INCENSE_NAME", item.getName());
//            context.startActivity(intent);
//        });
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        ImageView imageView;
        Button detailsButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.incense_name);
            imageView = itemView.findViewById(R.id.incense_image);
            detailsButton = itemView.findViewById(R.id.delete_button); // 修改为正确的ID
        }
    }
}