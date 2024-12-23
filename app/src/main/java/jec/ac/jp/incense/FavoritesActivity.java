package jec.ac.jp.incense;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    private List<FavoriteItem> favoriteItems = new ArrayList<>();
    private FavoriteAdapter favoriteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        // 初始化数据
        loadFavoriteItems();

    }

    // 加载收藏数据
    private void loadFavoriteItems() {
        // 示例数据，可从实际数据源加载
        favoriteItems.add(new FavoriteItem("香名1", "效果1", "https://example.com/image1.png"));
        favoriteItems.add(new FavoriteItem("香名2", "效果2", "https://example.com/image2.png"));
    }

    // 删除收藏项
    private void removeFavoriteItem(FavoriteItem item) {
        favoriteItems.remove(item);
        favoriteAdapter.notifyDataSetChanged();
        Toast.makeText(this, item.getName() + "を削除しました", Toast.LENGTH_SHORT).show();
    }
}
