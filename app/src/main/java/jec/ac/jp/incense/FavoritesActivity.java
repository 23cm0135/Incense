package jec.ac.jp.incense;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity implements FavoriteAdapter.OnFavoriteItemRemoveListener {

    private RecyclerView recyclerView;
    private FavoriteAdapter favoriteAdapter;
    private List<FavoriteItem> favoriteItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        recyclerView = findViewById(R.id.favorites_list);

        // 初始化收藏的商品数据
        favoriteItems = new ArrayList<>();

        // 假设我们将这些商品作为收藏的初始数据
        favoriteItems.add(new FavoriteItem("香名 1", "效果 1", "https://example.com/image1.jpg"));
        favoriteItems.add(new FavoriteItem("香名 2", "效果 2", "https://example.com/image2.jpg"));
        favoriteItems.add(new FavoriteItem("香名 3", "效果 3", "https://example.com/image3.jpg"));

        // 初始化适配器并设置
        favoriteAdapter = new FavoriteAdapter(favoriteItems, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(favoriteAdapter);
    }

    @Override
    public void onFavoriteItemRemove(int position) {
        // 从列表中删除该项
        favoriteItems.remove(position);

        // 通知适配器更新列表
        favoriteAdapter.notifyItemRemoved(position);

        // 给用户反馈
        Toast.makeText(this, "已删除收藏", Toast.LENGTH_SHORT).show();
    }
}
