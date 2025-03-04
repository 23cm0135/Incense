package jec.ac.jp.incense;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvEmptyMessage;
    private TextView favoritesTitleTextView; // 添加 TextView 变量
    private ArrayList<FavoriteItem> favoriteItems = new ArrayList<>();
    private FavoriteAdapter adapter;
    private FirebaseFirestore db;
    private String userId; // 用戶 UID（用於加載收藏）

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        EdgeToEdge.enable(this);

        tvEmptyMessage = findViewById(R.id.tvEmptyMessage);
        recyclerView = findViewById(R.id.favorites_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        favoritesTitleTextView = findViewById(R.id.favorites_title); // 初始化 TextView

        db = FirebaseFirestore.getInstance();

        // 優先從 Intent 中取得 USER_ID（例如：當從其他用戶的帖子點擊後傳入），若未提供則使用當前用戶 UID
        userId = getIntent().getStringExtra("USER_ID");
        String displayName = getIntent().getStringExtra("DISPLAY_NAME");
        if (userId == null || userId.isEmpty()) {
            if (com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser() != null) {
                userId = com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser().getUid();
            } else {
                Toast.makeText(this, "ログインしてください", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }

        // 设置标题
        setActivityTitle(userId, displayName); // 修改为传递 displayName
        adapter = new FavoriteAdapter(this, favoriteItems, item -> {
            showDetails(item);
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFavoritesFromFirestore(); // 在 onResume 中加载数据
    }

    private void loadFavoritesFromFirestore() {
        db.collection("users")
                .document(userId)
                .collection("favorites")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    favoriteItems.clear();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        FavoriteItem item = doc.toObject(FavoriteItem.class);
                        favoriteItems.add(item);
                    }
                    if (favoriteItems.isEmpty()) {
                        tvEmptyMessage.setVisibility(TextView.VISIBLE);
                        recyclerView.setVisibility(RecyclerView.GONE);
                    } else {
                        tvEmptyMessage.setVisibility(TextView.GONE);
                        recyclerView.setVisibility(RecyclerView.VISIBLE);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "お気に入りの読み込みに失敗しました", Toast.LENGTH_SHORT).show()
                );
    }

    private void showDetails(FavoriteItem item) {
        Intent intent = new Intent(this, IncenseDetailActivity.class);
        intent.putExtra("name", item.getName());
        intent.putExtra("effect", item.getEffect());
        intent.putExtra("imageUrl", item.getImageUrl());
        intent.putExtra("description", item.getDescription());
        intent.putExtra("url", item.getUrl());
        startActivity(intent);
    }

    private void setActivityTitle(String userId, String displayName) {
        Log.d("FavoritesActivity", "setActivityTitle called with userId: " + userId + ", displayName: " + displayName);
        if (userId != null && !userId.isEmpty() && !userId.equals(com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            if (displayName != null && !displayName.isEmpty()) {
                favoritesTitleTextView.setText(displayName + "さんのコレクション");
            } else {
                db.collection("users").document(userId).get()
                        .addOnSuccessListener(documentSnapshot -> {
                            Log.d("FavoritesActivity", "Document snapshot received for userId: " + userId);
                            if (documentSnapshot.exists()) {
                                String username = documentSnapshot.getString("displayName"); // 修改为 displayName
                                Log.d("FavoritesActivity", "displayName: " + username);
                                if (username != null && !username.isEmpty()) {
                                    favoritesTitleTextView.setText(username + "さんのコレクション");
                                } else {
                                    Log.d("FavoritesActivity", "displayName is null or empty");
                                    favoritesTitleTextView.setText("他の人のコレクション");
                                }
                            } else {
                                Log.d("FavoritesActivity", "Document does not exist for userId: " + userId);
                                favoritesTitleTextView.setText("他の人のコレクション");
                            }
                        })
                        .addOnFailureListener(e -> {
                            Log.e("FavoritesActivity", "Error getting username", e);
                            favoritesTitleTextView.setText("他の人のコレクション");
                        });
            }
        } else {
            favoritesTitleTextView.setText("私のコレクション");
        }
    }
}