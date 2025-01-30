package jec.ac.jp.incense;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
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

        recyclerViewImpressions = findViewById(R.id.recyclerViewImpressions);

        // 初始化數據列表
        postList = new ArrayList<>();

        // 設置 RecyclerView
        recyclerViewImpressions.setLayoutManager(new LinearLayoutManager(this));
        postAdapter = new PostAdapter(postList);
        recyclerViewImpressions.setAdapter(postAdapter);

        fetchPosts();
    }

    private void fetchPosts() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                postList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String username = document.getString("username");
                    String content = document.getString("content");
                    String incenseName = document.getString("incenseName"); // 獲取香的名稱
                    postList.add(new Post(username, content, incenseName));
                }
                postAdapter.notifyDataSetChanged();
            }
        });
    }
}
