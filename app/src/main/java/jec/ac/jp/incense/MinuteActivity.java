package jec.ac.jp.incense;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MinuteActivity extends AppCompatActivity {

    private TextView textView;
    private String url;
    private String incenseId, incenseName;
    private Button btnFavorite;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "FavoritesPrefs";
    private static final String FAVORITES_KEY = "favorite_items";
    private ArrayList<FavoriteItem> favoriteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_minute);

        String text = getIntent().getStringExtra("EXTRA_TEXT");
        int imageResId = getIntent().getIntExtra("EXTRA_IMAGE", R.drawable.default_image);

        url = getIntent().getStringExtra("EXTRA_URL");
        if (url == null || url.isEmpty()) {
            url = "https://www.google.com";
        }

        // 取得 incenseId & incenseName
        incenseId = getIntent().getStringExtra("INCENSE_ID");
        incenseName = getIntent().getStringExtra("INCENSE_NAME");

        if (incenseId == null) incenseId = "unknown_id";
        if (incenseName == null) incenseName = "不明な香";

        // 设置文本与图片
        textView = findViewById(R.id.textView);
        textView.setText(text);

        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageResource(imageResId);

        // 「投稿」按钮
        Button btnSubmitImpression = findViewById(R.id.btnSubmitImpression);
        btnSubmitImpression.setOnClickListener(v -> {
            Intent intent = new Intent(MinuteActivity.this, UserImpression.class);
            intent.putExtra("INCENSE_ID", incenseId);
            intent.putExtra("INCENSE_NAME", incenseName);
            startActivityForResult(intent, 1);
        });

        // 「购买」按钮
        Button btnPurchase = findViewById(R.id.btnPurchase);
        btnPurchase.setOnClickListener(v -> {
            Intent purchaseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(purchaseIntent);
        });

        // 「查看其他用户投稿」按钮
        Button btnViewPosts = findViewById(R.id.btnViewPosts);
        btnViewPosts.setOnClickListener(v -> {
            Intent intent = new Intent(MinuteActivity.this, UserImpressionListActivity.class);
            startActivity(intent);
        });

        // 「お気に入り」按钮
        btnFavorite = findViewById(R.id.btnFavorite);
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        favoriteList = loadFavorites();

        // **检查是否已收藏**
        if (isFavorited(incenseName)) {
            setButtonAsFavorited(btnFavorite);
        } else {
            btnFavorite.setOnClickListener(v -> addToFavorites(new FavoriteItem(incenseName, "", "", "", url)));
        }
    }

    // **添加到收藏**
    private void addToFavorites(FavoriteItem item) {
        if (isFavorited(item.getName())) {
            Toast.makeText(this, item.getName() + " はすでにお気に入りに登録されています", Toast.LENGTH_SHORT).show();
            return;
        }

        favoriteList.add(item);
        saveFavorites(favoriteList);

        // **更新按钮状态**
        setButtonAsFavorited(btnFavorite);

        Toast.makeText(this, item.getName() + " をお気に入りに追加しました", Toast.LENGTH_SHORT).show();
    }

    // **检查是否已收藏**
    private boolean isFavorited(String name) {
        for (FavoriteItem f : favoriteList) {
            if (f.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    // **设置按钮为「お気に入り済み」**
    private void setButtonAsFavorited(Button button) {
        button.setEnabled(false);
        button.setText("お気に入り済み");
        button.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
    }

    // **保存お気に入り**
    private void saveFavorites(ArrayList<FavoriteItem> list) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(FAVORITES_KEY, json);
        editor.apply();
    }

    // **加载お気に入り**
    private ArrayList<FavoriteItem> loadFavorites() {
        String json = sharedPreferences.getString(FAVORITES_KEY, null);
        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<FavoriteItem>>() {}.getType();
            return gson.fromJson(json, type);
        }
        return new ArrayList<>();
    }
}
