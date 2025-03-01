package jec.ac.jp.incense;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class BrowsingHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvEmptyMessage;
    private BrowsingHistoryAdapter adapter;
    private ArrayList<FavoriteItem> historyList = new ArrayList<>();

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browsing_history);
        EdgeToEdge.enable(this);

        tvEmptyMessage = findViewById(R.id.tvEmptyMessage);
        recyclerView = findViewById(R.id.browsing_history_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        adapter = new BrowsingHistoryAdapter(historyList, item -> removeHistory(item));
        recyclerView.setAdapter(adapter);

        loadBrowsingHistory();
    }

    private void loadBrowsingHistory() {
        if (currentUser == null) {
            Toast.makeText(this, "ログインしてください", Toast.LENGTH_SHORT).show();
            return;
        }

        db.collection("users")
                .document(currentUser.getUid())
                .collection("history")
                .orderBy("timestamp", Query.Direction.DESCENDING) // 添加这一行，按时间戳升序排序
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    historyList.clear();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        FavoriteItem item = doc.toObject(FavoriteItem.class);
                        if (item != null) {
                            historyList.add(item); // 直接添加到列表末尾，保持时间顺序
                        }
                    }
                    adapter.notifyDataSetChanged();

                    if (historyList.isEmpty()) {
                        tvEmptyMessage.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        tvEmptyMessage.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "履歴の読み込みに失敗しました", Toast.LENGTH_SHORT).show()
                );
    }

    private void removeHistory(FavoriteItem item) {
        if (currentUser == null) return;

        db.collection("users")
                .document(currentUser.getUid())
                .collection("history")
                .document(item.getName())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    historyList.remove(item);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "削除しました：" + item.getName(), Toast.LENGTH_SHORT).show();
                    if (historyList.isEmpty()) {
                        tvEmptyMessage.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "削除に失敗しました", Toast.LENGTH_SHORT).show()
                );
    }
}
