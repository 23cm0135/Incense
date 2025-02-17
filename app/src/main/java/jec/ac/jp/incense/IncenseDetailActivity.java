package jec.ac.jp.incense;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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
    private BrowsingHistoryAdapter browsingHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incense_detail);
        // 启用 Edge-to-Edge 显示效果
        EdgeToEdge.enable(this);

        // 加载收藏数据
        loadFavorites(this);

        IncenseDetailActivity.loadBrowsingHistory(this);

        // 获取历史记录列表
        ArrayList<FavoriteItem> historyItems = IncenseDetailActivity.browsingHistory;


        // 获取传递的数据
        String name = getIntent().getStringExtra("name");
        String imageUrl = getIntent().getStringExtra("imageUrl");
        String description = getIntent().getStringExtra("description");
        String effect = getIntent().getStringExtra("effect");
        String url = getIntent().getStringExtra("url");

        Log.d("url", "Received url: " + url);

        // 初始化视图
        TextView nameTextView = findViewById(R.id.incense_detail_name);
        ImageView imageView = findViewById(R.id.incense_detail_image);
        TextView descriptionTextView = findViewById(R.id.incense_detail_description);
        Button favoriteButton = findViewById(R.id.favorite_button);
        Button openUrlButton = findViewById(R.id.open_url_button);

        // 设置数据到视图
        nameTextView.setText(name);
        descriptionTextView.setText(description);
        Glide.with(this).load(imageUrl).into(imageView);

        // 设置"购买"按钮点击事件，打开浏览器
        openUrlButton.setOnClickListener(v -> {
            if (url == null || url.isEmpty()) {
                Toast.makeText(this, "無効なURLです", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        });

        // 添加到浏览历史
        addToBrowsingHistory(new FavoriteItem(name, effect, imageUrl, description,url), this);

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
            FavoriteItem newItem = new FavoriteItem(name, effect, imageUrl, description,url);
            favoriteItems.add(newItem);
            favoriteNamesSet.add(name);
            saveFavorites(this);

            // 动态更新按钮状态
            setButtonAsFavorited(favoriteButton);

            Toast.makeText(this, "お気に入りに追加しました: " + name, Toast.LENGTH_SHORT).show();
        });
        // 1) 找到「投稿します」按鈕
        Button userImpressionButton = findViewById(R.id.UserImpression);

        // 2) 點擊時，跳到 UserImpression 這個 Activity
        userImpressionButton.setOnClickListener(v -> {
            // 建立 Intent
            Intent intent = new Intent(IncenseDetailActivity.this, UserImpression.class);

            // 例如把「香の名前」傳給投稿頁面
            // (UserImpression 中可用 getIntent().getStringExtra("INCENSE_NAME") 取得)
            intent.putExtra("INCENSE_NAME", name);

            startActivity(intent);
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
           // browsingHistory = new ArrayList<>(); // 初始化为空列表
        }
    }
    private void updateHistoryUI() {
        // 如果你使用的是 RecyclerView，则通知适配器数据更新
        if (browsingHistoryAdapter != null) {
            browsingHistoryAdapter.notifyDataSetChanged();
        }
    }

}
