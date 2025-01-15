package jec.ac.jp.incense;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private final List<FavoriteItem> favoriteItems;
    private final OnFavoriteItemRemoveListener removeListener;

    /**
     * 接口定义用于处理项删除操作的回调
     */
    public interface OnFavoriteItemRemoveListener {
        void onFavoriteItemRemove(int position);
    }

    /**
     * 构造函数
     */
    public FavoriteAdapter(List<FavoriteItem> favoriteItems, OnFavoriteItemRemoveListener removeListener) {
        this.favoriteItems = favoriteItems;
        this.removeListener = removeListener;
    }

    /**
     * 创建 ViewHolder
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent, false);
        return new ViewHolder(view);
    }

    /**
     * 绑定 ViewHolder
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(favoriteItems.get(position), position, removeListener);
    }

    /**
     * 返回项目数
     */
    @Override
    public int getItemCount() {
        return favoriteItems.size();
    }

    /**
     * 内部 ViewHolder 类
     */
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

        /**
         * 绑定数据到视图
         */
        public void bind(FavoriteItem item, int position, OnFavoriteItemRemoveListener removeListener) {
            // 设置文本
            incenseName.setText(item.getName());
            incenseEffect.setText(item.getEffect());

            // 设置删除按钮点击事件
            deleteButton.setOnClickListener(v -> removeListener.onFavoriteItemRemove(position));
        }
    }
}
