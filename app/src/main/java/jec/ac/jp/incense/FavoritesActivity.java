package jec.ac.jp.incense;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity {

    // 声明适配器
    private FavoriteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        // 加载收藏数据
        IncenseDetailActivity.loadFavorites(this);

        // 初始化 RecyclerView
        RecyclerView recyclerView = findViewById(R.id.favorites_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 初始化适配器并设置回调
        adapter = new FavoriteAdapter(IncenseDetailActivity.favoriteItems, this::onDeleteItem);
        recyclerView.setAdapter(adapter);
    }

    /**
     * 删除商品的回调方法
     *
     * @param item 要删除的收藏商品
     */
    private void onDeleteItem(FavoriteItem item) {
        // 从收藏列表中移除商品
        IncenseDetailActivity.favoriteItems.remove(item);
        // 更新本地存储
        IncenseDetailActivity.saveFavorites(this);
        // 通知适配器数据已更改
        adapter.notifyDataSetChanged();
        // 提示用户已删除
        Toast.makeText(this, "已删除: " + item.getName(), Toast.LENGTH_SHORT).show();
    }
}
