package jec.ac.jp.incense;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

        // 加载图片
        Glide.with(context).load(item.getImageUrl()).into(holder.imageView);

        // 删除按钮点击事件
        holder.deleteButton.setOnClickListener(v -> {
            browsingHistory.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, browsingHistory.size());

            saveBrowsingHistory(context);
        });

        // 单元点击事件（如跳转详情页）
        holder.itemLayout.setOnClickListener(v -> {
            // 跳转到商品详情页面
            Intent intent = new Intent(context, IncenseDetailActivity.class);
            intent.putExtra("name", item.getName());
            intent.putExtra("imageUrl", item.getImageUrl());
            intent.putExtra("description", item.getDescription());
            intent.putExtra("effect", item.getEffect());
            context.startActivity(intent);
        });
    }

    private void saveBrowsingHistory(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("FavoritesPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(browsingHistory); // 将当前浏览历史转换为JSON字符串

        editor.putString("browsing_history", json); // 保存到SharedPreferences
        editor.apply();
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
