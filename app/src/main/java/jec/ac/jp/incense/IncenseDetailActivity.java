package jec.ac.jp.incense;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class IncenseDetailActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "FavoritesPrefs";
    private static final String FAVORITES_KEY = "favorite_items";
    private static final String BROWSING_HISTORY_KEY = "browsing_history";

    public static ArrayList<FavoriteItem> favoriteItems = new ArrayList<>();
    public static ArrayList<FavoriteItem> browsingHistory = new ArrayList<>();
    private static Set<String> favoriteNamesSet = new HashSet<>(); // 用于快速检查是否收藏

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incense_detail);

        // 加载收藏数据
        loadFavorites(this);

        // 加载浏览历史数据
        loadBrowsingHistory(this);

        // 初始化快速检查集合
        for (FavoriteItem item : favoriteItems) {
            favoriteNamesSet.add(item.getName());
        }

        // 获取传递的数据
        String name = getIntent().getStringExtra("name");
        String imageUrl = getIntent().getStringExtra("imageUrl");
        String description = getIntent().getStringExtra("description");
        String effect = getIntent().getStringExtra("effect");

        Log.d("IntentDebug", "Received Name: " + name);

        // 初始化视图
        TextView nameTextView = findViewById(R.id.incense_detail_name);
        ImageView imageView = findViewById(R.id.incense_detail_image);
        TextView descriptionTextView = findViewById(R.id.incense_detail_description);
        Button favoriteButton = findViewById(R.id.favorite_button);

        // 设置数据到视图
        nameTextView.setText(name);
        descriptionTextView.setText(description);
        Glide.with(this).load(imageUrl).into(imageView);

        // 添加到浏览历史
        addToBrowsingHistory(new FavoriteItem(name, effect, imageUrl, description), this);

        // 初始化收藏按钮状态
        if (favoriteNamesSet.contains(name)) {
            setButtonAsFavorited(favoriteButton);
        }

        // 收藏按钮点击事件
        favoriteButton.setOnClickListener(v -> {
            if (favoriteNamesSet.contains(name)) {
                Toast.makeText(this, "商品はすでにお気に入りに追加されています", Toast.LENGTH_SHORT).show();
                return;
            }

            // 添加到收藏列表
            FavoriteItem newItem = new FavoriteItem(name, effect, imageUrl, description);
            favoriteItems.add(newItem);
            favoriteNamesSet.add(name);
            saveFavorites(this);

            // 动态更新按钮状态
            setButtonAsFavorited(favoriteButton);

            Toast.makeText(this, "お気に入りに追加しました: " + name, Toast.LENGTH_SHORT).show();
        });
    }

    private void setButtonAsFavorited(Button button) {
        button.setEnabled(false);
        button.setText("お気に入り済み");
        button.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
    }

    private void addToBrowsingHistory(FavoriteItem item, Context context) {
        for (FavoriteItem historyItem : browsingHistory) {
            if (historyItem.getName().equals(item.getName())) {
                return; // 如果已经存在，则不添加
            }
        }

        browsingHistory.add(0, item); // 将最新浏览记录添加到顶部
        saveBrowsingHistory(context);
    }

    public static void saveFavorites(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(favoriteItems);

        editor.putString(FAVORITES_KEY, json);
        editor.apply();
    }

    public static void loadFavorites(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        String json = prefs.getString(FAVORITES_KEY, null);
        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<FavoriteItem>>() {}.getType();
            favoriteItems = gson.fromJson(json, type);
        } else {
            favoriteItems = new ArrayList<>();
        }
    }

    private void saveBrowsingHistory(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(browsingHistory);

        editor.putString(BROWSING_HISTORY_KEY, json);
        editor.apply();
    }
    /*
    加载浏览历史
     */
    public static void loadBrowsingHistory(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(BROWSING_HISTORY_KEY, null);

        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<FavoriteItem>>() {}.getType();
            browsingHistory = gson.fromJson(json, type);
        } else {
            browsingHistory = new ArrayList<>(); // 初始化为空列表
        }
    }
}
