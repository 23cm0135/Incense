package jec.ac.jp.incense;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
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

                            long timestamp = 0L;
                            Object timestampObj = document.get("timestamp");

                            if (timestampObj instanceof com.google.firebase.Timestamp) {
                                timestamp = ((com.google.firebase.Timestamp) timestampObj).toDate().getTime();
                            } else if (timestampObj instanceof Long) {
                                timestamp = (Long) timestampObj;
                            }

                            Log.d("UserImpressionListActivity", "incenseNameInPost: " + incenseName);

                            if (incenseName != null && incenseName.trim().equals(this.incenseName.trim())) {
                                postList.add(new Post(username, content, incenseName, timestamp));
                            }
                        }
                        postAdapter.notifyDataSetChanged();

                        // 检查评论列表是否为空
                        TextView noImpressionsTextView = findViewById(R.id.noImpressionsTextView);
                        if (postList.isEmpty()) {
                            noImpressionsTextView.setVisibility(View.VISIBLE);
                            recyclerViewImpressions.setVisibility(View.GONE);
                        } else {
                            noImpressionsTextView.setVisibility(View.GONE);
                            recyclerViewImpressions.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

}
