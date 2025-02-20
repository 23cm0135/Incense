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

public class MinuteActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "FavoritesPrefs";
    private static final String FAVORITES_KEY = "favorite_items";
    private static final String HISTORY_KEY = "browsing_history"; // 履歴キー

    // 靜態收藏列表 & 履歴列表
    public static ArrayList<FavoriteItem> favoriteItems = new ArrayList<>();
    public static ArrayList<FavoriteItem> browsingHistory = new ArrayList<>();
    // 用來快速檢查是否已收藏
    private static Set<String> favoriteNamesSet = new HashSet<>();

    private String url;
    private String incenseId, incenseName;
    private Button btnFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_minute);

        // 1) 加载收藏 & 履歴数据（共用）
        loadFavorites(this);
        loadBrowsingHistory(this);

        // 2) 获取前一个 Activity 传递的数据
        String text = getIntent().getStringExtra("EXTRA_TEXT");
        int imageResId = getIntent().getIntExtra("EXTRA_IMAGE", R.drawable.default_image);
        url = getIntent().getStringExtra("EXTRA_URL");

        if (url == null || url.isEmpty()) {
            url = "https://www.google.com";
        }

        incenseId = getIntent().getStringExtra("INCENSE_ID");
        incenseName = getIntent().getStringExtra("INCENSE_NAME");

        if (incenseId == null) incenseId = "unknown_id";
        if (incenseName == null) incenseName = "不明な香";

        // 3) 設置 UI
        TextView nameTextView = findViewById(R.id.incense_detail_name);
        TextView descriptionTextView = findViewById(R.id.incense_detail_description);
        ImageView imageView = findViewById(R.id.incense_detail_image);

        nameTextView.setText(incenseName);
        descriptionTextView.setText(text);

        // 這裡通常是本地圖片 → 用 `Glide` 載入也可，但 setImageResource 也可
        Glide.with(this).load(imageResId).into(imageView);

        // 4) 自動添加到履歴
        //   用本地圖片版本的 FavoriteItem 建構函式
        addNewBrowsingHistory(new FavoriteItem(incenseName, /* effect= */ "",
                        imageResId, text, url),
                this);

        // 5) 「購入」按钮
        Button btnPurchase = findViewById(R.id.btnPurchase);
        btnPurchase.setOnClickListener(v -> {
            Intent purchaseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(purchaseIntent);
        });
// 找到「投稿」按鈕
        Button btnSubmitImpression = findViewById(R.id.btnSubmitImpression);
        btnSubmitImpression.setOnClickListener(v -> {
            // 創建 Intent 跳轉到 UserImpression 活動
            Intent intent = new Intent(MinuteActivity.this, UserImpression.class);

            // 傳遞香的名稱與 ID，以便在投稿頁面顯示
            intent.putExtra("INCENSE_ID", incenseId);
            intent.putExtra("INCENSE_NAME", incenseName);

            // 啟動 UserImpression 活動
            startActivity(intent);
        });

        // 6) 「查看其他用户投稿」按钮
        Button btnViewPosts = findViewById(R.id.btnViewPosts);
        btnViewPosts.setOnClickListener(v -> {
            Intent intent = new Intent(MinuteActivity.this, UserImpressionListActivity.class);
            startActivity(intent);
        });

        // 7) 「お気に入り」按钮
        btnFavorite = findViewById(R.id.btnFavorite);
        if (favoriteNamesSet.contains(incenseName)) {
            // 已收藏 → 灰色 + 文字
            setButtonAsFavorited(btnFavorite);
        } else {
            // 點擊收藏 → 加入 favoriteItems
            btnFavorite.setOnClickListener(v -> addToFavorites(new FavoriteItem(
                    incenseName, "",
                    imageResId, text, url
            )));
        }
    }

    // **收藏**
    private void addToFavorites(FavoriteItem item) {
        if (favoriteNamesSet.contains(item.getName())) {
            Toast.makeText(this, item.getName() + " はすでにお気に入りです", Toast.LENGTH_SHORT).show();
            return;
        }

        // 新增到列表頂端
        favoriteItems.add(0, item);
        favoriteNamesSet.add(item.getName());
        saveFavorites(this);
        setButtonAsFavorited(btnFavorite);

        Toast.makeText(this, item.getName() + " をお気に入りに追加しました", Toast.LENGTH_SHORT).show();
    }

    // **添加到履歴**
    private void addNewBrowsingHistory(FavoriteItem newItem, Context context) {
        Log.d("BrowsingHistory", "addNewBrowsingHistory triggered for: " + newItem.getName());
        // 检查是否已经有该产品的历史记录
        for (int i = 0; i < browsingHistory.size(); i++) {
            FavoriteItem item = browsingHistory.get(i);
            if (item.getName().equals(newItem.getName())) {
                browsingHistory.remove(i); // 删除旧的记录
              //  notifyItemRemoved(i);
                break;
            }
        }

        // 将新的历史记录插入到最前面
        browsingHistory.add(0, newItem);
       // notifyItemInserted(0);
        Log.d("BrowsingHistory", "Browsing history after update: " + browsingHistory.toString());

        // 保存更新后的历史记录
        saveBrowsingHistory(context);
    }

    // **收藏按鈕狀態 → 灰色 & 文案**
    private void setButtonAsFavorited(Button button) {
        button.setEnabled(false);
        button.setText("お気に入り済み");
        button.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
    }

    // **保存收藏列表**
    public static void saveFavorites(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(favoriteItems);
        editor.putString(FAVORITES_KEY, json);
        editor.apply();

        // 同步 favoriteNamesSet
        favoriteNamesSet.clear();
        for (FavoriteItem fi : favoriteItems) {
            favoriteNamesSet.add(fi.getName());
        }
    }

    // **加载收藏列表**
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

        favoriteNamesSet.clear();
        for (FavoriteItem fi : favoriteItems) {
            favoriteNamesSet.add(fi.getName());
        }
    }

    // **保存履歴**
    private void saveBrowsingHistory(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        String json = gson.toJson(browsingHistory);
        editor.putString(HISTORY_KEY, json);
        editor.apply();
    }

    // **加载履歴**
    public static void loadBrowsingHistory(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(HISTORY_KEY, null);
        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<FavoriteItem>>() {}.getType();
            browsingHistory = gson.fromJson(json, type);
        } else {
            browsingHistory = new ArrayList<>();
        }
    }
}
