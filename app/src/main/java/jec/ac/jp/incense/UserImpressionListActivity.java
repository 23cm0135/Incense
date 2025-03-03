package jec.ac.jp.incense;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserImpressionListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewImpressions;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private String incenseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_impression_list);
        EdgeToEdge.enable(this);

        // 從 Intent 中取得香名
        incenseName = getIntent().getStringExtra("INCENSE_NAME");
        Log.d("UserImpressionListActivity", "Received incense name: " + incenseName);

        recyclerViewImpressions = findViewById(R.id.recyclerViewImpressions);
        postList = new ArrayList<>();

        recyclerViewImpressions.setLayoutManager(new LinearLayoutManager(this));
        postAdapter = new PostAdapter(postList);
        recyclerViewImpressions.setAdapter(postAdapter);

        fetchPosts();
    }

    private void fetchPosts() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        postList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String username = document.getString("username");
                            // 從 Firestore 中讀取 userId 欄位
                            String userId = document.getString("userId");
                            String content = document.getString("content");
                            String postIncenseName = document.getString("incenseName");

                            long timestamp = 0L;
                            Object timestampObj = document.get("timestamp");
                            if (timestampObj instanceof com.google.firebase.Timestamp) {
                                timestamp = ((com.google.firebase.Timestamp) timestampObj).toDate().getTime();
                            } else if (timestampObj instanceof Long) {
                                timestamp = (Long) timestampObj;
                            }

                            Log.d("UserImpressionListActivity", "incenseNameInPost: " + postIncenseName);

                            // 只加入香名符合當前頁面顯示的資料
                            if (postIncenseName != null && postIncenseName.trim().equals(this.incenseName.trim())) {
                                // 注意參數順序必須與 Post 類別的建構子相符：
                                // (username, content, incenseName, timestamp, userId)
                                postList.add(new Post(username, content, postIncenseName, timestamp, userId));
                            }
                        }
                        postAdapter.notifyDataSetChanged();

                        // 檢查評論列表是否為空
                        TextView noImpressionsTextView = findViewById(R.id.noImpressionsTextView);
                        if (postList.isEmpty()) {
                            noImpressionsTextView.setVisibility(View.VISIBLE);
                            recyclerViewImpressions.setVisibility(View.GONE);
                        } else {
                            noImpressionsTextView.setVisibility(View.GONE);
                            recyclerViewImpressions.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Log.e("UserImpressionListActivity", "Error fetching posts", task.getException());
                    }
                });
    }
}
