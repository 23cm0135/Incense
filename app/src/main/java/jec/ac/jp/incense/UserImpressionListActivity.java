package jec.ac.jp.incense;

import android.os.Bundle;
import android.util.Log;

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

        // 获取香名
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
                            String content = document.getString("content");
                            String incenseName = document.getString("incenseName");
                            String postIncenseName = document.getString("incenseName"); // 使用不同的变量名
                            long timestamp = document.getLong("timestamp") != null ? document.getLong("timestamp") : 0L; // **🔥 讀取時間戳記**
                            // 打印日志查看数据
                            Log.d("UserImpressionListActivity", "incenseNameInPost: " + incenseName);
                            // 只添加与当前香名匹配的评论
                            if (postIncenseName != null && postIncenseName.trim().equals(this.incenseName.trim())) {
                                postList.add(new Post(username, content, postIncenseName, timestamp));
                            }
                        }
                        postAdapter.notifyDataSetChanged();
                    }
                });
    }
}
