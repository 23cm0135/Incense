package jec.ac.jp.incense;

import android.os.Bundle;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_impression_list);
        // 启用 Edge-to-Edge 显示效果
        EdgeToEdge.enable(this);

        recyclerViewImpressions = findViewById(R.id.recyclerViewImpressions);
        postList = new ArrayList<>();

        // 設置 RecyclerView
        recyclerViewImpressions.setLayoutManager(new LinearLayoutManager(this));
        postAdapter = new PostAdapter(postList);
        recyclerViewImpressions.setAdapter(postAdapter);

        fetchPosts();
    }

    private void fetchPosts() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts")
                .orderBy("timestamp", Query.Direction.DESCENDING) // **🔥 按時間降序排列**
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        postList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String username = document.getString("username");
                            String content = document.getString("content");
                            String incenseName = document.getString("incenseName");
                            long timestamp = document.getLong("timestamp") != null ? document.getLong("timestamp") : 0L; // **🔥 讀取時間戳記**

                            postList.add(new Post(username, content, incenseName, timestamp));
                        }
                        postAdapter.notifyDataSetChanged(); // **🔥 更新 RecyclerView**
                    }
                });
    }
}
