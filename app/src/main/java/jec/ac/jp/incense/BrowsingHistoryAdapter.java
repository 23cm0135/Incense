package jec.ac.jp.incense;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;

public class BrowsingHistoryAdapter extends RecyclerView.Adapter<BrowsingHistoryAdapter.ViewHolder> {

    private final ArrayList<FavoriteItem> browsingHistory;
    private final Context context;

    public BrowsingHistoryAdapter(ArrayList<FavoriteItem> browsingHistory, Context context) {
        this.browsingHistory = browsingHistory;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_browsing_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavoriteItem item = browsingHistory.get(position);

        // 设置名称和效果
        holder.nameTextView.setText(item.getName());
        holder.effectTextView.setText(item.getEffect());

        // **区分本地图片和网络图片**
        if (item.getImageResId() != 0) {
            // 如果是本地图片
            holder.imageView.setImageResource(item.getImageResId());
        } else if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
            // 如果是网络图片
            Glide.with(context).load(item.getImageUrl()).into(holder.imageView);
        } else {
            // 都没有 → 使用默认图
            holder.imageView.setImageResource(R.drawable.default_image);
        }

        // 删除按钮点击事件
        holder.deleteButton.setOnClickListener(v -> {
            browsingHistory.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, browsingHistory.size());

            saveBrowsingHistory(context);
        });

        // 单元点击事件（如跳转详情页）
        holder.itemLayout.setOnClickListener(v -> {
            // 如果是本地香 → 跳转到 MinuteActivity
            if (item.getImageResId() != 0) {
                // 添加浏览历史（本地香）
                addNewBrowsingHistory(item, context);

                Intent intent = new Intent(context, MinuteActivity.class);
                intent.putExtra("EXTRA_TEXT", item.getDescription());
                intent.putExtra("EXTRA_IMAGE", item.getImageResId());
                intent.putExtra("EXTRA_URL", item.getUrl());
                intent.putExtra("INCENSE_ID", item.getName());
                intent.putExtra("INCENSE_NAME", item.getName());
                context.startActivity(intent);

            } else {
                // 如果是网络香 → 跳转到 IncenseDetailActivity
                Intent intent = new Intent(context, IncenseDetailActivity.class);
                intent.putExtra("name", item.getName());
                intent.putExtra("imageUrl", item.getImageUrl());
                intent.putExtra("description", item.getDescription());
                intent.putExtra("effect", item.getEffect());
                intent.putExtra("url", item.getUrl());
                context.startActivity(intent);
            }
        });
    }
    private void saveBrowsingHistory(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("FavoritesPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(browsingHistory); // 将当前浏览历史转换为JSON字符串

        // 打印保存的浏览历史 JSON 数据
        Log.d("BrowsingHistory", "Saving browsing history: " + json);

        editor.putString("browsing_history", json); // 保存到SharedPreferences
        editor.apply();
    }
    // 添加新的浏览记录，并确保同一产品不会重复
    private void addNewBrowsingHistory(FavoriteItem newItem, Context context) {
        Log.d("BrowsingHistory", "addNewBrowsingHistory triggered for: " + newItem.getName());
        // 检查是否已经有该产品的历史记录
        for (int i = 0; i < browsingHistory.size(); i++) {
            FavoriteItem item = browsingHistory.get(i);
            if (item.getName().equals(newItem.getName())) {
                browsingHistory.remove(i); // 删除旧的记录
                notifyItemRemoved(i);
                break;
            }
        }

        // 将新的历史记录插入到最前面
        browsingHistory.add(0, newItem);
        //notifyItemInserted(0);
        notifyDataSetChanged();// 新记录显示在最上面
        Log.d("BrowsingHistory", "Browsing history after update: " + browsingHistory.toString());

        // 保存更新后的历史记录
        saveBrowsingHistory(context);
    }


    @Override
    public int getItemCount() {
        return browsingHistory.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView nameTextView;
        private final TextView effectTextView;
        private final ImageView imageView;
        private final Button deleteButton;
        private final View itemLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.incense_name);
            effectTextView = itemView.findViewById(R.id.incense_effect);
            imageView = itemView.findViewById(R.id.incense_image);
            deleteButton = itemView.findViewById(R.id.delete_button);
            itemLayout = itemView.findViewById(R.id.item_layout);
        }
    }
}
