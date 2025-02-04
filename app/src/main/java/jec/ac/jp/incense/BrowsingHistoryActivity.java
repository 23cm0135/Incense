package jec.ac.jp.incense;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class BrowsingHistoryActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "FavoritesPrefs";
    private static final String BROWSING_HISTORY_KEY = "browsing_history";

    private ArrayList<FavoriteItem> browsingHistory = new ArrayList<>();
    private BrowsingHistoryAdapter browsingHistoryAdapter;
    private RecyclerView browsingHistoryRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browsing_history);

        // 初始化 RecyclerView
        browsingHistoryRecyclerView = findViewById(R.id.browsing_history_recycler_view);
        browsingHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 加载浏览历史数据
        loadBrowsingHistory(this);

        // 绑定适配器
        browsingHistoryAdapter = new BrowsingHistoryAdapter(browsingHistory,this);
        browsingHistoryRecyclerView.setAdapter(browsingHistoryAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBrowsingHistory(this);
        updateHistoryUI();
    }

    private void updateHistoryUI() {
        if (browsingHistoryAdapter != null) {
            browsingHistoryAdapter.notifyDataSetChanged();
        }
    }

    private void loadBrowsingHistory(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(BROWSING_HISTORY_KEY, null);

        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<FavoriteItem>>() {}.getType();
            browsingHistory = gson.fromJson(json, type);
        } else {
            browsingHistory = new ArrayList<>();
        }
    }
}
