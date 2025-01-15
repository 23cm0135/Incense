package jec.ac.jp.incense;

import android.content.Context;
import android.content.Intent;
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

public class IncenseDetailActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "FavoritesPrefs";
    private static final String FAVORITES_KEY = "favorite_items";

    public static ArrayList<FavoriteItem> favoriteItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incense_detail);

//        // 强制清理旧数据
//        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
//        prefs.edit().remove(FAVORITES_KEY).apply(); // 删除旧数据

        // 加载收藏数据
        loadFavorites(this);

        // 获取传递的数据
        String name = getIntent().getStringExtra("name");
        String imageUrl = getIntent().getStringExtra("imageUrl");
        String description = getIntent().getStringExtra("description");
        String effect = getIntent().getStringExtra("effect");

        // 初始化视图
        TextView nameTextView = findViewById(R.id.incense_detail_name);
        ImageView imageView = findViewById(R.id.incense_detail_image);
        TextView descriptionTextView = findViewById(R.id.incense_detail_description);
        Button favoriteButton = findViewById(R.id.favorite_button);

        nameTextView.setText(name);
        Glide.with(this).load(imageUrl).into(imageView);
        descriptionTextView.setText(description);

        // 收藏按钮点击事件
        favoriteButton.setOnClickListener(v -> {
            for (FavoriteItem item : favoriteItems) {
                if (item.getName().equals(name)) {
                    Toast.makeText(this, "商品已在收藏列表中", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            // 添加到收藏列表
            favoriteItems.add(new FavoriteItem(name, effect, imageUrl));
            saveFavorites(this);
            Toast.makeText(this, "已收藏: " + name, Toast.LENGTH_SHORT).show();
        });

    }

    public static void saveFavorites(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // 将收藏列表转换为 JSON
        Gson gson = new Gson();
        String json = gson.toJson(favoriteItems);

        // 存储为 String
        editor.putString(FAVORITES_KEY, json);
        editor.apply();
    }


    public static void loadFavorites(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        if (prefs.contains(FAVORITES_KEY)) {
            try {
                String json = prefs.getString(FAVORITES_KEY, null);
                Log.d("FavoritesDebug", "Loaded JSON: " + json);
                if (json != null) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ArrayList<FavoriteItem>>() {}.getType();
                    favoriteItems = gson.fromJson(json, type);
                } else {
                    favoriteItems = new ArrayList<>();
                }
            } catch (ClassCastException e) {
                Log.e("FavoritesDebug", "ClassCastException: Old data found. Removing it.");
                prefs.edit().remove(FAVORITES_KEY).apply();
                favoriteItems = new ArrayList<>();
            }
        } else {
            Log.d("FavoritesDebug", "No favorites found. Initializing empty list.");
            favoriteItems = new ArrayList<>();
        }
    }


}
