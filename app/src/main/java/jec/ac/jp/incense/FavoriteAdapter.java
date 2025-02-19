package jec.ac.jp.incense;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private final ArrayList<FavoriteItem> favoriteItems;
    private final OnDeleteClickListener onDeleteClickListener;

    public FavoriteAdapter(ArrayList<FavoriteItem> favoriteItems, OnDeleteClickListener onDeleteClickListener) {
        this.favoriteItems = favoriteItems;
        this.onDeleteClickListener = onDeleteClickListener;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorite, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        FavoriteItem item = favoriteItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return favoriteItems.size();
    }

    class FavoriteViewHolder extends RecyclerView.ViewHolder {
        private final TextView incenseName;
        private final TextView incenseEffect;
        private final ImageView incenseImage;
        private final Button deleteButton;
        private final LinearLayout itemLayout;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            incenseName = itemView.findViewById(R.id.incense_name);
            incenseEffect = itemView.findViewById(R.id.incense_effect);
            incenseImage = itemView.findViewById(R.id.incense_image);
            deleteButton = itemView.findViewById(R.id.delete_button);
            itemLayout = itemView.findViewById(R.id.item_layout);
        }

        public void bind(FavoriteItem item) {
            incenseName.setText(item.getName());
            incenseEffect.setText(item.getEffect());

            // **處理圖片顯示**
            if (item.getImageResId() != 0) {
                // 如果是本地圖片
                incenseImage.setImageResource(item.getImageResId());
            } else if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
                // 如果是網絡圖片
                Glide.with(itemView.getContext()).load(item.getImageUrl()).into(incenseImage);
            } else {
                // 預設圖片
                incenseImage.setImageResource(R.drawable.default_image);
            }

            // **删除按钮点击事件**
            deleteButton.setOnClickListener(v -> onDeleteClickListener.onDeleteClick(item));

            // **點擊整個項目時，跳轉到 `IncenseDetailActivity`**
            itemLayout.setOnClickListener(v -> {
                if (item.getImageResId() != 0) {
                    // **本地图片** -> 跳转到 `MinuteActivity`
                    Intent intent = new Intent(itemView.getContext(), MinuteActivity.class);

                    intent.putExtra("EXTRA_TEXT", item.getDescription());
                    intent.putExtra("EXTRA_IMAGE", item.getImageResId());
                    intent.putExtra("EXTRA_URL", item.getUrl());
                    intent.putExtra("INCENSE_ID", item.getName());
                    intent.putExtra("INCENSE_NAME", item.getName());

                    itemView.getContext().startActivity(intent);

                } else {
                    // **网络图片** -> 跳转到 `IncenseDetailActivity`
                    Intent intent = new Intent(itemView.getContext(), IncenseDetailActivity.class);

                    intent.putExtra("name", item.getName());
                    intent.putExtra("effect", item.getEffect());
                    intent.putExtra("imageUrl", item.getImageUrl());
                    intent.putExtra("description", item.getDescription());
                    intent.putExtra("url", item.getUrl());

                    itemView.getContext().startActivity(intent);
                }
            });


        }
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(FavoriteItem item);
    }
}
