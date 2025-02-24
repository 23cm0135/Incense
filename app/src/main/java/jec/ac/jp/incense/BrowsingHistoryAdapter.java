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

public class BrowsingHistoryAdapter extends RecyclerView.Adapter<BrowsingHistoryAdapter.ViewHolder> {

    private ArrayList<FavoriteItem> historyList;
    private OnDeleteClickListener onDeleteClickListener;
    private Context context;

    @FunctionalInterface
    public interface OnDeleteClickListener {
        void onDeleteClick(FavoriteItem item);
    }

    public BrowsingHistoryAdapter(ArrayList<FavoriteItem> historyList, OnDeleteClickListener listener) {
        this.historyList = historyList;
        this.onDeleteClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_browsing_history, parent, false);
        // 获取 context 供后续使用
        this.context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavoriteItem item = historyList.get(position);
        holder.nameTextView.setText(item.getName());
        holder.effectTextView.setText(item.getEffect());

        // 加载网络图片
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

        // 点击项目，跳转到详情页面（统一使用 MinuteActivity 显示网络图片）
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), MinuteActivity.class);
            intent.putExtra("EXTRA_TEXT", item.getDescription());
            intent.putExtra("EXTRA_IMAGE_URL", item.getImageUrl());
            intent.putExtra("EXTRA_URL", item.getUrl());
            intent.putExtra("INCENSE_ID", item.getName());
            intent.putExtra("INCENSE_NAME", item.getName());
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, effectTextView;
        ImageView imageView;
        Button deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView  = itemView.findViewById(R.id.incense_name);
            effectTextView = itemView.findViewById(R.id.incense_effect);
            imageView     = itemView.findViewById(R.id.incense_image);
            deleteButton  = itemView.findViewById(R.id.delete_button);
        }
    }
}
