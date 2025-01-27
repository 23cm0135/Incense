package jec.ac.jp.incense;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

        // 初始化数据列表
        postList = new ArrayList<>();

        // 设置RecyclerView
        recyclerViewImpressions.setLayoutManager(new LinearLayoutManager(this));
        postAdapter = new PostAdapter(postList);
        recyclerViewImpressions.setAdapter(postAdapter);

        fetchPosts();
    }

    private void fetchPosts() {
        // 添加一些示例数据
        postList.add(new Post("用户A", "这是第一条投稿"));
        postList.add(new Post("用户B", "这是第二条投稿"));

        // 通知适配器数据已更新
        postAdapter.notifyDataSetChanged();
    }
}
