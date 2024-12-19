package jec.ac.jp.incense;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Favorite extends AppCompatActivity {

    private RecyclerView favoritesRecyclerView;
    private FavoritesAdapter favoritesAdapter;
    private List<FavoriteItem> favoriteItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favorite);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 获取 RecyclerView 实例
        favoritesRecyclerView = findViewById(R.id.favoritesRecyclerView);

        // 创建和初始化收藏项数据列表
        favoriteItems = new ArrayList<>();
        favoriteItems.add(new FavoriteItem("收藏项 1", R.drawable.user));
        favoriteItems.add(new FavoriteItem("收藏项 2", R.drawable.user));

        // 初始化适配器并设置给 RecyclerView
        favoritesAdapter = new FavoritesAdapter(this, favoriteItems);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        favoritesRecyclerView.setAdapter(favoritesAdapter);
    }
}