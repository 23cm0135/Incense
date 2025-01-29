package jec.ac.jp.incense;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class IncenseListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private IncenseAdapter adapter;
    private List<Incense> incenseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incense_list);

        // 获取传递的数据
        incenseList = getIntent().getParcelableArrayListExtra("incenseList");

        ArrayList<String> urlList = getIntent().getStringArrayListExtra("urlList");
        if (urlList != null) {
            for (String url : urlList) {
                Log.d("IncenseListActivity", "URL: " + url);
            }
        } else {
            Log.e("IncenseListActivity", "urlList is null");
        }

        // 检查数据是否为 null
        if (incenseList == null || incenseList.isEmpty()) {
            Log.e("IncenseListActivity", "No incense data received or list is empty.");
            Toast.makeText(this, "No incense data available. Showing an empty list.", Toast.LENGTH_SHORT).show();
            incenseList = new ArrayList<>();
        }

        // 初始化 RecyclerView
        recyclerView = findViewById(R.id.incense_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new IncenseAdapter(this, incenseList);
        recyclerView.setAdapter(adapter);
    }
}
