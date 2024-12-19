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

import java.util.List;

public class IncenseAdapter extends RecyclerView.Adapter<IncenseAdapter.IncenseViewHolder> {

    private Context context;
    private List<Incense> incenseList;

    // 构造函数，接收上下文和数据列表
    public IncenseAdapter(Context context, List<Incense> incenseList) {
        this.context = context;
        this.incenseList = incenseList;
    }

    @NonNull
    @Override
    public IncenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 加载布局文件
        View view = LayoutInflater.from(context).inflate(R.layout.item_incense, parent, false);
        return new IncenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IncenseViewHolder holder, int position) {
        // 获取当前的香数据
        Incense incense = incenseList.get(position);

        // 绑定数据到控件
        holder.nameTextView.setText(incense.getName());
        holder.effectTextView.setText(incense.getEffect());
        Glide.with(context).load(incense.getImageUrl()).into(holder.imageView);

        // 设置按钮点击事件，跳转到详细信息界面
        holder.detailButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, IncenseDetailActivity.class);
            intent.putExtra("name", incense.getName());
            intent.putExtra("imageUrl", incense.getImageUrl());
            intent.putExtra("description", incense.getDescription());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return incenseList.size();
    }

    // 内部 ViewHolder 类，用于缓存控件
    public static class IncenseViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, effectTextView;
        ImageView imageView;
        Button detailButton;

        public IncenseViewHolder(@NonNull View itemView) {
            super(itemView);

            // 初始化控件
            nameTextView = itemView.findViewById(R.id.incense_name);
            effectTextView = itemView.findViewById(R.id.incense_effect);
            imageView = itemView.findViewById(R.id.incense_image);
            detailButton = itemView.findViewById(R.id.detail_button); // 详细按钮
        }
    }
}
